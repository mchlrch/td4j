/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

  td4j is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  td4j is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with td4j.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

package org.td4j.swing.workbench;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.internal.metamodel.JavaMetaModel;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.Observable;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.internal.workbench.ByClassNameFormFactory;
import org.td4j.swing.internal.workbench.CompositeFormFactory;
import org.td4j.swing.internal.workbench.GenericEditorFactory;
import org.td4j.swing.internal.workbench.GenericFormFactory;
import org.td4j.swing.workbench.Editor.EditorContent;

import ch.miranet.commons.TK;
import ch.miranet.commons.service.SvcProvider;
import ch.miranet.commons.service.SvcRepository;

public class Workbench extends JFrame {
  private static final long serialVersionUID = 1L;

  private static Workbench INSTANCE; // static singleton, initialized via setup() method

  private final AppCtx appCtx;
  
  private final EditorFactory editorFactory;
  
  private final HashMap<Class<?>, Editor> editorCache = new HashMap<Class<?>, Editor>();
  private final HashMap<Class<?>, EditorContent> lastContentCache = new HashMap<Class<?>, EditorContent>();

  private Editor visibleEditor;

  private final Navigator navigator;
  private final SidebarModel sidebarModel;

  private final JSplitPane splitPane;

  public static Workbench getInstance() {
    if (INSTANCE == null)
      throw new IllegalStateException("no workbench instance available. Try Workbench.start()");

    return INSTANCE;
  }
  
	public static void start(final Object initialNavigation, final Class<?>... sidebarEntries) {
		final AppCtx appCtx = new AppCtx();
		appCtx.setInitialNavigation(initialNavigation);
		appCtx.setSidebarEntries(sidebarEntries);

    start(appCtx);
  }

	public static void start(final AppCtx appCtx) {
		final SvcProvider svcProvider = initSvcProvider(appCtx);
		final MetaModel metaModel = initMetaModel(appCtx, svcProvider);
		final EditorFactory editorFactory = initEditorFactory(appCtx, metaModel);
		
		final List<Class<?>> sidebarEntries = appCtx.getSidebarEntries();
		final Object initialNavigation = appCtx.getInitialNavigation();
		
		SwingUtilities.invokeLater(new Runnable() {
      public void run() {      	
      	setLookAndFeel();
      	
        final Workbench wb = setup(appCtx, editorFactory, sidebarEntries);

        if (initialNavigation != null) {
          wb.seek(initialNavigation);
        }

        wb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wb.setSize(new Dimension(680, 820));
        wb.setVisible(true);
      }
    });
  }
  

	private static SvcProvider initSvcProvider(AppCtx appCtx) {
		SvcProvider svcProvider = appCtx.getSvcProvider();
		if (svcProvider == null) {
			svcProvider = new SvcRepository();
			appCtx.setSvcProvider(svcProvider);
		}
		return svcProvider;
	}

	private static MetaModel initMetaModel(AppCtx appCtx, SvcProvider svcProvider) {
  	MetaModel metaModel = appCtx.getMetamodel();
  	if (metaModel == null) {
  		metaModel = new JavaMetaModel(svcProvider);
  		appCtx.setMetamodel(metaModel);
  	}  	
		
		return metaModel;
	}

	private static EditorFactory initEditorFactory(AppCtx appCtx, MetaModel metaModel) {
		EditorFactory editorFactory = appCtx.getEditorFactory();
		if (editorFactory == null) {
			editorFactory = createDefaultEditorFactory(metaModel);
			appCtx.setEditorFactory(editorFactory);
		}
		return editorFactory;
	}
  
  private static EditorFactory createDefaultEditorFactory(MetaModel metaModel) {
  	final GenericFormFactory genericFormFactory = new GenericFormFactory(metaModel);
  	final ByClassNameFormFactory byClassNameFormFactory = new ByClassNameFormFactory();
  	final CompositeFormFactory formFactory = new CompositeFormFactory(byClassNameFormFactory,
  			genericFormFactory);
  	
  	final EditorFactory editorFactory = new GenericEditorFactory(metaModel, formFactory);		
  	return editorFactory;
  }

	private static void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// use default l&f
		}
  }

  private static Workbench setup(final AppCtx appCtx, final EditorFactory editorFactory, final List<Class<?>> sidebarEntries) {
    if (INSTANCE == null) {
      synchronized (Workbench.class) {
        if (INSTANCE == null) {
          INSTANCE = new Workbench(appCtx, editorFactory, sidebarEntries);
        }
      }
    }
    else {
      throw new IllegalStateException("Workbench is already up.");
    }
    return INSTANCE;
  }

  private Workbench(final AppCtx appCtx, final EditorFactory editorFactory, final List<Class<?>> sidebarEntries) {
  	this.appCtx = TK.Objects.assertNotNull(appCtx, "appCtx");
    this.editorFactory = TK.Objects.assertNotNull(editorFactory, "editorFactory");
    this.navigator = new Navigator(this);

    this.sidebarModel = new SidebarModel(this, sidebarEntries);
    final Component sidebar = createSidebar(sidebarModel);

    splitPane = new JSplitPane();
    splitPane.setLeftComponent(sidebar);
    getContentPane().add(splitPane);

    setTitle("td4j");
  }

  private Component createSidebar(final SidebarModel model) {
    final JList<Object> classChooser = new JList<Object>();
    classChooser.setCellRenderer(new ClassNameRenderer());
    
    final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();

    // options to choose from
    final ListDataConnector classOptionsConnector = connectorFactory.createListFieldConnector(SidebarModel.class, "currentClassOptions");
    final ListDataProxy classOptionsProxy = new ListDataProxy(classOptionsConnector, "currentClassOptions");
    final ListController classOptionsController = new ListController(classChooser, classOptionsProxy);

    // choice
    final IndividualDataConnector currentClassConnector = connectorFactory.createIndividualMethodConnector(SidebarModel.class, "currentClass");
    final IndividualDataProxy currentClassProxy = new IndividualDataProxy(currentClassConnector, "currentClass");
    final SelectionController currentClassController = SelectionController.createSelectionController(classChooser, currentClassProxy);

    classOptionsController.getDataProxy().setContext(model);
    currentClassController.getDataProxy().setContext(model);

    return classChooser;
  }

  public Navigator getNavigator() {
    return navigator;
  }
  
  public AppCtx getAppCtx() {
		return appCtx;
	}

  void show(Class<?> cls) {
    cacheVisibleModel();
    setVisibleClass(cls);
    editor(cls).setContent((Object) null);
  }

  void show(Object obj) {
    if (obj != null) {
      cacheVisibleModel();
      setVisibleClass(obj.getClass());
      editor(obj.getClass()).setContent(obj);

    }
    else if (visibleEditor != null) {
      visibleEditor.setContent((Object) null);
    }
  }

  void show(EditorContent content) {
    if (content != null) {
      cacheVisibleModel();
      final Class<?> contentType = content.getContentType();
      setVisibleClass(contentType);
      editor(contentType).setContent(content);

    }
    else if (visibleEditor != null) {
      visibleEditor.setContent((Object) null);
    }
  }

  private void cacheVisibleModel() {
    final Class<?> visibleClass = getVisibleClass();

    // true at very first navigation
    if (visibleClass == null || visibleEditor == null)
      return;

    final EditorContent content = visibleEditor.getContent();
    lastContentCache.put(visibleClass, content);
  }

  private void setVisibleClass(Class<?> cls) {
    sidebarModel.setCurrentClass0(cls);
  }

  public Class<?> getVisibleClass() {
    return sidebarModel.getCurrentClass();
  }

  public void seek(Object obj) {
    getNavigator().seek(obj);
  }

  public void seek(Class<?> cls) {
    seek(cls, true);
  }

  public void seek(EditorContent content) {
    getNavigator().seek(content);
  }

  public void seek(Class<?> cls, boolean showLastInstance) {
    final EditorContent content = showLastInstance ? lastContentCache.get(cls) : null;
    if (content != null) {
      getNavigator().seek(content);
    }
    else {
      getNavigator().seek(cls);
    }
  }

  protected Editor editor(Class<?> cls) {
    Editor editor = editorCache.get(cls);

    if (editor == null) {
      editor = editorFactory.createEditor(this, cls);
      editorCache.put(cls, editor);

      final JComponent editorComponent = editor.getComponent();
      final JPanel editorPanel = new JPanel(new GridBagLayout());

      editorPanel.add(editorComponent, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
          GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      splitPane.setRightComponent(editorPanel);

    } else {

      final Container editorPanel = editor.getComponent().getParent();
      splitPane.setRightComponent(editorPanel);
    }

    visibleEditor = editor;
    return editor;
  }

  public static class SidebarModel extends Observable {

    private final Workbench workbench;

    List<Class<?>> currentClassOptions;
    private Class<?> currentClass;

    private SidebarModel(final Workbench wb, final List<Class<?>> sidebarEntries) {
      this.workbench = TK.Objects.assertNotNull(wb, "wb");
      this.currentClassOptions = TK.Objects.assertNotNull(sidebarEntries, "sidebarEntries");
    }

    public Class<?> getCurrentClass() {
      return currentClass;
    }

    // called via sidebar selection
    public void setCurrentClass(Class<?> currentClass) {
      if (TK.Objects.equal(this.currentClass, currentClass))
        return;

      workbench.seek(currentClass);
      setCurrentClass0(currentClass);
    }

    // called internally from Workbench show() code
    private void setCurrentClass0(Class<?> currentClass) {
      final ChangeEvent changeEvent = changeSupport.preparePropertyChange("currentClass",
          this.currentClass, currentClass);
      if (changeEvent == null)
        return;

      this.currentClass = currentClass;
      changeSupport.fire(changeEvent);
    }

  }

  private static class ClassNameRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    private Class<?> currentClass;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      if (value instanceof Class) {
        currentClass = (Class<?>) value;
        value = currentClass.getSimpleName();
      }
      else {
        currentClass = null;
      }

      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
      if (currentClass != null) {
        return currentClass.getName();
      }
      else {
        return super.getToolTipText(event);
      }
    }
  }

}
