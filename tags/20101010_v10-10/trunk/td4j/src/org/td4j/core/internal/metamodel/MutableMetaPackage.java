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
import java.util.Collections;
import java.util.List;

import org.td4j.core.metamodel.MetaPackage;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

public class MutableMetaPackage extends MetaPackage {
	
	private static final String PKG_SEPARATOR = ".";
	
	private final String simpleName;
	private final String fqn;
	
	private final MutableMetaPackage parent;
	private final List<MetaPackage> children = new ArrayList<MetaPackage>();
	
	private final int hashCode;
	
	// TODO: handle DEFAULT (root) package without name -> root pkg is the only one not requiring a name (necessary to allow Classes in the default Pkg -> write testcase for that)
	protected MutableMetaPackage(String simpleName, MutableMetaPackage parent) {
		this.simpleName = StringTK.enforceNotEmpty(simpleName, "simpleName");		
		this.fqn = parent != null ? parent.getName() + PKG_SEPARATOR + simpleName : simpleName;
		
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
			
			this.hashCode = 41 * (41 + parent.hashCode()) + simpleName.hashCode();
		} else {
			this.hashCode = simpleName.hashCode();
		}		
	}

	public String getName() {
		return fqn;
	}
	
	public String getSimpleName() {
		return simpleName;
	}
	
	public MutableMetaPackage getParent() {
		return parent;
	}
	
	public List<MetaPackage> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	protected void addChild(MetaPackage pkg) {
		ObjectTK.enforceNotNull(pkg, "pkg");
		
		// avoid duplicate packages
		for (MetaPackage child : children) {
			if (child.equals(pkg)) throw new IllegalStateException("duplicate package: " + child);
		}
		
		children.add(pkg);
	}
	
	
	// =============================================================
	
	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof MutableMetaPackage) {
			final MutableMetaPackage that = (MutableMetaPackage) other;
			return that.canEqual(this) && ObjectTK.equal(this.getParent(), that.getParent()) && ObjectTK.equal(this.getSimpleName(), that.getSimpleName());
			
		} else {
			return false;
		}
	}	
	
	public boolean canEqual(Object other) {
		return other instanceof MutableMetaPackage;
	}	

}
