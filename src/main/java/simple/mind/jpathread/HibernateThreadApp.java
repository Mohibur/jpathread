package simple.mind.jpathread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import simple.mind.jpathread.hibernates.Hibernate;
import simple.mind.jpathread.model.Comments;
import simple.mind.jpathread.model.repo.CommentsRepo;

public class HibernateThreadApp {

	private static final Integer MAX_QUERY_PER_REQUEST = 1000;
	public static final Integer THREAD_COUNT = 250;
	private BlockingQueue<List<Comments>> queue;
	private Boolean moreData = true;
	private static final Integer MAX_QUEUE_SIZE = 100000;

	Hibernate hibernateUtil = null;

	public static void main(String[] args) {
		new HibernateThreadApp().run(args);
	}

	public void run(String... args) {
		hibernateUtil = new Hibernate();
		if (args.length == 0) {
			throw new RuntimeException("Commend Line takes file name");
		}
		start1(args[0]);
	}

	private BufferedReader bufferEx(String fileName) throws FileNotFoundException {
		final String xmlFile = "";
		BufferedReader bis = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(xmlFile)), StandardCharsets.UTF_8));
		return bis;
	}

	private BufferedReader buffer(String fileName) {
		try {
			return bufferEx(fileName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void start1(String fileName) {
		try {
			start1Ex(fileName);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	private void start1Ex(String fileName) throws Exception {
		queue = new LinkedBlockingQueue<List<Comments>>();
		BufferedReader bis = buffer(fileName);
		List<Thread> threadList = popThreads();

		String line;
		List<Comments> lc = new ArrayList<Comments>();
		while ((line = bis.readLine()) != null) {
			if (line.matches("^ *<row .+$")) {
				line = line.trim().replace("<row ", "").replace(" />", "").trim();
				lc.add(Parse.parseString(line));
				if (lc.size() == MAX_QUERY_PER_REQUEST) {
					queue.add(lc);
					lc = new ArrayList<Comments>();
					pause();
				}
			}
		}

		if (lc.size() != 0) {
			queue.add(lc);
			lc = new ArrayList<Comments>();
		}

		moreData = false;

		threadList.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.err.println("End of Action Services");
	}

	private void pause() {
		boolean f = false;
		while (queue.size() > MAX_QUEUE_SIZE) {
			f = true;
			System.out.println("Sleeping after fat-queue: " + queue.size());
			sleep(2);
		}
		if (f) {
			System.out.println("Wakingup reduced queue");
		}
	}

	private List<Thread> popThreads() {
		List<Thread> ret = new ArrayList<Thread>();
		IntStream.range(0, THREAD_COUNT).forEach(item -> {
			CommentsRepo repo = new CommentsRepo(hibernateUtil);
			Runnable register = new RegisterRunnable(this, repo);
			Thread t = new Thread(register);
			t.start();
			ret.add(t);
		});
		return ret;
	}

	public Boolean hasMoreData() {
		return moreData;
	}

	public List<Comments> pop() {
		List<Comments> p = queue.poll();

		return p == null ? new ArrayList<Comments>() : p;
	}

	public static void sleep(Integer seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
