/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008 Michael Rauch

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

package org.td4j.examples.helloverbose;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.td4j.core.model.Observable;



public class HelloModel extends Observable {

	private static final Map<Locale, String> messageMap = new HashMap<Locale, String>();

	static {
		messageMap.put(Locale.ENGLISH, "Hi %s! How are you?");
		messageMap.put(Locale.GERMAN, "Hallo %s! Wie gehts?");
		messageMap.put(Locale.FRENCH, "Salut %s! Ça va?");
		messageMap.put(Locale.ITALIAN, "Ciao %s! Come stai?");
	}

	private String name;
	private Locale locale;

	// PEND: temp only, until CollectionMethodPlug is implemented
	private Set<Locale> localeChoiceField = messageMap.keySet();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		changeSupport.fireStateChange();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		changeSupport.fireStateChange();
	}

	public Set<Locale> getLocaleChoice() {
		return messageMap.keySet();
	}

	public String getMessage() {
		return String.format(locale != null ? messageMap.get(locale) : "%s", name != null ? name : "");
	}

}
