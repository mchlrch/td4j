package org.td4j.core.internal.binding.model;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.annotations.Test;

public class ListDataConnectorTest extends BaseDataConnectorTest {
	
	@Test
	public void testArrayFieldConnectorEquality() {
		final ArrayFieldConnector afc_a1a   = createArrayFieldConnector(ClsA.class, "f1a");
		final ArrayFieldConnector afc_a1a_2 = createArrayFieldConnector(ClsA.class, "f1a");
		
		assert afc_a1a.equals(afc_a1a);
		assert afc_a1a.equals(afc_a1a_2);
		
		assert afc_a1a.hashCode() == afc_a1a_2.hashCode();
		
		final ArrayFieldConnector afc_a1b = createArrayFieldConnector(ClsA.class, "f1b");
		final ArrayFieldConnector afc_b1a = createArrayFieldConnector(ClsB.class, "f1a");		
		
		assert ! afc_a1a.equals(afc_a1b);
		assert ! afc_a1a.equals(afc_b1a);		
	}
	
	@Test
	public void testListFieldConnectorEquality() {
		final ListFieldConnector lfc_a2a   = createListFieldConnector(ClsA.class, "f2a");
		final ListFieldConnector lfc_a2a_2 = createListFieldConnector(ClsA.class, "f2a");
		
		assert lfc_a2a.equals(lfc_a2a);
		assert lfc_a2a.equals(lfc_a2a_2);
		
		assert lfc_a2a.hashCode() == lfc_a2a_2.hashCode(); 
		
		final ListFieldConnector lfc_a2b = createListFieldConnector(ClsA.class, "f2b");
		final ListFieldConnector lfc_b2a = createListFieldConnector(ClsB.class, "f2a");
		
		assert ! lfc_a2a.equals(lfc_a2b);
		assert ! lfc_a2a.equals(lfc_b2a);
	}
	
	@Test
	public void testArrayMethodConnectorNoArgsEquality() {
		final ArrayMethodConnector amc_a1a   = createArrayMethodConnector(ClsA.class, "getOneA");
		final ArrayMethodConnector amc_a1a_2 = createArrayMethodConnector(ClsA.class, "getOneA");
		
		assert amc_a1a.equals(amc_a1a);
		assert amc_a1a.equals(amc_a1a_2);
		
		assert amc_a1a.hashCode() == amc_a1a_2.hashCode();
		
		final ArrayMethodConnector amc_a1b = createArrayMethodConnector(ClsA.class, "getOneB");
		final ArrayMethodConnector amc_b1a = createArrayMethodConnector(ClsB.class, "getOneA");
		
		assert ! amc_a1a.equals(amc_a1b);
		assert ! amc_a1a.equals(amc_b1a);
	}
	
	@Test
	public void testArrayMethodConnectorWithArgsEquality() {
		final ArrayMethodConnector amc_a1a_args   = createArrayMethodConnector(ClsA.class, "getOneA", "foo");
		final ArrayMethodConnector amc_a1a_args_2 = createArrayMethodConnector(ClsA.class, "getOneA", "foo");
		
		assert amc_a1a_args.equals(amc_a1a_args);
		assert amc_a1a_args.equals(amc_a1a_args_2);
		
		assert amc_a1a_args.hashCode() == amc_a1a_args_2.hashCode();
		
		final ArrayMethodConnector amc_a1a_noargs   = createArrayMethodConnector(ClsA.class, "getOneA");
		final ArrayMethodConnector amc_a1a_argsDiff = createArrayMethodConnector(ClsA.class, "getOneA", "bar");
		final ArrayMethodConnector amc_b1a_args     = createArrayMethodConnector(ClsB.class, "getOneA", "foo");
		
		assert ! amc_a1a_args.equals(amc_a1a_noargs);
		assert ! amc_a1a_args.equals(amc_a1a_argsDiff);
		assert ! amc_a1a_args.equals(amc_b1a_args);
	}
	
	@Test
	public void testListMethodConnectorNoArgsEquality() {
		final ListMethodConnector lmc_a2a   = createListMethodConnector(ClsA.class, "getTwoA");
		final ListMethodConnector lmc_a2a_2 = createListMethodConnector(ClsA.class, "getTwoA");
		
		assert lmc_a2a.equals(lmc_a2a);
		assert lmc_a2a.equals(lmc_a2a_2);
		assert lmc_a2a.hashCode() == lmc_a2a_2.hashCode();
		
		final ListMethodConnector lmc_a2b = createListMethodConnector(ClsA.class, "getTwoB");
		final ListMethodConnector lmc_b2a = createListMethodConnector(ClsB.class, "getTwoA");
		
		assert ! lmc_a2a.equals(lmc_a2b);
		assert ! lmc_a2a.equals(lmc_b2a);
	}
	
	@Test
	public void testListMethodConnectorWithArgsEquality() {
		final ListMethodConnector lmc_a1a_args   = createListMethodConnector(ClsA.class, "getTwoA", "foo");
		final ListMethodConnector lmc_a1a_args_2 = createListMethodConnector(ClsA.class, "getTwoA", "foo");
		
		assert lmc_a1a_args.equals(lmc_a1a_args);
		assert lmc_a1a_args.equals(lmc_a1a_args_2);
		
		assert lmc_a1a_args.hashCode() == lmc_a1a_args_2.hashCode();
		
		final ListMethodConnector lmc_a1a_noargs   = createListMethodConnector(ClsA.class, "getTwoA");
		final ListMethodConnector lmc_a1a_argsDiff = createListMethodConnector(ClsA.class, "getTwoA", "bar");
		final ListMethodConnector lmc_b1a_args     = createListMethodConnector(ClsB.class, "getTwoA", "foo");
		
		assert ! lmc_a1a_args.equals(lmc_a1a_noargs);
		assert ! lmc_a1a_args.equals(lmc_a1a_argsDiff);
		assert ! lmc_a1a_args.equals(lmc_b1a_args);
	}
	
	@Test
	public void testListDataContainerConnectorEquality() {
		final ListDataContainerConnector ldcc_a   = new ListDataContainerConnector(String.class);
		final ListDataContainerConnector ldcc_a_2 = new ListDataContainerConnector(String.class);
		
		assert ldcc_a.equals(ldcc_a);
		assert ldcc_a.equals(ldcc_a_2);
		
		assert ldcc_a.hashCode() == ldcc_a_2.hashCode();
		
		final ListDataContainerConnector ldcc_b   = new ListDataContainerConnector(Integer.class);
		
		assert ! ldcc_a.equals(ldcc_b);		
	}
	
	
	// ------------------------------------------------------------------------
	
	
	private ArrayFieldConnector createArrayFieldConnector(Class<?> cls, String fieldName) {
		return new ArrayFieldConnector(ClsA.class, field(cls, fieldName), String.class);
	}
	
	private ListFieldConnector createListFieldConnector(Class<?> cls, String fieldName) {
		return new ListFieldConnector(ClsA.class, field(cls, fieldName), String.class);
	}
	
	private ArrayMethodConnector createArrayMethodConnector(Class<?> cls, String getterName) {
		final Method getter = method(cls, getterName, new Class[0]);
		return new ArrayMethodConnector(cls, getter, String.class);
	}
	
	private ArrayMethodConnector createArrayMethodConnector(Class<?> cls, String getterName, Object... argumentValues) {
		final Method getter = method(cls, getterName, new Class[] {String.class});
		return new ArrayMethodConnector(cls, getter, String.class, argumentValues);
	}
	
	private ListMethodConnector createListMethodConnector(Class<?> cls, String getterName) {
		final Method getter = method(cls, getterName, new Class[0]);
		return new ListMethodConnector(cls, getter, String.class);
	}
	
	private ListMethodConnector createListMethodConnector(Class<?> cls, String getterName, Object... argumentValues) {
		final Method getter = method(cls, getterName, new Class[] {String.class});
		return new ListMethodConnector(cls, getter, String.class, argumentValues);
	}
		
	
	// ------------------------------------------------------------------------
	
	
	public static class ClsA {
		public String[] f1a;
		public String[] f1b;
		
		public List<String> f2a;
		public List<String> f2b;
		
		public String[] getOneA() { return null; }
		public String[] getOneB() { return null; }
		
		public String[] getOneA(String s) { return null; }
		public String[] getOneB(String s) { return null; }
		
		public List<String> getTwoA() { return null; }
		public List<String> getTwoB() { return null; }
		
		public List<String> getTwoA(String s) { return null; }
		public List<String> getTwoB(String s) { return null; }
	}
	
	public static class ClsB {
		public String[] f1a;
		public String[] f1b;
		
		public List<String> f2a;
		public List<String> f2b;
		
		public String[] getOneA() { return null; }
		public String[] getOneB() { return null; }
		
		public String[] getOneA(String s) { return null; }
		public String[] getOneB(String s) { return null; }
		
		public List<String> getTwoA() { return null; }
		public List<String> getTwoB() { return null; }
		
		public List<String> getTwoA(String s) { return null; }
		public List<String> getTwoB(String s) { return null; }
	}
	
}
