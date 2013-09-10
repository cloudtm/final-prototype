package eu.cloudtm.autonomicManager.statistics;

import eu.cloudtm.autonomicManager.commons.Param;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/10/13
 */
public class WPMSample implements Sample {

   private static AtomicLong counter = new AtomicLong(0);
   private final long id;
   private final Map<String, Object> aggregatedFromWPM;

   public WPMSample(long _id, Map<String, Object> aggregated) {
      this.id = _id;
      this.aggregatedFromWPM = aggregated;
   }

   public static WPMSample getInstance(Map<String, Object> params2values) {
      WPMSample sample = new WPMSample(
              counter.getAndIncrement(),
              new HashMap<String, Object>(params2values)
      );

      return sample;
   }

   public Map<String, Object> getParams() {
      return aggregatedFromWPM;
   }

   public long getId() {
      return id;
   }

   @Override
   public Object getParam(Param param) {
      return aggregatedFromWPM.get(param.getKey());
   }


}

