package eu.cloudtm.autonomicManager.commons;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/1/13
 */
public class PlatformTuning {

    private static Log log = LogFactory.getLog(PlatformTuning.class);

    private Forecaster forecaster = Forecaster.ANALYTICAL;

    private AtomicBoolean autoScale = new AtomicBoolean(true);
    private AtomicBoolean autoDegree = new AtomicBoolean(true);
    private AtomicBoolean autoProtocol = new AtomicBoolean(true);

    public PlatformTuning(Forecaster forecaster, boolean defaultTuning){
        this.forecaster = forecaster;
        autoScale = new AtomicBoolean(defaultTuning);
        autoDegree = new AtomicBoolean(defaultTuning);
        autoProtocol = new AtomicBoolean(defaultTuning);
    }

    public void setForecaster(Forecaster forecaster){
        this.forecaster = forecaster;
    }

    public Forecaster forecaster(){
        return forecaster;
    }

    public void autoScale(boolean tuning){
        autoScale.set(tuning);
    }

    public boolean isAutoScale(){
        return autoScale.get();
    }

    public void autoDegree(boolean tuning){
        autoDegree.set(tuning);
    }

    public boolean isAutoDegree(){
        return autoDegree.get();
    }

    public void autoProtocol(boolean tuning){
        autoProtocol.set(tuning);
    }

    public boolean isAutoProtocol(){
        return autoProtocol.get();
    }

    public PlatformTuning cloneThroughJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return gson.fromJson(json, PlatformTuning.class);
    }
}
