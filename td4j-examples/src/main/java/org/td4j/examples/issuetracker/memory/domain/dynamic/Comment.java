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

package org.td4j.examples.issuetracker.memory.domain.dynamic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.td4j.core.reflect.ShowPropertiesInEditorList;

import ch.miranet.commons.ObjectTK;

@ShowPropertiesInEditorList({ "description", "commentedAt", "withLink" })
public class Comment {
	
	private Issue issue;
	private String description;	
	private Date commentedAt;
	private URL link;
	
	@SuppressWarnings("unused")
	private Comment() {}
	
	Comment(Issue issue, String description) {
		setIssue(issue);
		setDescription(description);
		setCommentedAt(new Date());
	}
	
	public  Issue getIssue()            { return issue; }
	private void  setIssue(Issue issue) { this.issue = ObjectTK.enforceNotNull(issue, "issue"); } 
	
	public String getDescription()                   { return description; }
	public void   setDescription(String description) { this.description = description; }

	public  Date getCommentedAt()                 { return commentedAt; }
	private void setCommentedAt(Date commentedAt) { this.commentedAt = commentedAt; }
	
	public  URL  getLink()         { return link; }
	private void setLink(URL link) { this.link = link; }
	
	public String getLinkURL()                                        { return link != null ? link.toString() : null; }
	public void   setLinkURL(String url) throws MalformedURLException { setLink(new URL(url)); }
	
	public boolean isWithLink() { return link != null; }
	
	@Override
	public String toString() { return description; }	

}
