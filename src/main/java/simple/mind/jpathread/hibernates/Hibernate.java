package simple.mind.jpathread.hibernates;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import simple.mind.jpathread.HibernateThreadApp;
import simple.mind.jpathread.configs.Config;
import simple.mind.jpathread.model.Comments;

public class Hibernate {
	Config conf = new Config();

	private StandardServiceRegistry registry;
	private SessionFactory sessionFactory;

	public Hibernate() {
		try {
			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

			Map<String, Object> settings = new HashMap<>();
			settings.put(Environment.DRIVER, conf.getDriver());
			settings.put(Environment.URL, conf.getUrl());
			settings.put(Environment.USER, conf.getUser());
			settings.put(Environment.PASS, conf.getPass());
			settings.put(Environment.HBM2DDL_AUTO, conf.getAutoDdl());
			settings.put(Environment.SHOW_SQL, conf.showSQL());

			// HikariCP settings
			settings.put("hibernate.hikari.connectionTimeout", "20000");
			settings.put("hibernate.hikari.minimumIdle", "10");
			settings.put("hibernate.hikari.maximumPoolSize", HibernateThreadApp.THREAD_COUNT.toString());
			settings.put("hibernate.hikari.idleTimeout", "300000");

			registryBuilder.applySettings(settings);

			registry = registryBuilder.build();
			MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Comments.class);
			Metadata metadata = sources.getMetadataBuilder().build();
			sessionFactory = metadata.getSessionFactoryBuilder().build();
		} catch (Exception e) {
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		StandardServiceRegistryBuilder.destroy(registry);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
