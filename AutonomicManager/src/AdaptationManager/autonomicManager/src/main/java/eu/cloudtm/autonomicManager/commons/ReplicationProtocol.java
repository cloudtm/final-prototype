package eu.cloudtm.autonomicManager.commons;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/1/13
 */
public enum ReplicationProtocol {

    TWOPC("2PC", 0),TO("TO",1),PB("PB",2);

    private final int id;
    private final String wpmValue;

    private ReplicationProtocol(final String text, int id) {
        this.id = id;
        this.wpmValue = text;
    }

    public static ReplicationProtocol getByWPMValue(String wpmValue){
        for ( ReplicationProtocol rp : values() ){
            if(rp.getWpmValue().equals(wpmValue))
                return rp;
        }
        throw new RuntimeException("No protocol available with wpmValue=" + wpmValue );
    }

    public int getId(){
        return id;
    }

    public String getWpmValue() {
        return wpmValue;
    }

}
