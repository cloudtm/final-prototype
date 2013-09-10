//package eu.cloudtm.autonomicManager.newActuator.test;
//
//import eu.cloudtm.InfinispanClient;
//import eu.cloudtm.InfinispanClientImpl;
//import eu.cloudtm.autonomicManager.IActuator;
//import eu.cloudtm.autonomicManager.actuators.DeltaCloudActuator;
//import eu.cloudtm.autonomicManager.actuators.DeltaCloudActuatorFactory;
//import eu.cloudtm.autonomicManager.actuators.ActuatorException;
//import eu.cloudtm.autonomicManager.actuators.CloudTMActuator;
//import org.apache.deltacloud.client.DeltaCloudClient;
//import org.apache.deltacloud.client.DeltaCloudClientException;
//import org.apache.deltacloud.client.DeltaCloudClientImpl;
//
//import java.net.MalformedURLException;
//import java.util.Scanner;
//
///**
// * Created by: Fabio Perfetti
// * E-mail: perfabio87@gmail.com
// * Date: 6/20/13
// */
//public class CloudTMActuatorTest {
//
//    public static void main(String[] args) {
//        System.out.println("Quante istanze vuoi?");
//        Scanner in = new Scanner(System.in);
//        int num = in.nextInt();
//
//        try {
//            DeltaCloudClient deltaCloudClient = new DeltaCloudClientImpl("http://cloudtm.ist.utl.pt:30000",
//                    "fabio+OpenShift","cloud%admin");
//
//            InfinispanClient infinispanClient = new InfinispanClientImpl()
//
//            IActuator actuator = new CloudTMActuator(
//                    deltaCloudClient,
//                    null,
//                    9998,
//                    "456a2259-e1b8-4ca7-9308-6b369720aa82",
//                    "10" );
//
//            actuator.startInstance();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (DeltaCloudClientException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (ActuatorException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//    }
//
//}