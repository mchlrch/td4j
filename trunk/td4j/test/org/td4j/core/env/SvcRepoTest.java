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

package org.td4j.core.env;

public class SvcRepoTest {
	
	public static void main(String[] args) {
		final SvcRepoTest test = new SvcRepoTest();
		
		test.testSingletonSvcInstance();
		test.testSingletonSvcByClass();
		
		test.testSvcWithDependency();
		test.testSvcWithDependency2();
		
		// TODO
//		test.testSvcProviderInstance();
//		test.testSvcProviderByClass();
	}


	// ---------------------------------------------------------
	private void testSingletonSvcInstance() {
		logDelimiter("testSingletonSvcInstance()");
		
		final SvcRepository repo = new DefaultSvcRepository();
		
		repo.setSingletonService(HelloService.class, new HelloServiceImpl("impl instance"));
		repo.getService(HelloService.class).sayHello();		
	}
	
	private void testSingletonSvcByClass() {
		logDelimiter("testSingletonSvcByClass()");
		
		final SvcRepository repo = new DefaultSvcRepository();
		
		// TODO
		// HelloServiceImpl() OR HelloServiceImpl(Environment)
		repo.setSingletonService(HelloService.class, HelloServiceImpl.class);
		repo.getService(HelloService.class).sayHello();		
	}

	
	private void testSvcWithDependency() {
		logDelimiter("testSvcWithDependency()");
		
		final SvcRepository repo = new DefaultSvcRepository();
		
		repo.setSingletonService(HelloService.class, PersonalHelloServiceImpl.class);
		repo.setSingletonService(IdentityService.class, new IdentityServiceImpl());		
		repo.getService(HelloService.class).sayHello();		
	}
	
	private void testSvcWithDependency2() {
		logDelimiter("testSvcWithDependency2()");
		
		final SvcRepository repo = new DefaultSvcRepository();		
		repo.setSingletonService(HelloService.class, PersonalHelloServiceImpl2.class);
		repo.setSingletonService(IdentityService.class, new IdentityServiceImpl());
		
		SvcRepo.init(repo);
		repo.getService(HelloService.class).sayHello();		
	}
	
	
	private void testSvcProviderInstance() {
		logDelimiter("testSvcProviderInstance()");

		final SvcRepository repo = new DefaultSvcRepository();
		
		// @ServiceProvider(HelloService.class)
		repo.setServiceProvider(HelloService.class, new HelloServiceProvider());
		repo.getService(HelloService.class).sayHello();
		
	}
	
	private void testSvcProviderByClass() {
		logDelimiter("testSvcProviderByClass()");
		
		final SvcRepository repo = new DefaultSvcRepository();
		
		// TODO
		// HelloServiceProvider() OR HelloServiceProvider(Environment)
		repo.setServiceProvider(HelloService.class, HelloServiceProvider.class);
		repo.getService(HelloService.class).sayHello();
	}
	

	
	private void logDelimiter(String msg) {
		System.out.println("-------------------------------------------------");
		System.out.println(msg);
	}

}
