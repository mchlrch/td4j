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

package org.td4j.examples.dispatcher;

import java.util.Date;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.Operation;
import org.td4j.core.tk.env.SvcRepository;
import org.td4j.swing.workbench.AppCtx;
import org.td4j.swing.workbench.Workbench;

/**
 * This example shows a usage scenario for companion objects.
 * For prototyping purposes, we want to invoke the EmailDispatcher.dispatch()
 * method with a String parameter. We implement this behaviour in DispatcherTestCO,
 * therefore keeping EmailDispatcher's interface clean.
 */
public class DispatcherExample {

	public static void main(String[] args) {
		final SvcRepository svcRepo = new SvcRepository();
		svcRepo.setSingletonService(DispatcherTestCO.class, new DispatcherTestCO());
		
		final EmailDispatcher dispatcher = new EmailDispatcher("me@domain.org");
		
		final AppCtx ctx = new AppCtx();
		ctx.setSidebarEntries(EmailDispatcher.class);
		ctx.setSvcProvider(svcRepo);		
		ctx.setInitialNavigation(dispatcher);		
		
		Workbench.start(ctx);
	}


	// --------------------------------------	
	
	@Companions(DispatcherTestCO.class)
	public static class EmailDispatcher {
		
		public String address;
		
		public EmailDispatcher(String address) {
			this.address = address;
		}
				
		public DispatchLog dispatch(Message msg) {
			System.out.println("Dispatch email to: " + address);
			System.out.println("  >> " + msg);
			
			return new DispatchLog(address, msg);
		}
	}	
	
  //--------------------------------------
	
	public static class Message {
		public String content;
		
		public Message(String content) {
			this.content = content;
		}
		
		@Override
		public String toString() {
			return content;
		}
	}
	
  //--------------------------------------
	
	public static class DispatchLog {
		private String target;
		private Date timestamp;
		private Message message;
		
		DispatchLog(String target, Message msg) {
			this.target = target;
			this.timestamp = new Date();
			this.message = msg;
		}
		
		public String getTarget()   { return target; }		
		public Date getTimestamp()  { return timestamp; }		
		public Message getMessage() { return message; }
		
	}
	
	// ----------------------------------------
	
	public static class DispatcherTestCO {
		
	  @Operation
		public DispatchLog dispatch(EmailDispatcher dispatcher, String s) {
			return dispatcher.dispatch(new Message(s));
		}
	}
	
}
