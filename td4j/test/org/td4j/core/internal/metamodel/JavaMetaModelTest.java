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
import org.td4j.core.tk.env.SvcRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


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

}
