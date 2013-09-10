package eu.cloudtm.autonomicManager.RESTServer.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.cloudtm.autonomicManager.commons.dto.StatisticDTO;
import eu.cloudtm.autonomicManager.statistics.StatsManager;
import eu.cloudtm.wpm.parser.ResourceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/monitor")
public class MonitorResource extends AbstractResource {

    private static Log log = LogFactory.getLog(MonitorResource.class);

    @Inject
    private StatsManager statsManager;

    private Gson gson = new GsonBuilder()
            .serializeSpecialFloatingPointValues()
            .create();

    private Map<String,String> param2key = new HashMap<String,String>(){{
        put("throughput",                           "Throughput");
        put("nodes",                                "NumNodes");
        put("writePercentage",                      "RetryWritePercentage");
        put("abortRate",                            "CommitProbability");

    }};

    private Map<String,ResourceType> monitoredParams = new HashMap<String,ResourceType>(){{
        put("Throughput",                           ResourceType.JMX);
        put("NumNodes",                             ResourceType.JMX);
        put("RetryWritePercentage",                 ResourceType.JMX);
        put("CommitProbability",                    ResourceType.JMX);
    }};

    private Map<String,String> editParam = new HashMap<String,String>(){{
        put("CommitProbability",                    "calculateAbortRate");
    }};


    @GET @Path("/{param}")
    @Produces("application/json")
    public synchronized Response getAllData(@PathParam("param") String param) {

        String paramKey = param2key.get(param);
        if( paramKey==null || paramKey.length()<=0 )
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        ResourceType resourceType = monitoredParams.get(paramKey);
        StatisticDTO statDTO = statsManager.getAllAvgStatistic(paramKey);

        if ( editParam.containsKey(paramKey) ){
            log.info("To evaluate");

            Class[] paramDouble = new Class[1];
            paramDouble[0] = Double.class;
            Class cls = this.getClass();
            Object obj = this;
            Method method = null;
            try {
                method = cls.getDeclaredMethod( editParam.get(paramKey), paramDouble);
                for( List<Double> point : statDTO.getData() ) {
                    Double toEdit = point.remove(1);
                    //log.info("---");
                    //log.info("CommitProbability:" + toEdit );
                    Double abortRateEvaluated = (Double) method.invoke(obj, (Double) toEdit );
                    //log.info("Valued evaluate:" + abortRateEvaluated );
                    point.add(1, abortRateEvaluated);
                    //log.info("Point updated:" + point.get(1) );
                    //log.info("---");
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String json = gson.toJson(statDTO);
        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }

    @GET @Path("/{param}/last")
    @Produces("application/json")
    public synchronized Response getLastData(@PathParam("param") String param) {

        String paramKey = param2key.get(param);
        if( paramKey==null || paramKey.length()<=0 )
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        ResourceType resourceType = monitoredParams.get(paramKey);
        StatisticDTO statDTO = statsManager.getLastAvgStatistic(paramKey);

        if ( editParam.containsKey(paramKey) ){
            //log.info("To evaluate");

            Class[] paramDouble = new Class[1];
            paramDouble[0] = Double.class;
            Class cls = this.getClass();
            Object obj = this;
            Method method = null;
            try {
                method = cls.getDeclaredMethod( editParam.get(paramKey), paramDouble);
                for( List<Double> point : statDTO.getData() ) {
                    Double toEdit = point.remove(1);
                    //log.info("---");
                    //log.info("CommitProbability:" + toEdit );
                    Double abortRateEvaluated = (Double) method.invoke(obj, (Double) toEdit );
                    //log.info("Valued evaluate:" + abortRateEvaluated );
                    point.add(1, abortRateEvaluated);
                    //log.info("Point updated:" + point.get(1) );
                    //log.info("---");
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String json = gson.toJson(statDTO);
        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }

    public Double calculateAbortRate(Double commitProbability){
        double val = (1 - commitProbability);
        return new Double(val);
    }

}