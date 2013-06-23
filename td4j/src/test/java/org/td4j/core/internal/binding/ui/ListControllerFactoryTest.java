/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2013 Michael Rauch

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

package org.td4j.core.internal.binding.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.internal.metamodel.JavaMetaModel;
import org.td4j.core.metamodel.MetaClassProvider;
import org.testng.annotations.Test;

import ch.miranet.commons.container.Option;
import ch.miranet.commons.service.SvcProvider;
import ch.miranet.commons.service.SvcRepository;

public class ListControllerFactoryTest {

	@Test
	public void testBindField() {
		final Option<MetaClassProvider> mcp = Option.createNone();
		final MockListControllerFactory factory = createFactory(Person1.class, mcp);

		final Person1 homer = new Person1("homer", new Person1("bart"));

		final String fieldName = "children";
		final MockListController ctrl = factory.bindField(fieldName);

		assert ctrl.dataProxy != null;
		assert ctrl.dataProxy.getName() != null;
		assert ctrl.dataProxy.getName().equals(fieldName);

		ctrl.dataProxy.setContext(homer);
		assert ctrl.dataProxy.readValue() != null;
		assert ctrl.dataProxy.readValue().equals(homer.children);
	}

	@Test
	public void testBindField_nestedProperties_withMetaClassProvider() {
		final SvcProvider svcProvider = new SvcRepository();
		final Option<MetaClassProvider> mcp = new Option<MetaClassProvider>(new JavaMetaModel(svcProvider));
		final MockListControllerFactory factory = createFactory(Person1.class, mcp);

		final MockListController ctrl = factory.bindField("children");

		assert ctrl.dataProxy != null;
		assert ctrl.dataProxy.getNestedProperties().length == 1;
		assert ctrl.dataProxy.getNestedProperties()[0].getName().equals("name");
	}

	@Test
	public void testBindField_nestedProperties_noMetaClassProvider_fallbackToString() {
		final Option<MetaClassProvider> mcp = Option.createNone();
		final MockListControllerFactory factory = createFactory(Person1.class, mcp);

		final MockListController ctrl = factory.bindField("children");

		assert ctrl.dataProxy != null;
		assert ctrl.dataProxy.getNestedProperties().length == 1;
		assert ctrl.dataProxy.getNestedProperties()[0].getName().equals("toString");
	}

	// ----------------------------------------------------------------------
	public static class Person1 {
		public String name;
		public final List<Person1> children = new ArrayList<Person1>();

		private Person1(String name, Person1... kids) {
			this.name = name;
			this.children.addAll(Arrays.asList(kids));
		}

	}

	// ----------------------------------------------------------------------
	private static <T> MockListControllerFactory createFactory(Class<T> ctxType, Option<MetaClassProvider> metaClassProvider) {
		final Mediator<T> mediator = new Mediator<>(ctxType);
		final MockListControllerFactory result = new MockListControllerFactory(mediator, metaClassProvider);
		return result;
	}

	private static class MockListControllerFactory extends ListControllerFactory<MockListController> {
		public MockListControllerFactory(Mediator<?> mediator, Option<MetaClassProvider> metaClassProvider) {
			super(mediator, new JavaDataConnectorFactory(), metaClassProvider);
		}

		@Override
		protected MockListController createController(ListDataProxy proxy) {
			return new MockListController(proxy);
		}
	}

	private static class MockListController {
		private final ListDataProxy dataProxy;

		private MockListController(ListDataProxy dataProxy) {
			this.dataProxy = dataProxy;
		}
	}

}
