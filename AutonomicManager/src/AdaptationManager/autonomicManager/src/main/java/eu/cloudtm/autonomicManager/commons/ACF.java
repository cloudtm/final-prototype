package eu.cloudtm.autonomicManager.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/18/13
 */
public class ACF {

    private static Log log = LogFactory.getLog(ACF.class);

    private double PaoloRemoteTakenLocks,
                          NumPuts,
                          PaoloLocalTakenHoldTime,
                          PaoloLocalTakenLocks,
                          PaoloRemoteTakenHoldTime,
                          threads,
                          timeWindow;

    public ACF(double _PaoloLocalTakenLocks,
               double _NumPuts,
               double _PaoloLocalTakenHoldTime,
               double _PaoloRemoteTakenHoldTime,
               double _PaoloRemoteTakenLocks,
               double _threads,
               double _timeWindow){
        PaoloRemoteTakenLocks = _PaoloRemoteTakenLocks;
        NumPuts = _NumPuts;
        PaoloLocalTakenHoldTime = _PaoloLocalTakenHoldTime;
        PaoloLocalTakenLocks = _PaoloLocalTakenLocks;
        PaoloRemoteTakenHoldTime = _PaoloRemoteTakenHoldTime;
        threads = _threads;
        timeWindow = _timeWindow;
    }

    private double getLocalAbortProb() {
        double puts = NumPuts;
        log.trace("Attempted put " + puts);
        double okPuts = PaoloLocalTakenLocks;
        log.trace("Ok put " + okPuts);
        if (puts != 0) {
            return (puts - okPuts) / puts;
        }
        return 0;
    }

    public double evaluate() {

        double otherThreads = threads - 1.0D;
        double pCont = getLocalAbortProb();
        log.trace("pCont is " + pCont);
        if (pCont == 0)
            return 0D;
        double otherThreadsFraction = otherThreads / threads;
        double paoloLocalLocks = PaoloLocalTakenLocks;
        double paoloRemoteLocks = PaoloRemoteTakenLocks;
        double otherLocalLocks = paoloLocalLocks * otherThreadsFraction;
        log.trace("OtherLocalLocks " + otherLocalLocks);
        double ll = 1e-9 * otherLocalLocks / timeWindow;
        log.trace("localLambda " + ll);
        double rl = 1e-9 * paoloRemoteLocks / timeWindow;
        log.trace("remoteLambda " + rl);
        double lh = pLocalHoldTime();
        double rh = pRemoteHoldTime();
        double lm = ll * lh + rl * rh;
        if (lm == 0)
            return 0;

        return pCont / (lm);
    }

    private double pLocalHoldTime() {
        double pLocalHoldTime = PaoloLocalTakenHoldTime;
        double pLocalLocks = PaoloLocalTakenLocks;
        if (pLocalLocks == 0)
            return 0;
        return pLocalHoldTime / pLocalLocks;
    }

    private double pRemoteHoldTime() {
        double pRemoteHoldTime = PaoloRemoteTakenHoldTime;
        double pRemoteLocks = PaoloRemoteTakenLocks;
        if (pRemoteLocks == 0)
            return 0;
        return pRemoteHoldTime / pRemoteLocks;

    }

}
