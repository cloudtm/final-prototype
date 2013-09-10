package eu.cloudtm.autonomicManager.RESTServer.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.cloudtm.autonomicManager.AutonomicManager;
import eu.cloudtm.autonomicManager.commons.AtomicBooleanSerializer;
import eu.cloudtm.autonomicManager.commons.dto.StatusDTO;
import eu.cloudtm.autonomicManager.statistics.StatsManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicBoolean;


@Singleton
@Path("/status")
public class StatusResource extends AbstractResource {

    private static Log log = LogFactory.getLog(StatusResource.class);

    @Inject
    private AutonomicManager autonomicManager;

    /*
     {
       "currentState":"RUNNING",
       "tuning":{
         "forecaster":"ANALYTICAL",
         "autoScale":"true",
         "autoDegree":"true",
         "autoProtocol":"true"
       },

       "configuration":{
         "platformSize":2,
         "threadPerNode":2,
         "nodesConfig":"MEDIUM",
         "replicationProtocol":"TWOPC",
         "replicationDegree":2,
         "dataPlacement":false
       }
     }
    */
    @GET
    @Produces("application/json")
    public Response getState() {

        log.trace("Generating state to send...");

        //Gson gson = new Gson();
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(AtomicBoolean.class, new AtomicBooleanSerializer());

        StatusDTO statusDTO = new StatusDTO(
                autonomicManager.state(),
                autonomicManager.platformTuning(),
                autonomicManager.platformConfiguration()
        );

        String json = gson.create().toJson(statusDTO);
        log.trace("state: " + json);

        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }

    /*
    @GET @Path("/replication/degree")
    @Produces("application/json")
    public Response getReplicationDegree() {
        String json = gson.toJson(controller.getStateClone().getReplicationDegreeClone());

        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }

    @GET @Path("/replication/protocol")
    @Produces("application/json")
    public Response getReplicationProtocol() {
        String json = gson.toJson(controller.getStateClone().getReplicationProtocolClone());

        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);

    }

    @GET @Path("/placement")
    @Produces("application/json")
    public Response getDataPlacement() {
        String json = gson.toJson(controller.getStateClone().getDataPlacementClone());

        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);

    }

    @GET @Path("/scale")
    @Produces("application/json")
    public Response getScale() {
        String json = gson.toJson(controller.getStateClone().getScaleClone());

        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }
    */

}
