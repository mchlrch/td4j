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

package org.td4j.core.internal.metamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.td4j.core.internal.metamodel.JavaModelInspector.Level;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.reflect.Hide;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ListProperty;
import org.td4j.core.reflect.Property;
import org.td4j.core.reflect.Show;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.core.reflect.UnknownPropertyException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ch.miranet.commons.service.SvcRepository;


// TODO: what about static field/methods for plugs?
public class JavaMetaModelTest {

	private JavaMetaModel metaModel;

	@BeforeClass
	public void setup() {
		metaModel = new JavaMetaModel(new SvcRepository());
	}

	@Test
	public void testPublicExposure() {
		assertProperties(metaModel.getMetaClass(ClsA.class), "int4", "integer4");
	}

	@Test
	public void testXRayExposure() {
		assertProperties(metaModel.getMetaClass(ClsB.class), "int1", "int2", "int3", "int4", "integer4");
	}

	@Test(dependsOnMethods = { "testPublicExposure" })
	public void testPublicExposureOverriden() {
		assertProperties(metaModel.getMetaClass(ClsC.class), "int1", "integer1");
	}

	@Test(dependsOnMethods = { "testXRayExposure" })
	public void testXRayExposureOverriden() {
		assertProperties(metaModel.getMetaClass(ClsD.class), "int1");
	}

	@Test
	public void testExplicitExposure() {
		assertProperties(metaModel.getMetaClass(ClsE.class), "int4", "int1", "integer3", "integer2");
	}

	@Test(expectedExceptions = { UnknownPropertyException.class })
	public void testUnknownPlug() {
		metaModel.getMetaClass(ClsF.class);
	}
	
	@Test
	public void testBidirectionalDependenciesWithoutNestedProperties() {
		final ListProperty propertyRefToH = metaModel.getMetaClass(ClsG.class).getListProperty("refToH");
		final ListProperty propertyRefToG = metaModel.getMetaClass(ClsH.class).getListProperty("refToG");
		
		assert propertyRefToH != null;
		assert propertyRefToG != null;
	}
	
	@Test
	public void testBidirectionalDependenciesWithNestedProperties() {
		final IndividualProperty propertyNameI = metaModel.getMetaClass(ClsI.class).getIndividualProperty("name");
		final IndividualProperty propertyNameJ = metaModel.getMetaClass(ClsJ.class).getIndividualProperty("name");
		
		final ListProperty propertyRefToJ = metaModel.getMetaClass(ClsI.class).getListProperty("refToJ");
		final ListProperty propertyRefToI = metaModel.getMetaClass(ClsJ.class).getListProperty("refToI");
		
		assert propertyRefToJ != null;
		assert propertyRefToI != null;
		
		assert propertyRefToJ.getNestedProperties()[0].equals(propertyNameJ);
		assert propertyRefToI.getNestedProperties()[0].equals(propertyNameI);
	}
	
	@Test
	public void testSelfReferencingProperty() {
		final IndividualProperty refToSelf = metaModel.getMetaClass(ClsK.class).getIndividualProperty("refToSelf");
		assert refToSelf != null;
	}
	
	@Test
	public void testExplicitNestedPropertiesOnPOJOListField() {
		assertUniqueNestedProperty(ClsM1.class, "ref", "name");
	}
	
	@Test
	public void testExplicitNestedPropertiesOnEnumListField() {
		assertUniqueNestedProperty(ClsM2.class, "ref", "name");		
	}
	
	@Test
	public void testExplicitNestedPropertiesOnPOJOArrayField() {
		assertUniqueNestedProperty(ClsM3.class, "ref", "name");
	}
	
	@Test
	public void testExplicitNestedPropertiesOnEnumArrayField() {
		assertUniqueNestedProperty(ClsM4.class, "ref", "name");
	}
	
	@Test
	public void testExplicitNestedPropertiesOnPOJOListMethod() {
		assertUniqueNestedProperty(ClsN1.class, "reference", "name");
	}
	
	@Test
	public void testExplicitNestedPropertiesOnEnumListMethod() {
		assertUniqueNestedProperty(ClsN2.class, "reference", "name");
	}
	
	@Test
	public void testExplicitNestedPropertiesOnPOJOArrayMethod() {
		assertUniqueNestedProperty(ClsN3.class, "reference", "name");
	}	
	
	@Test
	public void testExplicitNestedPropertiesOnEnumArrayMethod() {
		assertUniqueNestedProperty(ClsN4.class, "reference", "name");
	}	
	
	private void assertUniqueNestedProperty(Class<?> cls, String listPropertyName, String nestedPropertyName) {
		final ListProperty listProperty = metaModel.getMetaClass(cls).getListProperty(listPropertyName);
		final IndividualProperty[] nestedProperties = listProperty.getNestedProperties(); 
		
		assert nestedProperties.length == 1;
		assert nestedProperties[0] != null;
		assert nestedProperties[0].getName().equals(nestedPropertyName);		
	}

	// ========= helpers =========

	private void assertProperties(MetaClass metaClass, String... propertyNames) {
		final List<IndividualProperty> individualProps = metaClass.getIndividualProperties();
		final List<ListProperty> listProps = metaClass.getListProperties();
		
		final List<Property> allProps = new ArrayList<Property>();
		allProps.addAll(individualProps);
		allProps.addAll(listProps);
		
		assert allProps.size() == propertyNames.length;

		final Set<String> nameSet = createStringSet(propertyNames);
		for (Property prop : allProps) {
			final String pName = prop.getName();
			assert nameSet.contains(pName) : String.format("unexpected property: %s", pName);
		}
		
	}

	private Set<String> createStringSet(String... strings) {
		final Set<String> result = new HashSet<String>();
		for (String s : strings) {
			result.add(s);
		}
		return result;
	}

	
	// default exposure is DefaultModelInspector.Level.PUBLIC
	public static class ClsA {
		private int int1;
		int int2;
		protected int int3;
		public int int4;

		private int getInteger1() {
			return int1;
		}

		int getInteger2() {
			return int2;
		}

		protected int getInteger3() {
			return int3;
		}

		public int getInteger4() {
			return int4;
		}
	}


	// default exposure for annotation is DefaultModelInspector.Level.XRAY
	@ShowProperties
	public static class ClsB {
		private int int1;
		int int2;
		protected int int3;
		public int int4;

		private int getInteger1() {
			return int1;
		}

		int getInteger2() {
			return int2;
		}

		protected int getInteger3() {
			return int3;
		}

		public int getInteger4() {
			return int4;
		}
	}


	@ShowProperties(level = Level.PUBLIC)
	public static class ClsC {
		@Show
		private int int1;
		@Hide
		public int int2;

		@Show
		private int getInteger1() {
			return int1;
		}

		@Hide
		public int getInteger2() {
			return int2;
		}
	}


	@ShowProperties(level = Level.XRAY)
	public static class ClsD {
		private int int1;
		@Hide
		int int2;
	}


	@ShowProperties( { "int4", "int1", "integer3", "integer2" })
	public static class ClsE {
		private int int1;
		int int2;
		protected int int3;
		public int int4;

		private int getInteger1() {
			return int1;
		}

		int getInteger2() {
			return int2;
		}

		protected int getInteger3() {
			return int3;
		}

		public int getInteger4() {
			return int4;
		}
	}


	@ShowProperties("int5")
	public static class ClsF {
		public int int4;
	}
	
	
	// -----------------------------------------------
	
	/**
	 * Bidirectional dependency without nested properties
	 * 
	 * ClsG.refToH: type=ClsH
	 * ClsH.refToG: type=ClsG
	 */
	public static class ClsG {
		public List<ClsH> refToH;
	}
	
	public static class ClsH {
		public List<ClsG> refToG;
	}
	
	
	/**
	 * Bidirectional dependency with nested properties
	 * 
	 * ClsI.refToJ: type=ClsJ, nestedProperties=name
	 * ClsJ.refToI: type=ClsI, nestedProperties=name
	 */
	public static class ClsI {
		@ShowProperties("name")
		public List<ClsJ> refToJ;
		public String name;
	}
	
	public static class ClsJ {
		@ShowProperties("name")
		public List<ClsI> refToI;
		public String name;
	}
	
	
	public static class ClsK {
		public ClsK refToSelf;
	}

	
	//-----------------------------------------------
	public static class ClsL {
		public String name;
		public String address;
	}
	
	public static enum EnumL {
		A("a"), B("b"), C("c");		
		private String name;
		private EnumL(String name) { this.name = name; }
		public String getName()    { return name; }
		public String getAddress() { return "foo"; }
	}
	
	public static class ClsM1 {
		@ShowProperties({ "name" })
		public List<ClsL> ref;
	}
	
	public static class ClsM2 {
		@ShowProperties({ "name" })
		public List<EnumL> ref;
	}	
	
	public static class ClsM3 {
		@ShowProperties({ "name" })
		public ClsL[] ref;
	}
	
	public static class ClsM4 {
		@ShowProperties({ "name" })
		public EnumL[] ref;
	}
	
	public static class ClsN1 {
		@ShowProperties({ "name" })
		public List<ClsL> getReference() { return null; }
	}
	
	public static class ClsN2 {
		@ShowProperties({ "name" })
		public List<EnumL> getReference() { return null; }
	}	
	
	public static class ClsN3 {
		@ShowProperties({ "name" })
		public ClsL[] getReference() { return null; }
	}
	
	public static class ClsN4 {
		@ShowProperties({ "name" })
		public EnumL[] getReference() { return null; }
	}	
	
}
