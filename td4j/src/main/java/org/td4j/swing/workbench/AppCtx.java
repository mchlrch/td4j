/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.td4j.core.metamodel.MetaModel;

import ch.miranet.commons.container.FeatureKey;
import ch.miranet.commons.container.FeatureMap;
import ch.miranet.commons.container.ListContainer;
import ch.miranet.commons.service.SvcProvider;

/**
 * Only use AppCtx between user code and Workbench. Rest of td4j has no dependency on AppCtx.
 * This also means, that later modifications to AppCtx have no influence on the frameworks behaviour
 * (for example setting EditorFactory after Workbench.start() has been called.
 * 
 * @author mira
 */
public class AppCtx extends FeatureMap {
	
	public static final FeatureKey<SvcProvider> KEY_SVC_PROVIDER = new FeatureKey<SvcProvider>() {
		public Class<SvcProvider> getFeatureType() {	return SvcProvider.class;	}
	};
	
	public static final FeatureKey<Object> KEY_INITIAL_NAVIGATION = new FeatureKey<Object>() {
		public Class<Object> getFeatureType() {  return Object.class;  };
	};
	
	public static final FeatureKey<SidebarEntries> KEY_SIDEBAR_ENTRIES = new FeatureKey<SidebarEntries>() {
		public Class<SidebarEntries> getFeatureType() {  return SidebarEntries.class;  };
	};
	
	public static final FeatureKey<EditorFactory> KEY_EDITOR_FACTORY = new FeatureKey<EditorFactory>() {
		public Class<EditorFactory> getFeatureType() {  return EditorFactory.class;  };
	};
	
	public static final FeatureKey<MetaModel> KEY_METAMODEL = new FeatureKey<MetaModel>() {
		public Class<MetaModel> getFeatureType() {  return MetaModel.class;  };
	};

	
	// ---------------------------------------------------------------------------
	

	public SvcProvider getSvcProvider() {
		return getFeature(KEY_SVC_PROVIDER);
	}
	
	public void setSvcProvider(SvcProvider provider) {
		putFeature(KEY_SVC_PROVIDER, provider);
	}
	
	
	public Object getInitialNavigation() {
		return getFeature(KEY_INITIAL_NAVIGATION);
	}
	
	public void setInitialNavigation(Object initialNavigation) {
		putFeature(KEY_INITIAL_NAVIGATION, initialNavigation);
	}
	
	
	public List<Class<?>> getSidebarEntries() {
		final SidebarEntries entries = getFeature(KEY_SIDEBAR_ENTRIES);
		if (entries != null) {
			return entries.get();
		} else {
			return Collections.emptyList();
		}
	}
	
	public void setSidebarEntries(List<Class<?>> sidebarEntries) {
		final SidebarEntries entries = new SidebarEntries(sidebarEntries);
		putFeature(KEY_SIDEBAR_ENTRIES, entries);
	}
	
	public void setSidebarEntries(Class<?>... sidebarEntries) {
		setSidebarEntries(Arrays.asList(sidebarEntries));
	}
	
	
	public EditorFactory getEditorFactory() {
		return getFeature(KEY_EDITOR_FACTORY);		
	}
	
	public void setEditorFactory(EditorFactory editorFactory) {
		putFeature(KEY_EDITOR_FACTORY, editorFactory);
	}
	
	
	public MetaModel getMetamodel() {
		return getFeature(KEY_METAMODEL);
	}
	
	public void setMetamodel(MetaModel metaModel) {
		putFeature(KEY_METAMODEL, metaModel);
	}
	
	// ---------------------------------------------------------------------------
	
	
	public static class SidebarEntries extends ListContainer<Class<?>> {
		public SidebarEntries(List<Class<?>> entries) {
			super(entries);
		}
	}
	
}
