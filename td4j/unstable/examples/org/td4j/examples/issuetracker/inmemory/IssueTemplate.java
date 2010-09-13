package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.core.tk.ObjectTK;

@Companions(FluentMasterDataRepo.class)
public class IssueTemplate extends NamedElement {
	
	private List<Severity> severities = new ArrayList<Severity>();
	private List<Status>   stati      = new ArrayList<Status>();
	private List<Module>   modules    = new ArrayList<Module>();
	private List<IssueContainer>  derivates  = new ArrayList<IssueContainer>();
	
	@ShowProperties("name")	public List<Severity> getSeverities() { return severities;}
	@ShowProperties("name") public List<Status>   getStati()      { return stati; }
	@ShowProperties("name") public List<Module>   getModules()    { return modules;}
	@ShowProperties("name") public List<IssueContainer>  getDerivates()  { return derivates;}
	
	void addSeverity(Severity severity)        { severities.add(ObjectTK.enforceNotNull(severity, "severity")); }
	void addStatus(Status status)              { stati.add(ObjectTK.enforceNotNull(status, "status")); }
	void addModule(Module module)              { modules.add(ObjectTK.enforceNotNull(module, "module")); }
	void addDerivate(IssueContainer container) { derivates.add(ObjectTK.enforceNotNull(container, "ctx")); }

}
