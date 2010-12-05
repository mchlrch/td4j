package org.td4j.examples.issuetracker.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;

@Companions(MasterDataRepo.class)
@Entity
public class IssueTemplate extends NamedElement {
	
	@OneToMany(mappedBy="template", cascade=CascadeType.ALL)
	private List<Severity> severities;
	
	@OneToMany(mappedBy="template", cascade=CascadeType.ALL)
	private List<Status> stati;
	
	@OneToMany(mappedBy="template", cascade=CascadeType.ALL)
	private List<Module> modules;
	
	@ShowProperties("name")	public List<Severity> getSeverities() { return severities;}
	@ShowProperties("name") public List<Status> getStati()        { return stati; }
	@ShowProperties("name") public List<Module> getModules()      { return modules;}

}
