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

package org.td4j.core.binding.model;

import java.lang.reflect.Method;

import org.td4j.core.internal.binding.model.ScalarMethodConnector;
import org.testng.annotations.Test;


public class ScalarMethodConnectorTest {

	@Test
	public void testReadAccess() throws Exception {
		final Method getter = MyA.class.getMethod("getInt1", new Class[0]);
		final ScalarMethodConnector con = new ScalarMethodConnector(MyA.class, getter, null);

		final MyA a = new MyA();
		assert con.canRead(a);
		assert ! con.canWrite(a);
	}

	@Test
	public void testReadWriteAccess() throws Exception {
		final Method getter = MyA.class.getMethod("getInt2", new Class[0]);
		final Method setter = MyA.class.getMethod("setInt2", new Class[] { int.class });
		final ScalarMethodConnector con = new ScalarMethodConnector(MyA.class, getter, setter);

		final MyA a = new MyA();
		assert con.canRead(a);
		assert con.canWrite(a);
	}


	public static class MyA {
		private int int1;
		private int int2;

		public int getInt1() {
			return int1;
		}

		public int getInt2() {
			return int2;
		}

		public void setInt2(int i) {
			int2 = i;
		}
	}

}
