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

package org.td4j.core.reflect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.td4j.core.binding.model.DefaultDataConnectorFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;



// PEND: what about static field/methods for plugs?
public class DefaultModelInspectorTest {

	private DefaultModelInspector inspector;

	@BeforeClass
	public void setup() {
		final DefaultDataConnectorFactory connectorFactory = new DefaultDataConnectorFactory();
		inspector = new DefaultModelInspector(connectorFactory);
	}

	@Test
	public void testPublicExposure() {
		assertPlugSet(inspector.getConnectors(ClsA.class), "int4", "integer4");
	}

	@Test
	public void testXRayExposure() {
		assertPlugSet(inspector.getConnectors(ClsB.class), "int1", "int2", "int3", "int4", "integer4");
	}

	@Test(dependsOnMethods = { "testPublicExposure" })
	public void testPublicExposureOverriden() {
		assertPlugSet(inspector.getConnectors(ClsC.class), "int1", "integer1");
	}

	@Test(dependsOnMethods = { "testXRayExposure" })
	public void testXRayExposureOverriden() {
		assertPlugSet(inspector.getConnectors(ClsD.class), "int1");
	}

	@Test
	public void testExplicitExposure() {
		assertPlugList(inspector.getConnectors(ClsE.class), "int4", "int1", "integer3", "integer2");
	}

	@Test(expectedExceptions = { UnknownPropertyException.class })
	public void testUnknownPlug() {
		inspector.getConnectors(ClsF.class);
	}

	// ========= helpers =========

	private void assertPlugSet(List<NamedDataConnector> plugs, String... plugNames) {
		assert plugs.size() == plugNames.length;

		Set<String> nameSet = createStringSet(plugNames);
		for (NamedDataConnector plug : plugs) {
			final String plugName = plug.getName();
			assert nameSet.contains(plugName) : String.format("unexpected plug: %s", plugName);
		}
	}

	private Set<String> createStringSet(String... strings) {
		Set<String> result = new HashSet<String>();
		for (String s : strings) {
			result.add(s);
		}
		return result;
	}

	private void assertPlugList(List<NamedDataConnector> plugs, String... plugNames) {
		assert plugs.size() == plugNames.length;

		for (int i = 0, n = plugs.size(); i < n; i++) {
			final NamedDataConnector plug = plugs.get(i);
			final String expectedName = plugNames[i];
			final String actualName = plug.getName();
			assert actualName.equals(expectedName) : String.format("@[%d] expected: %s  actual: %s", i, expectedName, actualName);
		}
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
	@ExposeProperties
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


	@ExposeProperties(level = DefaultModelInspector.Level.PUBLIC)
	public static class ClsC {
		@Expose
		private int int1;
		@Hide
		public int int2;

		@Expose
		private int getInteger1() {
			return int1;
		}

		@Hide
		public int getInteger2() {
			return int2;
		}
	}


	@ExposeProperties(level = DefaultModelInspector.Level.XRAY)
	public static class ClsD {
		private int int1;
		@Hide
		int int2;
	}


	@ExposeProperties( { "int4", "int1", "integer3", "integer2" })
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


	@ExposeProperties("int5")
	public static class ClsF {
		public int int4;
	}

}
