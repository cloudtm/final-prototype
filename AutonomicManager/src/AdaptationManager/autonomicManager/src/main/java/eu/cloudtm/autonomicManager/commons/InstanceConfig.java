package eu.cloudtm.autonomicManager.commons;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 5/27/13
 */
public enum InstanceConfig {
    NONE("none"),SMALL("small"), MEDIUM("medium"), LARGE("large");
    
    private final String text;

    private InstanceConfig(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}