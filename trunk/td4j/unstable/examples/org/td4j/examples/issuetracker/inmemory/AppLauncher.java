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

package org.td4j.examples.issuetracker.inmemory;

import org.td4j.core.tk.env.SvcRepository;
import org.td4j.swing.workbench.AppCtx;
import org.td4j.swing.workbench.Workbench;

public class AppLauncher {
	
	public static void main(String[] args) {
		final SvcRepository svcRepo = new SvcRepository();
		
		final Setup setup = new Setup();
		final Object initialNavigation = setup.run(svcRepo);		
		
		final AppCtx ctx = new AppCtx();
		ctx.setSidebarEntries(IssueContainer.class, IssueTemplate.class);
		ctx.setSvcProvider(svcRepo);		
		ctx.setInitialNavigation(initialNavigation);		
		
		Workbench.start(ctx);
	}
	
	
	private static class Setup {
		
		private Object run(SvcRepository repo) {
			
			initCompanions(repo);
			
//			final IssueTemplate template = new IssueTemplate();
//			template.setName("foo");
			
			// TODO return initial navigation -> TagGroupsCO.findAll
			return null;
		}		
		
		private void initCompanions(SvcRepository svcRepo) {
			final EntityRepo entityRepo = new EntityRepo();
			
			svcRepo.setSingletonService(FluentMasterDataRepo.class, new FluentMasterDataRepo(new MasterDataFactory(entityRepo)));
			svcRepo.setSingletonService(IssueRepo.class,            new IssueRepo(entityRepo));
		}		
	}
	
}
