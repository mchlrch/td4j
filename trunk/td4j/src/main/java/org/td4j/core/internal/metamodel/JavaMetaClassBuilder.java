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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.td4j.core.internal.metamodel.JavaModelInspector.FeatureContainer;
import org.td4j.core.internal.metamodel.StatefulJavaMetaClass.State;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaClassProvider;

import ch.miranet.commons.TK;
import ch.miranet.commons.service.SvcProvider;

/**
 * Metaclasses and their properties can expose cyclic dependencies spanning multiple metaclasses.
 * Metaclass creation is therefore implemented as an incremental multi-step process, using a state machine.
 * 
 * The states of a metaclass are as follows:
 * 
 * 1) Shallow Metaclass    (references to metaclass are possible)
 * 2) Shallow Features     (references to metaclass.properties and operations are possible)
 * 3) Qualified Features   (metaclass.properties and operations are qualified,
 *                          therefore further references like nestedProperties are resolved)
 * 4) Qualified Metaclass  (metaclass is qualified/complete)
 * 
 * As long as a metaclass is not in 'qualified' state, it exists only in this builder (ctx.intermediates).
 * As soon as a metaclass is qualified, it is activly registered in the JavaMetaModel.
 * 
 * @author mira
 *
 */
public class JavaMetaClassBuilder {
	
	private final JavaMetaModel metaModel;
	private final SvcProvider svcProvider;
	private final JavaModelInspector featureFactory = new JavaModelInspector();	
	
	
	public JavaMetaClassBuilder(JavaMetaModel metaModel, SvcProvider svcProvider) {
		this.metaModel = TK.Objects.assertNotNull(metaModel, "metaModel");
		this.svcProvider = TK.Objects.assertNotNull(svcProvider, "svcProvider");
	}
	
	public <T> JavaMetaClass<T> buildMetaClass(Class<T> cls) {
		final BuilderContext ctx = new BuilderContext(metaModel, this);		
		final JavaMetaClass<T> mc = createShallowMetaClass(ctx, cls);
		
		// incrementally create all reachable MetaClasses
		processIntermediates(ctx);
		
		return mc;
	}
	
	private void processIntermediates(BuilderContext ctx) {
		while (ctx.hasIntermediates()) {
			final StatefulJavaMetaClass sfmc = ctx.peek();
			final JavaMetaClass<?> mc = sfmc.getMetaClass();
			final Class<?> cls = mc.getJavaClass();
			
			switch (sfmc.getState()) {
			case ShallowMetaClass:
				final FeatureContainer features = featureFactory.createShallowFeatures(cls, svcProvider, ctx);
				mc.setIndividualProperties(features.individualProperties);
				mc.setMutableListProperties(features.listProperties);
				mc.setOperations(features.operations);
				
				ctx.removeFirst();
				ctx.addLast(mc, State.ShallowFeatures);
				break;
				
			case ShallowFeatures:
				final FeatureContainer fcon = new FeatureContainer();
				fcon.individualProperties.addAll(mc.getIndividualProperties());
				fcon.listProperties.addAll(mc.getMutableListProperties());
				fcon.operations.addAll(mc.getOperations());

				featureFactory.refineShallowFeatures(fcon, svcProvider, ctx);				
				
				mc.removeMutableListProperties();
				
				ctx.removeFirst();
				ctx.addLast(mc, State.QualifiedFeatures);
				break;
				
			case QualifiedFeatures:
				// no-op
				ctx.removeFirst();
				ctx.addLast(mc, State.QualifiedMetaClass);
				break;
				
			case QualifiedMetaClass:
				ctx.removeFirst();
				metaModel.putMetaClass(mc);				
				break;
				
			default:
				throw new IllegalStateException();
			}
		}
		
	}
	
	private <T> JavaMetaClass<T> createShallowMetaClass(BuilderContext ctx, Class<T> cls) {
		final JavaMetaClass<T> metaClass = new JavaMetaClass<T>(cls);
		ctx.addLast(metaClass, State.ShallowMetaClass);
		return metaClass;
	}
	
	// ===> logic goes in JavaMetaClassBuilder
  // ------------------------------------------------------------------------
	// ===> state goes in BuilderContext
	
	private static class BuilderContext implements MetaClassProvider {
		private final LinkedList<StatefulJavaMetaClass> intermediatesQueue = new LinkedList<StatefulJavaMetaClass>();
		private final Map<Class<?>, StatefulJavaMetaClass> intermediatesMap = new HashMap<Class<?>, StatefulJavaMetaClass>();
		
		private final JavaMetaModel metaModel;
		private final JavaMetaClassBuilder mcBuilder;
		
		private BuilderContext(JavaMetaModel metaModel, JavaMetaClassBuilder metaClassBuilder) {
			this.metaModel = TK.Objects.assertNotNull(metaModel, "metaModel");
			this.mcBuilder = TK.Objects.assertNotNull(metaClassBuilder, "metaClassBuilder"); 
		}

		@Override
		public MetaClass getMetaClass(Class<?> cls) {
			MetaClass mc = metaModel.getMetaClassReadonly(cls);
			if (mc == null) {
				final StatefulJavaMetaClass sfmc = intermediatesMap.get(cls); 
				mc = sfmc != null ? sfmc.getMetaClass() : null;
			}
			if (mc == null) {
				mc = mcBuilder.createShallowMetaClass(this, cls);
			}
			
			return mc;
		}
		
		private void addLast(JavaMetaClass<?> metaClass, State state) {
			final Class<?> javaCls = metaClass.getJavaClass();
			if (intermediatesMap.containsKey(javaCls)) throw new IllegalStateException();

			final StatefulJavaMetaClass sfmc = new StatefulJavaMetaClass(metaClass, state);
			intermediatesQueue.addLast(sfmc);			
			intermediatesMap.put(javaCls, sfmc);
		}
		
		private StatefulJavaMetaClass peek() {
			return intermediatesQueue.peek();
		}
		
		private StatefulJavaMetaClass removeFirst() {
			final StatefulJavaMetaClass sfmc = intermediatesQueue.removeFirst();
			intermediatesMap.remove(sfmc.getMetaClass().getJavaClass());
			return sfmc;
		}
		
		private boolean hasIntermediates() {
			return ! intermediatesQueue.isEmpty();
		}
		
	}

}
