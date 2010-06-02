package org.td4j.examples.tagger;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Resource {

	@Id @GeneratedValue private Integer id;
	
	public String description;
	
	@OneToMany(mappedBy="resource", cascade=CascadeType.ALL)
	public List<ResourceTag> tags = new ArrayList<ResourceTag>();

	private Resource() {}
	
	@Override
	public String toString() {
		return description;
	}

}
