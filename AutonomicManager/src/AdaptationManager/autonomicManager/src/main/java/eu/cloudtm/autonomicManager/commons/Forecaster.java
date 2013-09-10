package eu.cloudtm.autonomicManager.commons;


import eu.cloudtm.autonomicManager.configs.Config;
import eu.cloudtm.autonomicManager.configs.KeyConfig;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 5/30/13
 */
public enum Forecaster {
    NONE(false, null),
    COMMITTEE(true, ""),
    ANALYTICAL( true, Config.getInstance().getString( KeyConfig.FORECASTER_ANALYTICAL.key() ) ),
    SIMULATOR( true, Config.getInstance().getString( KeyConfig.FORECASTER_SIMULATOR.key() ) ),
    MACHINE_LEARNING(true, Config.getInstance().getString( KeyConfig.FORECASTER_MACHINE_LEARNING.key() ));

    private final boolean autoTuning;
    private final String oracleClass;

    private Forecaster(final boolean _autoScale, String _oracleClass) {
        autoTuning = _autoScale;
        oracleClass = _oracleClass;
    }

    public boolean isAutoTuning() {
        return autoTuning;
    }

    public String getOracleClass(){
        return oracleClass;
    }
}
