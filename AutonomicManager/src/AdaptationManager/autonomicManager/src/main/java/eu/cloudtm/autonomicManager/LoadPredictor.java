package eu.cloudtm.autonomicManager;

import java.util.Random;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/16/13
 */
public class LoadPredictor {

    public static double doPrediction(){
        Random rnd = new Random();
        double randomNum = (double) rnd.nextInt(3000 - 700 + 1) + 700;
        return randomNum;
    }
}
