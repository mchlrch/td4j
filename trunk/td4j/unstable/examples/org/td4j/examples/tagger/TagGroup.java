package org.td4j.examples.tagger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.td4j.core.reflect.Companions;

@Entity
@Companions(TagGroupCO.class)
public class TagGroup {
	
	@Id @GeneratedValue private Integer id;
	
	@Column(unique=true, nullable=false)
	public String name;
	
	private TagGroup() {}
	
	TagGroup(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
