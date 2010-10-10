package org.td4j.examples.tagger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag {

	@Id @GeneratedValue private Integer id;
	
	public String    name;
	public TagGroup  group;
	
	private Tag() {}
	
	Tag(TagGroup group, String name) {
		this.group = group;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
