/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.model.DefaultDataConnectorFactory;
import org.td4j.core.binding.model.ICollectionDataConnector;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.internal.capability.ListDataAccessAdapter;
import org.td4j.core.internal.capability.ScalarDataAccessAdapter;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.Observable;
import org.td4j.core.reflect.DefaultModelInspector;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.internal.binding.ListModelAdapter;
import org.td4j.swing.internal.workbench.ByClassNameFormFactory;
import org.td4j.swing.internal.workbench.CompositeFormFactory;
import org.td4j.swing.internal.workbench.GenericEditorFactory;
import org.td4j.swing.internal.workbench.GenericFormFactory;
import org.td4j.swing.workbench.Editor.EditorContent;

public class Workbench extends JFrame {
  private static final long serialVersionUID = 1L;

  private static Workbench INSTANCE; // static singleton, initialized via
  // setup() method

  private final IEditorFactory editorFactory;
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
    final IDataConnectorFactory connectorFactory = new DefaultDataConnectorFactory();
    final ModelInspector modelInspector = new DefaultModelInspector(connectorFactory);

    final GenericFormFactory genericFormFactory = new GenericFormFactory(modelInspector);
    final ByClassNameFormFactory byClassNameFormFactory = new ByClassNameFormFactory();
    final CompositeFormFactory formFactory = new CompositeFormFactory(byClassNameFormFactory,
        genericFormFactory);

    final IEditorFactory editorFactory = new GenericEditorFactory(modelInspector, formFactory,
        connectorFactory);
    start(editorFactory, initialNavigation, sidebarEntries);
  }

  public static void start(final IEditorFactory editorFactory, final Object initialNavigation,
      final Class<?>... sidebarEntries) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        final Workbench wb = setup(editorFactory, Arrays.asList(sidebarEntries));

        if (initialNavigation != null) {
          wb.seek(initialNavigation);
        }

        wb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wb.setSize(new Dimension(680, 820));
        wb.setVisible(true);
      }
    });
  }

  private static Workbench setup(final IEditorFactory editorFactory,
      final List<Class<?>> sidebarEntries) {
    if (INSTANCE == null) {
      synchronized (Workbench.class) {
        if (INSTANCE == null) {
          INSTANCE = new Workbench(editorFactory, sidebarEntries);
        }
      }
    }
    else {
      throw new IllegalStateException("Workbench is already up.");
    }
    return INSTANCE;
  }

  private Workbench(final IEditorFactory editorFactory, final List<Class<?>> sidebarEntries) {
    if (editorFactory == null)
      throw new NullPointerException("editorFactory");

    this.editorFactory = editorFactory;
    this.navigator = new Navigator(this);

    this.sidebarModel = new SidebarModel(this, sidebarEntries);
    final Component sidebar = createSidebar(sidebarModel);

    splitPane = new JSplitPane();
    splitPane.setLeftComponent(sidebar);
    getContentPane().add(splitPane);

    setTitle("td4j");
  }

  private Component createSidebar(final SidebarModel model) {
    final JList classChooser = new JList();
    classChooser.setCellRenderer(new ClassNameRenderer());
    final IDataConnectorFactory connectorFactory = new DefaultDataConnectorFactory();

    // options to choose from
    final ICollectionDataConnector classOptionsConnector = connectorFactory
        .createCollectionFieldConnector(SidebarModel.class, "currentClassOptions");
    final ListDataProxy classOptionsProxy = new ListDataProxy(new ListDataAccessAdapter(classOptionsConnector), "currentClassOptions", null);
    final ListController classOptionsController = new ListController(classChooser, classOptionsProxy);

    // choice
    final IScalarDataConnector currentClassConnector = connectorFactory
        .createScalarMethodConnector(SidebarModel.class, "currentClass");
    final ScalarDataProxy currentClassProxy = new ScalarDataProxy(new ScalarDataAccessAdapter(currentClassConnector), "currentClass");
    final SelectionController currentClassController = new SelectionController(classChooser
        .getSelectionModel(), new ListModelAdapter(classChooser.getModel()), currentClassProxy);

    classOptionsController.getDataProxy().setModel(model);
    currentClassController.getDataProxy().setModel(model);

    return classChooser;
  }

  public Navigator getNavigator() {
    return navigator;
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
      this.workbench = ObjectTK.enforceNotNull(wb, "wb");
      this.currentClassOptions = ObjectTK.enforceNotNull(sidebarEntries, "sidebarEntries");
    }

    public Class<?> getCurrentClass() {
      return currentClass;
    }

    // called via sidebar selection
    public void setCurrentClass(Class<?> currentClass) {
      if (ObjectTK.equal(this.currentClass, currentClass))
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
    public Component getListCellRendererComponent(JList list, Object value, int index,
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
