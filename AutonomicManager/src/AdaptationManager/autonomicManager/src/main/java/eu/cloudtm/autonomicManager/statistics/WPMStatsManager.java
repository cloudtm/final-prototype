package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.dto.StatisticDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Singleton;
import java.util.*;

/**
 * It contains a stack of samples.
 *
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/6/13
 */

@Singleton
public class WPMStatsManager extends SampleProducer implements StatsManager {

    private final static Log log = LogFactory.getLog(WPMStatsManager.class);

    private final static int MAX_SIZE = 1000;

    private final Deque<ProcessedSample> stack = new ArrayDeque<ProcessedSample>(MAX_SIZE);

    private final Set<SampleListener> listeners = new HashSet<SampleListener>();

    public WPMStatsManager(){
    }

    @Override
    public final void push(ProcessedSample wpmProcessedSample){
        if(stack.size()>=MAX_SIZE){
            ProcessedSample removed = stack.removeLast();
            //log.trace("Deleted stat: " + removed.getId());
        }
        stack.push(wpmProcessedSample);
        notifyListeners(wpmProcessedSample);
        log.info("New stas added: " + wpmProcessedSample.getId());
    }

    @Override
    public final ProcessedSample getLastSample(){
        return stack.peek();
    }

    @Override
    public final List<ProcessedSample> getLastNSample(int n){
        List<ProcessedSample> samples = new ArrayList<ProcessedSample>();
        Queue<ProcessedSample> queue = Collections.asLifoQueue(new ArrayDeque<ProcessedSample>(stack));
        for(int i=0; i<n; i++){
            ProcessedSample sample = queue.remove();
            samples.add( sample );
        }
        return samples;
    }

    /**
     * This method returns a StatisticDTO, useful to communicate to RESTInterface.
     * It contains all the sample averages belong nodes
     * @param param
     * @return
     */
    @Override
    public final StatisticDTO getAllAvgStatistic(String param){

        StatisticDTO ret = new StatisticDTO(param);

        Iterator<ProcessedSample> iter;
        for (iter = stack.descendingIterator(); iter.hasNext();  ) {
            ProcessedSample processedSample = iter.next();
            ret.addPoint(
                    processedSample.getId(),
                    (Double) processedSample.getParam(Param.getByName(param))
            );
        }
        return ret;
    }

    /**
     * This method returns all samples related to a single stat
     * @param param
     * @return
     */
    @Override
    public final StatisticDTO getLastAvgStatistic(String param){
        StatisticDTO ret = new StatisticDTO(param);
        ProcessedSample processedSample = stack.peek();
        if(processedSample == null)
            return ret;
        ret.addPoint(
                processedSample.getId(),
                (Double) processedSample.getParam(Param.getByName(param))
        );

        return ret;
    }

    @Override
    public final void notifyListeners(ProcessedSample sample) {
        for(SampleListener listener : listeners){
            listener.onNewSample(sample);
        }
    }

    @Override
    public final void removeListener(SampleListener listener) {
        listeners.remove(listener);
    }

    @Override
    public final void addListener(SampleListener listener) {
        listeners.add(listener);
    }

}


