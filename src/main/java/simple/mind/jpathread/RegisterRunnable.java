package simple.mind.jpathread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.mind.jpathread.model.Comments;
import simple.mind.jpathread.model.repo.CommentsRepo;

public class RegisterRunnable implements Runnable {
	private Logger logger = LoggerFactory.getLogger(RegisterRunnable.class);

	CommentsRepo repo;
	HibernateThreadApp xml;

	public RegisterRunnable(HibernateThreadApp fXml, CommentsRepo fRepo) {
		xml = fXml;
		repo = fRepo;
	}

	@Override
	public void run() {
		while (true) {
			List<Comments> l = xml.pop();
			if (l.size() == 0 && !xml.hasMoreData()) {
				logger.info("Leaving this thread");
				break;
			} else if (l.size() > 0) {
				logger.info("Saving Started");
				repo.SaveAll(l);
				logger.info("Saving Finished");

			} else {

				HibernateThreadApp.sleep(1);
			}
		}

	}

}
