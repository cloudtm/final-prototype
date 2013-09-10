package eu.cloudtm.autonomicManager;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.Forecaster;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfCustomParamDTO;
import eu.cloudtm.autonomicManager.commons.dto.WhatIfDTO;
import eu.cloudtm.autonomicManager.oracles.OracleService;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.statistics.CustomSample;
import eu.cloudtm.autonomicManager.statistics.ProcessedSample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * User: Fabio Perfetti perfabio87 [at] gmail.com
 * Date: 7/8/13
 * Time: 1:33 PM
 */
public class WhatIfService {

    private static Log log = LogFactory.getLog(WhatIfService.class);

    private final ProcessedSample processedSample;

    public WhatIfService(ProcessedSample processedSample){
        this.processedSample = processedSample;

    }

    public WhatIfCustomParamDTO retrieveCurrentValues(){

        WhatIfCustomParamDTO customParam = new WhatIfCustomParamDTO();

        Double acf = null;
        Object acfObj = processedSample.getEvaluatedParam(EvaluatedParam.ACF);
        if(acfObj != null){
            acf = (Double) acfObj;
        }
        customParam.setACF( acf  );
        customParam.setAvgCommitAsync((Long) processedSample.getParam(Param.AvgCommitAsync));
        customParam.setAvgPrepareAsync((Long) processedSample.getParam(Param.AvgPrepareAsync));
        customParam.setAvgPrepareCommandSize((Long) processedSample.getParam(Param.AvgPrepareCommandSize));
        customParam.setAvgNumPutsBySuccessfulLocalTx((Double) processedSample.getParam(Param.AvgNumPutsBySuccessfulLocalTx));
        customParam.setPercentageSuccessWriteTransactions((Double) processedSample.getParam(Param.PercentageSuccessWriteTransactions));
        customParam.setLocalUpdateTxLocalServiceTime( (Long) processedSample.getParam( Param.LocalUpdateTxLocalServiceTime ) );
        customParam.setLocalReadOnlyTxLocalServiceTime( (Long) processedSample.getParam( Param.LocalReadOnlyTxLocalServiceTime ) );
        customParam.setAvgRemoteGetRtt( (Long) processedSample.getParam( Param.AvgRemoteGetRtt ) );
        customParam.setAvgGetsPerWrTransaction( (Long) processedSample.getParam( Param.AvgGetsPerWrTransaction ) );
        customParam.setAvgGetsPerROTransaction( (Long) processedSample.getParam( Param.AvgGetsPerROTransaction ) );
        return customParam;
    }


    public List<WhatIfDTO> evaluate(WhatIfCustomParamDTO customParamDTO){

        Map<Param, Object> customParam = extractCustomParam(customParamDTO);
        Map<EvaluatedParam, Object> customEvaluatedParam = extractCustomEvaluatedParam(customParamDTO);

        List<WhatIfDTO> result = new ArrayList<WhatIfDTO>();

        for(Forecaster forecaster : customParamDTO.getForecasters()){
            WhatIfDTO currWhatIfResult = new WhatIfDTO(forecaster, customParamDTO.getXaxis());

            if(forecaster.equals(Forecaster.COMMITTEE)){
                throw new RuntimeException("Not yet implemented");
            } else {
                OracleService oracleService = OracleService.getInstance( forecaster.getOracleClass() );
                log.info("Querying " + forecaster);

                /* creating custom Sample, with custom maps */
                CustomSample customSample = new CustomSample(processedSample, customParam, customEvaluatedParam);

                /* invoking OracleService, which returns a map of configurations-output */

                TreeMap<PlatformConfiguration, OutputOracle> currForecast;

                switch (customParamDTO.getXaxis()){
                    case NODES:
                        log.info("Nodes on x-axis");
                        currForecast = oracleService.whatIf( customSample,
                                customParamDTO.getFixedProtocol(),
                                customParamDTO.getFixedDegree() );
                        break;
                    case DEGREE:
                        log.info("Degrees on x-axis");
                        currForecast = oracleService.whatIf( customSample,
                                customParamDTO.getFixedNodes(),
                                customParamDTO.getFixedProtocol() );
                        break;
                    case PROTOCOL:
                        log.info("Protocols on x-axis");
                        currForecast = oracleService.whatIf( customSample,
                                customParamDTO.getFixedNodes(),
                                customParamDTO.getFixedDegree() );
                        break;
                    default:
                        throw new IllegalStateException("Xaxis can't be null!");
                }

                for (Map.Entry<PlatformConfiguration, OutputOracle> entry : currForecast.entrySet()){

                    long xaxis = 0;
                    switch ( customParamDTO.getXaxis() ){
                        case NODES:
                            xaxis = entry.getKey().platformSize();
                            break;
                        case DEGREE:
                            xaxis = entry.getKey().replicationDegree();
                            break;
                        case PROTOCOL:
                            xaxis = entry.getKey().replicationProtocol().getId();
                            break;
                    }
                    OutputOracle currOut = entry.getValue();

                    if(currOut==null){
                        log.info("Forecaster " + forecaster + " returned a null OutputOracle for PlatformConfiguration " + xaxis);
                        continue;
                    }

                    //TODO FIX TX CLASSES
                    log.warn("FIX TX CLASSES");


                    currWhatIfResult.addThroughputPoint( xaxis, currOut.throughput(0) );
                    currWhatIfResult.addReadResponseTimePoint(xaxis, currOut.responseTime(0));
                    currWhatIfResult.addWriteResponseTimePoint(xaxis, currOut.responseTime(1));
                    currWhatIfResult.addAbortRatePoint(xaxis, currOut.abortRate(0));
                }

            }
            result.add(currWhatIfResult);
        }
        return result;
    }

    private Map<Param, Object> extractCustomParam(WhatIfCustomParamDTO whatIfCustomParam){
        Map<Param, Object> customParam = new HashMap<Param, Object>();
        if( whatIfCustomParam!=null ){
            log.trace("Extracting AvgCommitAsync, whatIfCustomParam contains: " + whatIfCustomParam.getAvgCommitAsync());
            customParam.put(Param.AvgCommitAsync, whatIfCustomParam.getAvgCommitAsync());

            log.trace("Extracting AvgPrepareAsync, whatIfCustomParam contains: " + whatIfCustomParam.getAvgPrepareAsync());
            customParam.put(Param.AvgPrepareAsync, whatIfCustomParam.getAvgPrepareAsync());

            log.trace("Extracting AvgPrepareCommandSize, whatIfCustomParam contains: " + whatIfCustomParam.getAvgPrepareCommandSize());
            customParam.put(Param.AvgPrepareCommandSize, whatIfCustomParam.getAvgPrepareCommandSize() );

            log.trace("Extracting AvgNumPutsBySuccessfulLocalTx, whatIfCustomParam contains: " + whatIfCustomParam.getAvgCommitAsync());
            customParam.put(Param.AvgNumPutsBySuccessfulLocalTx, whatIfCustomParam.getAvgNumPutsBySuccessfulLocalTx() );

            log.trace("Extracting PercentageSuccessWriteTransactions, whatIfCustomParam contains: " + whatIfCustomParam.getPercentageSuccessWriteTransactions());
            customParam.put(Param.PercentageSuccessWriteTransactions, whatIfCustomParam.getPercentageSuccessWriteTransactions() );

            log.trace("Extracting LocalUpdateTxLocalServiceTime, whatIfCustomParam contains: " + whatIfCustomParam.getLocalUpdateTxLocalServiceTime());
            customParam.put(Param.LocalUpdateTxLocalServiceTime, whatIfCustomParam.getLocalUpdateTxLocalServiceTime() );

            log.trace("Extracting LocalReadOnlyTxLocalServiceTime, whatIfCustomParam contains: " + whatIfCustomParam.getLocalReadOnlyTxLocalServiceTime());
            customParam.put(Param.LocalReadOnlyTxLocalServiceTime, whatIfCustomParam.getLocalReadOnlyTxLocalServiceTime() );
        }
        log.trace("extractCustomParam has extracted " + customParam.size() + " params");
        return customParam;
    }

    private Map<EvaluatedParam, Object> extractCustomEvaluatedParam(WhatIfCustomParamDTO whatIfCustomParam){
        Map<EvaluatedParam, Object> customParam = new HashMap<EvaluatedParam, Object>();
        if( whatIfCustomParam!=null ){
            log.trace("Extracting ACF, whatIfCustomParam contains: " + whatIfCustomParam.getACF());
            customParam.put(EvaluatedParam.ACF, whatIfCustomParam.getACF());
        }
        log.trace("extractCustomEvaluatedParam has extracted " + customParam.size() + " params");
        return customParam;
    }

}
