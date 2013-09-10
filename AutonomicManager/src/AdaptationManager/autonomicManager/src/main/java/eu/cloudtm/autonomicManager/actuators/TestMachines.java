package eu.cloudtm.autonomicManager.actuators;

import org.apache.log4j.PropertyConfigurator;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 8/24/13
 */
public class TestMachines {

    public static void main(String[] args){

        PropertyConfigurator.configure("config/log4j.properties");

        ActuatorFactory factory = new ActuatorFactory();
        factory.build();

    }


}
