/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009 Michael Rauch

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

package org.td4j.examples.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.td4j.core.reflect.Executable;
import org.td4j.swing.workbench.Workbench;


public class ProjectExample {

	public static void main(String[] args) throws Exception {
		final Project td4j = new Project("td4j", "COPYING", "http://www.td4j.org");
		
		Workbench.start(td4j, Project.class);
	}


	// --------------------------------------

	public static class Project {

		public String name;
		public File licenseFile;
		public URL website;
		
		@Executable(paramNames = { "projectName", "licenseFilePath", "websiteURL" })
		public Project(String projectName, String licenseFilePath, String websiteURL) throws MalformedURLException, FileNotFoundException {
			this.name = projectName;
			editDetails(licenseFilePath, websiteURL);
		}
		
		@Executable(paramNames = {"licenseFilePath", "websiteURL" })
		public void editDetails(String licenseFilePath, String websiteURL) throws MalformedURLException, FileNotFoundException {
			this.licenseFile = new File(licenseFilePath);
			if ( ! licenseFile.exists()) throw new FileNotFoundException(licenseFile.getAbsolutePath());
			
			this.website = new URL(websiteURL);			
		}

		@Override
		public String toString() {
			return "" + name;
		}
	}

}
