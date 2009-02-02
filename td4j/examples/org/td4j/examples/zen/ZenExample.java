package org.td4j.examples.zen;

import javax.swing.SwingUtilities;

import org.td4j.swing.workbench.Workbench;


public class ZenExample {

	public static void main(String[] args) {
		Workbench.start(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Workbench wb = Workbench.getInstance();
				wb.seek(wb);
			}
		});
	}
	
}
