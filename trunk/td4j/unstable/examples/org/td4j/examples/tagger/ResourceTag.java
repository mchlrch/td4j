package org.td4j.examples.tagger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ResourceTag {
	
	@Id @GeneratedValue private Integer id;
	
	public Resource resource;
	public Tag      tag;

	private ResourceTag() {}
	
	@Override
	public String toString() {
		return "" + resource + " <" + tag + ">";
	}
	
}
