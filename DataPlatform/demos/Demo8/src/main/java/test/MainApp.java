package test;

import java.util.*;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;

import pt.ist.fenixframework.FenixFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static final int BOOK_COUNT = 250;

    public static void main(String[] args) throws Exception {
        try {
            initDomain();
            logger.debug("Population completed for " + BOOK_COUNT + " books");
	    FenixFramework.barrier("start", Integer.parseInt(args[1]));
            long steps = 0L;
            long start = System.nanoTime();
            while (true) {
                doRead("random");
		steps++;
                if (((System.nanoTime() - start)/1000000) > 10000) {
                    break;
                }
            }
            long end = System.nanoTime();
            logger.debug("Without caching " + (steps / ((end-start)/1000000000)) + " txs/sec");


            steps = 0L;
            start = System.nanoTime();
            while (true) {
                doRead("cached");
		steps++;
                if (((System.nanoTime() - start)/1000000) > 10000) {
                    break;
                }
            }

            end = System.nanoTime();
	    logger.debug("With L2 caching " + (steps / ((end-start)/1000000000)) + " txs/sec");

            FenixFramework.barrier("finish", Integer.parseInt(args[1]));
        } finally {
            FenixFramework.shutdown();
        }
    }

    public static final Random ran = new Random();
    public static DomainRoot DR = null;

    @Atomic
    public static void doRead(String mode) {
        Book book = new Book("book" + Math.abs(ran.nextInt()));
        if (mode.equals("random")) {
            DomainRoot domainRoot = FenixFramework.getDomainRoot();
            domainRoot.getBooksRandom().contains(book);
        } else {
            DR.getBooksRandomCached(false).contains(book);
        }
    }

    @Atomic
    public static void initDomain() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        App app = domainRoot.getApp();
        if (app == null) {
            app = new App();
            app.setIsPopulated(true);
            domainRoot.setApp(app);
        } else {
            logger.debug("The other machine already populated");
            DR = domainRoot;
            return;
        }
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksRandom(book);
        }
        DR = domainRoot;
    }

}
