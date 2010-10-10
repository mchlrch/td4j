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

import java.util.List;

import org.td4j.core.internal.metamodel.MutableMetaPackage;
import org.td4j.core.metamodel.MetaPackage;
import org.testng.annotations.Test;


public class MutableMetaPackageTest {

	@Test
	public void testPkgHierarchy() {
		MutableMetaPackage org   = new MutableMetaPackage("org", null);		
		MutableMetaPackage td4j  = new MutableMetaPackage("td4j", org);
		MutableMetaPackage core  = new MutableMetaPackage("core", td4j);
		MutableMetaPackage swing = new MutableMetaPackage("swing", td4j);
		
		// parent relation
		assert org.getParent() == null;
		assert td4j.getParent().equals(org);
		assert core.getParent().equals(td4j);
		assert swing.getParent().equals(td4j);
		
		// child relation
		assertChildren(org,  td4j);
		assertChildren(td4j, core, swing);
		assertChildren(core);
		assertChildren(swing);
	}
	
	private void assertChildren(MutableMetaPackage pkg, MutableMetaPackage... expectedChildren) {
		final List<MetaPackage> children = pkg.getChildren(); 
		assert children.size() == expectedChildren.length;
		for (MutableMetaPackage child : expectedChildren) {
			assert children.contains(child);
		}
	}
	
	@Test(expectedExceptions = { IllegalStateException.class })
	public void testAvoidDuplicateSubPkg() {
		MutableMetaPackage rootPkg = new MutableMetaPackage("org", null);
		
		MutableMetaPackage subPkg1 = new MutableMetaPackage("td4j", rootPkg);
		MutableMetaPackage subPkg2 = new MutableMetaPackage("td4j", rootPkg);
	}
	
	@Test
	public void testRootPkgEquality() {
		MutableMetaPackage rootPkg1 = new MutableMetaPackage("org", null);
		MutableMetaPackage rootPkg2 = new MutableMetaPackage("org", null);
		
		assert rootPkg1.equals(rootPkg2);
		assert rootPkg2.equals(rootPkg1);
		
		assert rootPkg1.hashCode() == rootPkg2.hashCode();
	}
	
	
	@Test(dependsOnMethods = { "testRootPkgEquality" })
	public void testSubPkgEquality() {
		MutableMetaPackage rootPkg1 = new MutableMetaPackage("org", null);
		MutableMetaPackage rootPkg2 = new MutableMetaPackage("org", null);
		
		MutableMetaPackage subPkg1 = new MutableMetaPackage("td4j", rootPkg1);
		MutableMetaPackage subPkg2 = new MutableMetaPackage("td4j", rootPkg2);
		
		assert subPkg1.equals(subPkg2);
		assert subPkg2.equals(subPkg1);
		
		assert subPkg1.hashCode() == subPkg2.hashCode();		
	}
	
}
