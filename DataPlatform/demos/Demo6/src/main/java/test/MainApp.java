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
            logger.debug("Random B+Tree " + (steps / ((end-start)/1000000000)) + " txs/sec");


            steps = 0L;
            start = System.nanoTime();
            while (true) {
                doRead("colocated");
		steps++;
                if (((System.nanoTime() - start)/1000000) > 10000) {
                    break;
                }
            }

            end = System.nanoTime();
	    logger.debug("Co-located B+Tree " + (steps / ((end-start)/1000000000)) + " txs/sec");

            FenixFramework.barrier("finish", Integer.parseInt(args[1]));
        } finally {
            FenixFramework.shutdown();
        }
    }

    public static final Random ran = new Random();

    @Atomic
    public static void doRead(String mode) {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        Book book = new Book("book" + Math.abs(ran.nextInt()));
        if (mode.equals("random")) {
            domainRoot.getBooksRandom().contains(book);
        } else {
            domainRoot.getBooksColocated().contains(book);
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
            return;
        }
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksRandom(book);
        }
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksColocated(book);
        }
    }

}
