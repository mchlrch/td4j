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

package org.td4j.core.tk.env;

import org.td4j.core.tk.env.SvcRepository;

public class SvcRepoTest {
	
	public static void main(String[] args) {
		final SvcRepoTest test = new SvcRepoTest();
		
		test.testSingletonSvcInstance();
		test.testSingletonSvcByClass();
		
		test.testSvcWithDependency();
		
	}


	// ---------------------------------------------------------
	private void testSingletonSvcInstance() {
		logDelimiter("testSingletonSvcInstance()");
		
		final SvcRepository repo = new SvcRepository();
		
		repo.setSingletonService(HelloService.class, new HelloServiceImpl("impl instance"));
		repo.getService(HelloService.class).sayHello();		
	}
	
	private void testSingletonSvcByClass() {
		logDelimiter("testSingletonSvcByClass()");
		
		final SvcRepository repo = new SvcRepository();
		
		// TODO
		// HelloServiceImpl() OR HelloServiceImpl(Environment)
		repo.setSingletonService(HelloService.class, HelloServiceImpl.class);
		repo.getService(HelloService.class).sayHello();		
	}

	
	private void testSvcWithDependency() {
		logDelimiter("testSvcWithDependency()");
		
		final SvcRepository repo = new SvcRepository();
		
		repo.setSingletonService(HelloService.class, PersonalHelloServiceImpl.class);
		repo.setSingletonService(IdentityService.class, new IdentityServiceImpl());		
		repo.getService(HelloService.class).sayHello();		
	}
	
	
	private void logDelimiter(String msg) {
		System.out.println("-------------------------------------------------");
		System.out.println(msg);
	}

}
