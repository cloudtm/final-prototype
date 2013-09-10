package test;

public class ScifiBook extends ScifiBook_Base {

    public ScifiBook() {
        super();
    }

    public ScifiBook(eu.cloudtm.LocalityHints hints) {
        super(hints);
    }

    public ScifiBook(String name) {
        this();
        setBookName(name);
    }

    @Override
    public String toString() {
        return super.toString() + " (Scifi)";
    }

}
