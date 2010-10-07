/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

package org.td4j.examples.helloworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.td4j.swing.workbench.Workbench;

public class LocalizedHelloWorldExample {
	
	public static void main(String[] args) {
		Workbench.start(new HelloWorld());
	}
	
	
	public static class HelloWorld {		
		private static final Map<Locale, String> messageMap = new HashMap<Locale, String>();
		
		static {
			messageMap.put(Locale.ENGLISH, "Hi %s! How are you?");
			messageMap.put(Locale.GERMAN, "Hallo %s! Wie gehts?");
			messageMap.put(Locale.FRENCH, "Salut %s! Ça va?");
			messageMap.put(Locale.ITALIAN, "Ciao %s! Come stai?");
		}

		public final List<Locale> localeOptions = new ArrayList<Locale>(messageMap.keySet());
		public Locale locale;
		
		public String name = "world";
		
		public String getGreeting() {
			return String.format(locale != null ? messageMap.get(locale) : "%s", name != null ? name : "");
		}
		
	}

}
