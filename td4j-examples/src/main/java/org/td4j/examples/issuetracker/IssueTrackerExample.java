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

package org.td4j.examples.issuetracker;

import org.td4j.examples.issuetracker.domain.dynamic.DynamicDataFactory;
import org.td4j.examples.issuetracker.domain.dynamic.DynamicDataRepository;
import org.td4j.examples.issuetracker.domain.dynamic.IssueContainer;
import org.td4j.examples.issuetracker.domain.master.IssueContainerTemplate;
import org.td4j.examples.issuetracker.domain.master.MasterDataFactory;
import org.td4j.examples.issuetracker.domain.master.MasterDataRepository;
import org.td4j.swing.workbench.AppCtx;
import org.td4j.swing.workbench.Workbench;

import ch.miranet.commons.service.SvcRepository;

public class IssueTrackerExample {
	
	public static void main(String[] args) {
		final SvcRepository svcRepo = new SvcRepository();
		
		final Setup setup = new Setup();
		final Object initialNavigation = setup.run(svcRepo);		
		
		final AppCtx ctx = new AppCtx();
		ctx.setSidebarEntries(IssueContainer.class, IssueContainerTemplate.class);
		ctx.setSvcProvider(svcRepo);		
		ctx.setInitialNavigation(initialNavigation);		
		
		Workbench.start(ctx);
	}
	
	
	private static class Setup {
		
		private Object run(SvcRepository repo) {			
			initCompanions(repo);
			final IssueContainerTemplate template = createTemplate(repo);
			return template;
		}		

		private void initCompanions(SvcRepository svcRepo) {
			final EntityRepo entityRepo = new EntityRepo();
			
			final MasterDataFactory masterFactory   = new MasterDataFactory(entityRepo);
			final DynamicDataFactory dynamicFactory = new DynamicDataFactory(entityRepo);
			
			final MasterDataRepository masterRepo = new MasterDataRepository(entityRepo);
			final DynamicDataRepository dynamicRepo = new DynamicDataRepository(entityRepo);
			
			svcRepo.setSingletonService(FluentMasterDataFactory.class, new FluentMasterDataFactory(masterFactory, dynamicFactory));
			svcRepo.setSingletonService(DynamicDataFactory.class, dynamicFactory);
			
			svcRepo.setSingletonService(MasterDataRepository.class, masterRepo);
			svcRepo.setSingletonService(DynamicDataRepository.class, dynamicRepo);			
		}		
	
		private IssueContainerTemplate createTemplate(SvcRepository repo) {
			final FluentMasterDataFactory mf = repo.getService(FluentMasterDataFactory.class);
			
			final IssueContainerTemplate template = mf.createTemplate("project");
			
			mf.createSeverity(template, "low");
			mf.createSeverity(template, "medium");
			mf.createSeverity(template, "high");
			
			mf.createStatus(template, "new",      false);
			mf.createStatus(template, "open",     false);
			mf.createStatus(template, "deferred", true);		
			mf.createStatus(template, "fixed",    true);
				
			return template;
		}
	}
	
}
