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

package org.td4j.examples.issuetracker.domain.dynamic;

import org.td4j.core.reflect.Operation;
import org.td4j.examples.issuetracker.EntityRepo;

import ch.miranet.commons.ObjectTK;

public class CommentFactory {

	private final EntityRepo repo;
	
	public CommentFactory(EntityRepo repo) {
		this.repo = ObjectTK.enforceNotNull(repo, "repo");
	}
	
	@Operation
	public Comment createComment(Issue issue, String description) {
		final Comment comment = new Comment(issue, description);
		
		issue.addComment(comment);
		
		repo.put(Comment.class, comment);
		return comment;
	}
	
}
