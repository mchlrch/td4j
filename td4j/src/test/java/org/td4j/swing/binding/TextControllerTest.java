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

package org.td4j.swing.binding;

import java.lang.reflect.Method;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.internal.binding.model.IndividualMethodConnector;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.miranet.commons.TK;

public class TextControllerTest {
	
  // connector is stateless, so we don't need to recreate it for every testcase 
	private IndividualMethodConnector dataConnector;

	
	
	@Test(dataProvider = "harness")
	public void testShowInitialModelValueInWidget(final TestHarness<?> harness) {
		assertTextFieldContent(DataContainer.INITIAL_VALUE, harness);
	}
	
	@Test(dataProvider = "harness")
	public void testShowChangedModelValueInWidget(final TestHarness<?> harness) {
		harness.dataContainer.setValue(DataContainer.ACCEPTED_VALUE);
		harness.dataProxy.refreshFromContext();
		
		assertTextFieldContent(DataContainer.ACCEPTED_VALUE, harness);
	}	
	
	@Test(dataProvider = "harness")
	public void testWriteWidgetValueToModel(final TestHarness<?> harness) {
		harness.textComponent.setText(DataContainer.ACCEPTED_VALUE);
		harness.textController.doUpdateModel();
		
		assertTextFieldContent(DataContainer.ACCEPTED_VALUE, harness);
		assertDataContainerContent(DataContainer.ACCEPTED_VALUE, harness);
	}
	
	@Test(dataProvider = "harness", expectedExceptions = { IllegalArgumentException.class })
	public void testWriteRejectedWidgetValueToModelAndRecover(final TestHarness<?> harness) throws Throwable {
		harness.textComponent.setText(DataContainer.REJECTED_VALUE);
		
		try {
			harness.textController.doUpdateModel();
		} catch (Exception ex) {			
			assertTextFieldContent(DataContainer.INITIAL_VALUE, harness);
			assertDataContainerContent(DataContainer.INITIAL_VALUE, harness);
			
			throw TK.Exceptions.unwrap(ex);
		}		
	}

	private void assertTextFieldContent(final String expectedContent, final TestHarness<?> harness) {
		assert harness.textComponent.getText().equals(expectedContent);
	}
	
	private void assertDataContainerContent(final String expectedContent, final TestHarness<?> harness) {
		assert harness.dataContainer.getValue().equals(expectedContent);
	}
	
	
	@DataProvider(name = "harness")
	public Object[][] createHarness() {		
		if (this.dataConnector == null) {
			this.dataConnector = createDataConnector();
		}			
		
		final TestHarness<JTextField> harnessTF = createHarnessForJTextField(createDataProxy());
		final TestHarness<JTextArea>  harnessTA = createHarnessForJTextArea(createDataProxy());
		
		return new Object[][] { { harnessTF }, { harnessTA } };
	}
	
	private IndividualDataProxy createDataProxy() {
		final IndividualDataProxy dataProxy = new IndividualDataProxy(this.dataConnector, "value");
		final DataContainer dataContainer = new DataContainer(); 
		dataProxy.setContext(dataContainer);
		return dataProxy;		
	}
	
	private TestHarness<JTextField> createHarnessForJTextField(IndividualDataProxy dataProxy) {
		final JTextField textField = new JTextField();
		final TestTextController<JTextField> textController = new TestTextController<JTextField>(textField, dataProxy);		
		final TestHarness<JTextField> harness = new TestHarness<JTextField>( (DataContainer)dataProxy.getContext(), dataProxy, textController, textField);
		return harness;
	}
	
	private TestHarness<JTextArea> createHarnessForJTextArea(IndividualDataProxy dataProxy) {
		final JTextArea textArea = new JTextArea();
		final TestTextController<JTextArea> textController = new TestTextController<JTextArea>(textArea, dataProxy);		
		final TestHarness<JTextArea> harness = new TestHarness<JTextArea>( (DataContainer)dataProxy.getContext(), dataProxy, textController, textArea);
		return harness;
	}
	
	private IndividualMethodConnector createDataConnector() {
		try {
			final Method getter = DataContainer.class.getMethod("getValue");
			final Method setter = DataContainer.class.getMethod("setValue", String.class);
			
			final IndividualMethodConnector dataConnector = new IndividualMethodConnector(DataContainer.class, getter, setter);
			return dataConnector;
			
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	// -------------------------------------------
	
	public static class TestHarness<T extends JTextComponent> {
		public final DataContainer         dataContainer;
		public final IndividualDataProxy   dataProxy;
		public final TestTextController<T> textController;
		public final T                     textComponent;
		
		private TestHarness(DataContainer dataContainer, IndividualDataProxy dataProxy, TestTextController<T> textController, T textComponent) {
			this.dataContainer  = TK.Objects.assertNotNull(dataContainer,  "dataContainer");
			this.dataProxy      = TK.Objects.assertNotNull(dataProxy,      "dataProxy");
			this.textController = TK.Objects.assertNotNull(textController, "textController");
			this.textComponent  = TK.Objects.assertNotNull(textComponent,  "textComponent");
		}
	}
	
	public static class DataContainer {
		public static final String INITIAL_VALUE  = "initial";
		public static final String ACCEPTED_VALUE = "accept";
		public static final String REJECTED_VALUE = "reject";
		
		private String value = INITIAL_VALUE;
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String s)  {
			if (REJECTED_VALUE.equals(s)) throw new IllegalArgumentException(REJECTED_VALUE);
			
			this.value = s;
		}
	}
	
	public static class TestTextController<T extends JTextComponent> extends TextController<T> {
		private TestTextController(T widget, IndividualDataProxy proxy) {
			super(widget, proxy);
		}
		
		// open access
		public void doUpdateModel() {
			super.updateModel();
		}
	}

}
