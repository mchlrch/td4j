/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009, 2010 Michael Rauch

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

package org.td4j.core.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.td4j.core.model.Observable;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;


public class OpenClass extends Observable {
	
	private final String nameSpace;
	private final String name;
	
	// primitive = class direct anzeigen, keine detailansicht möglich -> keine properties und connectors
	private final boolean primitive;
	
	// leading map, lookup based on featureIdent
	private final Map<Object, FeatureInfo> featuresByIdent;
	
	// keeps track of the number of features per group, to determine the position of a feature upon its addition
	private final Map<Object, Integer> featureCountByGroup;
	
	// lazy caching for lookup based on featureGroup. pure features in the list, as FeatureInfo is implementation detail
	private Map<Object, List<Object>> featuresByGroup;
	
	public OpenClass(String nameSpace, String name, boolean primitive) {
		this.nameSpace = ObjectTK.enforceNotNull(nameSpace, "nameSpace");
		this.name = StringTK.enforceNotEmpty(name, "name");
		this.primitive = primitive;
		
		if ( ! primitive) {
			featuresByIdent = new HashMap<Object, FeatureInfo>();
			featureCountByGroup = new HashMap<Object, Integer>();
		} else {
			featuresByIdent = Collections.emptyMap();
			featureCountByGroup = Collections.emptyMap();
			featuresByGroup = Collections.emptyMap();
		}
	}
	
	public String getNamespace() {
		return nameSpace;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isPrimitive() {
		return primitive;
	}
	
	// rebuild featuresByGroup
	private Map<Object, List<Object>> getFeaturesByGroup0() {
		if (featuresByGroup == null) {
			final Map<Object, List<FeatureInfo>> tempMap = new HashMap<Object, List<FeatureInfo>>();			
			for (FeatureInfo featInfo : featuresByIdent.values()) {
				List<FeatureInfo> featuresByGroup = tempMap.get(featInfo.featureGroup);
				if (featuresByGroup == null) {
					featuresByGroup = new ArrayList<FeatureInfo>();
					tempMap.put(featInfo.featureGroup, featuresByGroup);
				}
				featuresByGroup.add(featInfo);
			}
			
			// sort feature list per group, based on indexInGroup
			for (List<FeatureInfo> featuresByGroup : tempMap.values()) {
				Collections.sort(featuresByGroup);
			}
			
			// populate featuresByGroup
			featuresByGroup = new HashMap<Object, List<Object>>();
			for (Entry<Object, List<FeatureInfo>> tempEntry : tempMap.entrySet()) {
				final List<Object> featList = new ArrayList<Object>();
				for (FeatureInfo featInfo : tempEntry.getValue()) {
					featList.add(featInfo.feature);
				}
				featuresByGroup.put(tempEntry.getKey(), featList);
			}
		}
		return featuresByGroup;
	}
	
	public Set<Object> getFeatureGroups() {		
		return Collections.unmodifiableSet(featureCountByGroup.keySet());
	}
	
	public List<Object> getFeatures(Object featureGroup) {
		final List<Object> features = getFeaturesByGroup0().get(featureGroup);
		return features != null ? Collections.unmodifiableList(features) : Collections.emptyList();		
	}
	
	public <T> List<T> getFeatures(Object featureGroup, Class<T> featureType) {
		final List<Object> features = getFeaturesByGroup0().get(featureGroup);
		if (features != null) {
			final List<T> result = new ArrayList<T>(features.size());
			for (Object f : features) {
				result.add( (T) f );
			}
			return result;
		} else {
			final List<T> result = Collections.emptyList();
			return result;
		}
	}
	
	public Set<Object> getFeatureIdentities() {
		return Collections.unmodifiableSet(featuresByIdent.keySet());
	}
	
	public <T> T getFeature(Object featureIdent) {
		return (T) featuresByIdent.get(featureIdent).feature;		
	}
	
	public void addFeature(Object featureGroup, Object featureIdent, Object feature) {
		ObjectTK.enforceNotNull(featureGroup, "featureGroup");
		ObjectTK.enforceNotNull(featureIdent, "featureIdent");
		ObjectTK.enforceNotNull(feature, "feature");
		
		if (featuresByIdent.containsKey(featureIdent)) throw new IllegalStateException("featureIdent not unique: " + featureIdent);
				
		// register feature under the given ident
		Integer featureCountOfType = featureCountByGroup.get(featureGroup);
		if (featureCountOfType == null) {
			featureCountOfType = 0;
		}
		final FeatureInfo featureInfo = new FeatureInfo(featureGroup, featureCountOfType, featureIdent, feature); 
		featuresByIdent.put(featureIdent, featureInfo);
		featureCountByGroup.put(featureGroup, featureCountOfType + 1);		

		// invalidate featuresByType cache
		featuresByGroup = null;
		
		// PEND: besser spezifische listener methoden anbieten und in event featureType spezifizieren: properties, connectors und actions unterscheiden
		changeSupport.fireStateChange();
	}
	
	
	@Override
	public String toString() {		
		return "" + nameSpace + "::" + name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof OpenClass)) return false;
		
		final OpenClass that = (OpenClass) obj;
		return this.nameSpace.equals(that.nameSpace)
				&& this.name.equals(that.name);
	}
	
	@Override
	public int hashCode() {
		int hash = nameSpace.hashCode();
		hash = 31*hash + name.hashCode();
		return hash;
	}


    // wrapper to save information at feature registeration (indexInType)
	private static class FeatureInfo implements Comparable<FeatureInfo> {
		private final Object featureGroup;
		private final int indexInGroup;
		private final Object featureIdent;
		private final Object feature;
		private FeatureInfo(Object featureGroup, int indexInGroup, Object featureIdent, Object feature) {
			this.featureGroup = ObjectTK.enforceNotNull(featureGroup, "featureGroup");
			
			if (indexInGroup < 0) throw new IllegalArgumentException("indexInGroup is negative: " + indexInGroup);
			this.indexInGroup = indexInGroup;
			
			this.featureIdent = ObjectTK.enforceNotNull(featureIdent, "featureIdent");
			this.feature = ObjectTK.enforceNotNull(feature, "feature");
		}		
		@Override
		public int compareTo(FeatureInfo that) {
			return this.indexInGroup - that.indexInGroup;
		}
	}

}
