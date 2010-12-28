package org.td4j.core.internal.binding.model;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

public class IndividualDataConnectorTest extends BaseDataConnectorTest {

	@Test
	public void testIndividualFieldConnectorEquality() {
		final IndividualFieldConnector fc_a1a   = createFieldConnector(ClsA.class, "f1a");
		final IndividualFieldConnector fc_a1a_2 = createFieldConnector(ClsA.class, "f1a");
		
		assert fc_a1a.equals(fc_a1a);
		assert fc_a1a.equals(fc_a1a_2);
		
		assert fc_a1a.hashCode() == fc_a1a_2.hashCode();
		
		final IndividualFieldConnector fc_a1b = createFieldConnector(ClsA.class, "f1b");
		final IndividualFieldConnector fc_b1a = createFieldConnector(ClsB.class, "f1a");
		
		assert ! fc_a1a.equals(fc_a1b);
		assert ! fc_a1a.equals(fc_b1a);
	}
	
	@Test
	public void testIndividualReadOnlyMethodConnectorNoArgsEquality() {
		final IndividualMethodConnector mc_a1a   = createMethodConnector(ClsA.class, "getOneA", null);
		final IndividualMethodConnector mc_a1a_2 = createMethodConnector(ClsA.class, "getOneA", null);
		
		assert mc_a1a.equals(mc_a1a);
		assert mc_a1a.equals(mc_a1a_2);
		
		assert mc_a1a.hashCode() == mc_a1a_2.hashCode();
		
		final IndividualMethodConnector mc_a1b = createMethodConnector(ClsA.class, "getOneB", null);
		final IndividualMethodConnector mc_b1a = createMethodConnector(ClsB.class, "getOneA", null);
		
		assert ! mc_a1a.equals(mc_a1b);
		assert ! mc_a1a.equals(mc_b1a);
	}
	
	@Test
	public void testIndividualReadOnlyMethodConnectorWithArgsEquality() {
		final IndividualMethodConnector mc_a1a   = createMethodConnector(ClsA.class, "getOneA", null, "foo");
		final IndividualMethodConnector mc_a1a_2 = createMethodConnector(ClsA.class, "getOneA", null, "foo");
		
		assert mc_a1a.equals(mc_a1a);
		assert mc_a1a.equals(mc_a1a_2);
		
		assert mc_a1a.hashCode() == mc_a1a_2.hashCode();
		
		final IndividualMethodConnector mc_a1a_noargs   = createMethodConnector(ClsA.class, "getOneA", null);
		final IndividualMethodConnector mc_a1a_argsDiff = createMethodConnector(ClsA.class, "getOneA", null, "bar");
		final IndividualMethodConnector mc_b1a_args     = createMethodConnector(ClsB.class, "getOneA", null, "foo");		
		
		assert ! mc_a1a.equals(mc_a1a_noargs);
		assert ! mc_a1a.equals(mc_a1a_argsDiff);
		assert ! mc_a1a.equals(mc_b1a_args);
	}
	
	@Test
	public void testIndividualReadWriteMethodConnectorNoArgsEquality() {
		final IndividualMethodConnector mc_a1a   = createMethodConnector(ClsA.class, "getOneA", "setOneA");
		final IndividualMethodConnector mc_a1a_2 = createMethodConnector(ClsA.class, "getOneA", "setOneA");
		
		assert mc_a1a.equals(mc_a1a);
		assert mc_a1a.equals(mc_a1a_2);
		
		assert mc_a1a.hashCode() == mc_a1a_2.hashCode();
		
		final IndividualMethodConnector mc_a1a_readonly   = createMethodConnector(ClsA.class, "getOneA", null);
		final IndividualMethodConnector mc_a1a_getterDiff = createMethodConnector(ClsA.class, "getOneB", "setOneA");
		final IndividualMethodConnector mc_a1a_setterDiff = createMethodConnector(ClsA.class, "getOneA", "setOneB");
		final IndividualMethodConnector mc_b1a_readWrite  = createMethodConnector(ClsB.class, "getOneA", "setOneA");
		
		assert ! mc_a1a.equals(mc_a1a_readonly);
		assert ! mc_a1a.equals(mc_a1a_getterDiff);
		assert ! mc_a1a.equals(mc_a1a_setterDiff);
		assert ! mc_a1a.equals(mc_b1a_readWrite);
	}
	
	@Test
	public void testIndividualReadWriteMethodConnectorWithArgsEquality() {
		final IndividualMethodConnector mc_a1a   = createMethodConnector(ClsA.class, "getOneA", "setOneA", "foo");
		final IndividualMethodConnector mc_a1a_2 = createMethodConnector(ClsA.class, "getOneA", "setOneA", "foo");
		
		assert mc_a1a.equals(mc_a1a);
		assert mc_a1a.equals(mc_a1a_2);
		
		assert mc_a1a.hashCode() == mc_a1a_2.hashCode();
		
		final IndividualMethodConnector mc_a1a_noargs   = createMethodConnector(ClsA.class, "getOneA", "setOneA");
		final IndividualMethodConnector mc_a1a_argsDiff = createMethodConnector(ClsA.class, "getOneA", "setOneA", "bar");
		final IndividualMethodConnector mc_b1a_args     = createMethodConnector(ClsB.class, "getOneA", "setOneA", "foo");		
		
		assert ! mc_a1a.equals(mc_a1a_noargs);
		assert ! mc_a1a.equals(mc_a1a_argsDiff);
		assert ! mc_a1a.equals(mc_b1a_args);
	}
	
	@Test
	public void testToStringConnectorEquality() {
		final ToStringConnector tc_a   = new ToStringConnector(ClsA.class);
		final ToStringConnector tc_a_2 = new ToStringConnector(ClsA.class);

		assert tc_a.equals(tc_a);
		assert tc_a.equals(tc_a_2);
		
		assert tc_a.hashCode() == tc_a_2.hashCode();
		
		final ToStringConnector tc_b   = new ToStringConnector(ClsB.class);
		
		assert ! tc_a.equals(tc_b);
	}
	
	@Test
	public void testIndividualDataContainerConnectorEquality() {
		final IndividualDataContainerConnector dcc_a   = new IndividualDataContainerConnector(String.class);
		final IndividualDataContainerConnector dcc_a_2 = new IndividualDataContainerConnector(String.class);
		
		assert dcc_a.equals(dcc_a);
		assert dcc_a.equals(dcc_a_2);
		
		assert dcc_a.hashCode() == dcc_a_2.hashCode();
		
		final IndividualDataContainerConnector dcc_b   = new IndividualDataContainerConnector(Integer.class);
		
		assert ! dcc_a.equals(dcc_b);
	}
	
	
	// ------------------------------------------------------------------------
	
	
	private IndividualFieldConnector createFieldConnector(Class<?> cls, String fieldName) {
		return new IndividualFieldConnector(ClsA.class, field(cls, fieldName));
	}
	
	private IndividualMethodConnector createMethodConnector(Class<?> cls, String getterName, String setterName) {
		final Method getter = method(cls, getterName, new Class[0]);
		final Method setter = method(cls, setterName, new Class[] {String.class} );
		return new IndividualMethodConnector(ClsA.class, getter, setter);
	}
	
	private IndividualMethodConnector createMethodConnector(Class<?> cls, String getterName, String setterName, Object... argumentValues) {
		final Method getter = method(cls, getterName, new Class[] {String.class});
		final Method setter = method(cls, setterName, new Class[] {String.class, String.class} );
		return new IndividualMethodConnector(ClsA.class, getter, setter, argumentValues);
	}
	
	
	// ------------------------------------------------------------------------
	
	
	public static class ClsA {
		public String f1a;
		public String f1b;
		
		public String getOneA() { return null; }
		public String getOneB() { return null; }
		
		public void setOneA(String s) { }
		public void setOneB(String s) { }
		
		public String getOneA(String s) { return null; }
		public String getOneB(String s) { return null; }
		
		public void setOneA(String s1, String s2) { }
	}
	
	public static class ClsB {
		public String f1a;
		public String f1b;
		
		public String getOneA() { return null; }
		public String getOneB() { return null; }
		
		public void setOneA(String s) { }
		public void setOneB(String s) { }
		
		public String getOneA(String s) { return null; }
		public String getOneB(String s) { return null; }
		
		public void setOneA(String s1, String s2) { }
	}
	
}
