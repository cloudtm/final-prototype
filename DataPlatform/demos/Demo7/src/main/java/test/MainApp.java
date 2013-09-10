package test;

import java.util.*;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;

import pt.ist.fenixframework.FenixFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static final int BOOK_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        try {
            initDomain();
            initDomainIndexed();

            long steps = 0L;
            long start = System.nanoTime();
            while (true) {
                doRead("non-indexed");
                if (((System.nanoTime() - start) / 1000000) > 10000) {
                    break;
                }
                steps++;
            }
            long end = System.nanoTime();
            logger.debug("B+Tree non-indexed did " + (steps / ((end-start)/1000000000)) + " txs/sec");

            steps = 0L;
            start = System.nanoTime();
            while (true) {
                doRead("indexed");
                if (((System.nanoTime() - start) / 1000000) > 10000) {
                    break;
                }
                steps++;
            }
            end = System.nanoTime();
            logger.debug("B+Tree indexed did " + (steps / ((end-start)/1000000000)) + " txs/sec");

        } finally {
            FenixFramework.shutdown();
        }
    }

    public static final Random ran = new Random();

    @Atomic
    public static void doRead(String mode) {
        String val = "book" + Math.abs(ran.nextInt(BOOK_COUNT));
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (mode.equals("non-indexed")) {
            for (Book b : domainRoot.getBooks()) {
                if (b.getBookName().equals(val)) return;
            }
        } else {
            domainRoot.getBooksIndexedByBookName(val);
        }
    }

    @Atomic
    public static void initDomain() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree non-indexed");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooks(book);
        }
        long end = System.nanoTime();
    }

    @Atomic
    public static void initDomainIndexed() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();	
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree Indexed");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksIndexed(book);
        }
        long end = System.nanoTime();
    }

}
