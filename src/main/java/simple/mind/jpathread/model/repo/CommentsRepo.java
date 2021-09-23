package simple.mind.jpathread.model.repo;

import simple.mind.jpathread.hibernates.HibernateConfiguration;
import simple.mind.jpathread.hibernates.RepoBase;
import simple.mind.jpathread.model.Comments;

public class CommentsRepo extends RepoBase<Comments> {
	public CommentsRepo(HibernateConfiguration hu) {
		super(hu);
	}
}
