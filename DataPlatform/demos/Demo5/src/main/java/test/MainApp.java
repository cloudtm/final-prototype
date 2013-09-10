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
    public static final int WRITER_COUNT = 1;

    public static void main(String[] args) throws Exception {
        try {
            initDomain();
            initDomainOpt();
            initDomainGhost();
            initDomainOptGhost();

            long start = System.nanoTime();
            ThreadWriter[] writer = new ThreadWriter[WRITER_COUNT];
            ThreadReader reader = new ThreadReader("normal");
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i] = new ThreadWriter("normal");
            }
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i].start();
            }
            reader.start();
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i].join();
            }
            reader.join();
            long end = System.nanoTime();
            logger.debug("Normal B+Tree did " + (reader.steps / ((end-start)/1000000000)) + " txs/sec");

            start = System.nanoTime();
            writer = new ThreadWriter[WRITER_COUNT];
            reader = new ThreadReader("ghost");
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i] = new ThreadWriter("ghost");
            }
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i].start();
            }
            reader.start();
            for (int i = 0; i < WRITER_COUNT; i++) {
                writer[i].join();
            }
            reader.join();

            end = System.nanoTime();
	    logger.debug("Ghost B+Tree did " + (reader.steps / ((end-start)/1000000000)) + " txs/sec");

        } finally {
            FenixFramework.shutdown();
        }
    }

    public static class ThreadWriter extends Thread {

        public final Random ran;
        public final String mode;

        public ThreadWriter(String mode) {
            this.ran = new Random();
            this.mode = mode;
        }

        public void run() {
            long start = System.nanoTime();
            while (true) {
                long end = System.nanoTime();
                if (((end-start) / 1000000) > 10000) {
                    break;
                }
                doWrite();
            }
        }

        @Atomic
        public void doWrite() {
            DomainRoot domainRoot = FenixFramework.getDomainRoot();
            Book book = new Book("book" + Math.abs(ran.nextInt()));
            if (mode.equals("normal")) {
                domainRoot.addBooksTree(book);
            } else {
                domainRoot.addBooksGhostTree(book);
            }
        }
    }

    public static class ThreadReader extends Thread {

        public final Random ran;
        public final String mode;
        public long steps;

        public ThreadReader(String mode) {
            this.ran = new Random();
            this.mode = mode;
            this.steps = 0L;
        }

        public void run() {
            long start = System.nanoTime();
            while (true) {
                long end = System.nanoTime();
                if (((end-start) / 1000000) > 10000) {
                    break;
                }
                doRead();
                this.steps++;
            }
        }

        @Atomic
        public void doRead() {
            DomainRoot domainRoot = FenixFramework.getDomainRoot();
            Book book = new Book("book" + Math.abs(ran.nextInt()));
            if (mode.equals("normal")) {
                domainRoot.getBooksTree().contains(book);
            } else {
                domainRoot.getBooksGhostTree().contains(book);
            }
        }
    }

    @Atomic
    public static void initDomain() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksTree(book);
        }
        long end = System.nanoTime();
        logger.debug("Populate domain B+Tree finished in " + ((end-start)/1000000) + " milliseconds");
    }

    @Atomic
    public static void initDomainOpt() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();	
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree Opt");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksOpt(book);
        }
        long end = System.nanoTime();
        logger.debug("Populate domain B+Tree Opt finished in " + ((end-start)/1000000) + " milliseconds");
    }

    @Atomic
    public static void initDomainGhost() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree Ghost");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksGhostTree(book);
        }
        long end = System.nanoTime();
        logger.debug("Populate domain B+Tree Ghost finished in " + ((end-start)/1000000) + " milliseconds");
    }

    @Atomic
    public static void initDomainOptGhost() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        long start = System.nanoTime();
        logger.debug("Populate " + BOOK_COUNT + " books in B+Tree OptGhost");
        for (int i = 0; i < BOOK_COUNT; i++) {
            Book book = new Book("Book" + i);
            domainRoot.addBooksOptGhostTree(book);
        }
        long end = System.nanoTime();
        logger.debug("Populate domain B+Tree OptGhost finished in " + ((end-start)/1000000) + " milliseconds");
    }

}
