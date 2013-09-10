package eu.cloudtm.autonomicManager.optimizers;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import pt.ist.clustering.LDA.LDA;

import java.util.LinkedHashMap;


public class LAOptimizer implements OptimizerFilter {
    private boolean clustered = false;
    //private int numberOfClusters;//to be established through LDA+Silhouette optimization after calling the generateClustering
    private int numberOfAvailableNodes;//number of available nodes for processing requests. to be updated at runtime
    private LinkedHashMap<String,Integer> txClusterMap;// txID - clusterID map
    private LinkedHashMap<Integer,Integer> txIDClusterMap;// LDA_tx_ID - clusterID map
    private LinkedHashMap<String,Integer> txIDMap;// txID - LDA_tx_ID
    private LinkedHashMap<String,Integer> domainIDMap;// domClass - LDA_DomClass_ID
    private LinkedHashMap<Integer,String> reverseTxIDMap;// LDA_tx_ID - txID
    private LinkedHashMap<Integer,String> reverseDomainIDMap;//  LDA_DomClass_ID - domClass
    private LinkedHashMap<Integer,Float> clusterWeight;//normalized load (sum of all loads = 1) expected to be generated in every cluster


    @Override
    public OptimizerType getType() {
        return OptimizerType.LARD;
    }

    @Override
    public Object doOptimize(ProcessedSample processedSample) {
        LinkedHashMap<String,Integer> txInvokeFrequency = (LinkedHashMap<String, Integer>) processedSample.getEvaluatedParam(EvaluatedParam.TX_INVOKER_FREQUENCY);
        LinkedHashMap<String,Float> txResponseTime = (LinkedHashMap<String, Float>) processedSample.getEvaluatedParam(EvaluatedParam.TX_RESPONSE_TIME);
        LinkedHashMap<String,Float> txWeight = calculateTxWeight(txInvokeFrequency, txResponseTime);
        int clusterID;

        if (!clustered) {
            generateClusters((LinkedHashMap<String, LinkedHashMap<String, Integer>>) processedSample.getEvaluatedParam(EvaluatedParam.DATA_ACCESS_FREQUENCIES));
            clustered = true;
        }

        clusterWeight = new LinkedHashMap<Integer,Float>();

        for (String s : txWeight.keySet()) {
            clusterID = txClusterMap.get(s);
            if (clusterWeight.containsKey(clusterID)) clusterWeight.put(clusterID, clusterWeight.get(clusterID) + txWeight.get(s));
            else clusterWeight.put(clusterID, txWeight.get(s));
        }
        //numberOfClusters = clusterWeight.size();

        //LinkedHashMap<String,Integer> txIDMap; //transactionID - clusterID
        //LinkedHashMap<Integer,Float> clusterWeight;  //clusterID - clusterWeight



        throw new RuntimeException("THIS METHOD SHOULD RETURN SOMETHING...ASK TO STOYAN!");

    }


    //<TransactionID,<DomainClassID,AccessFrequency>>
    private void generateClusters(LinkedHashMap<String,LinkedHashMap<String,Integer>> dataAccessFrequencies) {
        int txLDA = 0;
        int domainLDA = 1;
        int numberOfClusters = 0;

        LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>> ldaInput = new LinkedHashMap<Integer,LinkedHashMap<Integer,Integer>>();
        LinkedHashMap<Integer,Integer> temp;

        txIDMap = new LinkedHashMap<String,Integer>();
        domainIDMap = new LinkedHashMap<String,Integer>();
        reverseTxIDMap = new LinkedHashMap<Integer,String>();
        reverseDomainIDMap = new LinkedHashMap<Integer,String>();

        for (String txID : dataAccessFrequencies.keySet()) {
            txIDMap.put(txID, txLDA);
            reverseTxIDMap.put(txLDA, txID);

            temp = new LinkedHashMap<Integer,Integer>();

            for (String domainClass : dataAccessFrequencies.get(txID).keySet()) {
                if (!domainIDMap.containsKey(domainClass)) {
                    domainIDMap.put(domainClass, domainLDA);
                    reverseDomainIDMap.put(domainLDA, domainClass);

                    domainLDA++;
                }
                temp.put(domainIDMap.get(domainClass), dataAccessFrequencies.get(txID).get(domainClass));
            }
            ldaInput.put(txLDA, temp);

            txLDA++;
        }

        txClusterMap = new LinkedHashMap<String,Integer>();
        txIDClusterMap = LDA.generateOptimalLDA(ldaInput);

        int t = 0;
        int clusters = 0;
        for (String txID : dataAccessFrequencies.keySet()) {
            //System.out.println("looking up " +txID + ", that goes into cluster " + txIDClusterMap.get(t));
            txClusterMap.put(txID, txIDClusterMap.get(t));
            if (numberOfClusters <= (txIDClusterMap.get(t) + 1)) numberOfClusters = txIDClusterMap.get(t) + 1;
            t++;
        }
        //System.out.println("There are " + numberOfClusters + " clusters.");
    }


    /*
    ctxInfo1#ctxInfo2#(...)ctxInfoN#
    where each ctxInfo has the following format:
        ctxClassName_ctxMethodName:token1;token2;(...)tokenN;
    where each token corresponds to a fullyQualifiedDomainClassName.attributeName=accessFrequency
    */
//    @Deprecated // MOVED IN PROCESSED SAMPLE
//    private LinkedHashMap<String,LinkedHashMap<String,Integer>> getDataAccessFrequencies(ProcessedSample processedSample) {
//        LinkedHashMap<String,LinkedHashMap<String,Integer>> result = new LinkedHashMap<String,LinkedHashMap<String,Integer>>();
//        //String readData = (String)processedSample.getParam(Param.getByName("DAP.DapReadAccessData_0"));
//        //String writeData = (String)processedSample.getParam(Param.getByName("DAP.DapWriteAccessData_0"));
//        LinkedList<String> paramNameList = new LinkedList<String>();
//        LinkedHashMap<String,Integer> contextStats;
//        String toParse;
//        String[] contexts;
//        String currentContext;
//        String contextName;
//        String[] splitContext;
//        String[] tokens;
//        String domainAttribute;
//        String domainClass;
//        String frequency;
//
//        paramNameList.add("DAP.DapReadAccessData_0");
//        paramNameList.add("DAP.DapWriteAccessData_0");
//
//        for (String paramName : paramNameList) {
//            //toParse = readData;
//            toParse = (String)processedSample.getParam(Param.getByName(paramName));
//            contexts = toParse.split("#");
//
//            for (int i = 0; i < contexts.length; i++) {
//                splitContext = contexts[i].split(":");
//                contextName = splitContext[0].split("_")[0];
//                tokens = splitContext[1].split(";");
//
//                if (result.containsKey(contextName)) {
//                    contextStats = result.get(contextName);
//                } else {
//                    contextStats = new LinkedHashMap<String,Integer>();// domainClass - accessFrequency
//                    result.put(contextName, contextStats);
//                }
//
//                for (int j = 0; j < tokens.length; j++) {
//                    //fullyQualifiedDomainClassName.attributeName=accessFrequency
//                    domainAttribute = tokens[j].split("=")[0];
//                    frequency = tokens[j].split("=")[1];
//                    domainClass = domainAttribute.substring(0, domainAttribute.lastIndexOf("."));
//
//                    if (contextStats.containsKey(domainClass))
//                        contextStats.put(domainClass, contextStats.get(domainClass) + (new Integer(frequency)));
//                    else
//                        contextStats.put(domainClass, new Integer(frequency));
//                }
//            }
//        }
//        return result;
//    }

//    @Deprecated // MOVED IN PROCESSED SAMPLE
//    private LinkedHashMap<String,Integer> getTxInvokeFrequency(ProcessedSample processedSample) {
//        LinkedHashMap<String,Integer> result = new LinkedHashMap<String,Integer>();
//        LinkedList<String> txClassList = new LinkedList<String>();
//        String attributeName = "AvgTxArrivalRate_0";
//
//        txClassList.add("NBST_CLASS");
//
//        for (String txClass : txClassList) {
//            result.put(txClass, ((Long)processedSample.getParam(Param.getByName(txClass + "." + attributeName))).intValue());
//        }
//
//        //result.put("NBSST_CLASS", ((Long)processedSample.getParam(Param.getByName("NBST_CLASS.AvgTxArrivalRate_0"))).intValue());
//
//        return result;
//    }


//    @Deprecated // MOVED IN PROCESSED SAMPLE
//    private LinkedHashMap<String,Float> getTxResponseTime(ProcessedSample processedSample) {
//        LinkedHashMap<String,Float> result = new LinkedHashMap<String,Float>();
//        LinkedList<String> txClassList = new LinkedList<String>();
//        String attributeName = "AvgResponseTime_0";
//
//        txClassList.add("NBST_CLASS");
//
//        for (String txClass : txClassList) {
//            result.put(txClass, ((Long)processedSample.getParam(Param.getByName(txClass + "." + attributeName))).floatValue());
//        }
//
//        //result.put("NBST_CLASS", ((Long)processedSample.getParam(Param.getByName("NBST_CLASS.AvgResponseTime_0"))).floatValue());
//
//        return result;
//    }

    private LinkedHashMap<String,Float> calculateTxWeight(LinkedHashMap<String,Integer> txInvokeFrequency, LinkedHashMap<String,Float> txResponseTime) {
        LinkedHashMap<String,Float> normalizedWeight = new LinkedHashMap<String,Float>();
        LinkedHashMap<String,Float> temp = new LinkedHashMap<String,Float>();
        float totalWeight = 0;
        float txWeight = 0;

        for (String txID : txInvokeFrequency.keySet()) {
            txWeight = (float)txInvokeFrequency.get(txID) * txResponseTime.get(txID);
            totalWeight += txWeight;
            temp.put(txID, txWeight);
        }

        for (String txID : txInvokeFrequency.keySet()) {
            normalizedWeight.put(txID, temp.get(txID) / totalWeight);
        }

        return normalizedWeight;
    }

}