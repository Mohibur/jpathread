package simple.mind.jpathread.hibernates;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class RepoBase<T> {
	HibernateConfiguration hibernateUtil;
	Session session;

	public RepoBase(HibernateConfiguration hibernateUtil) {
		this.hibernateUtil = hibernateUtil;
		session = hibernateUtil.getSessionFactory().openSession();
	}

	public T Save(T input) {
		Transaction transaction = null;
		try {
			session = hibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.save(input);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return input;
	}

	public List<T> SaveAll(List<T> input) {
		Transaction transaction = null;
		try {
			session = hibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			//transaction.begin();
			for (T t : input) {
				session.save(t);
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return input;
	}
}
