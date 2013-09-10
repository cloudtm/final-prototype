package eu.cloudtm.autonomicManager.RESTServer.resources;

import com.google.gson.Gson;
import eu.cloudtm.autonomicManager.commons.Forecaster;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

// The Java class will be hosted at the URI path "/myresource"
@Singleton
@Path("/replication")
public class ReplicationResource extends AbstractResource {

    private static Log log = LogFactory.getLog(ReplicationResource.class);

    Gson gson = new Gson();

    @PUT
    @Path("/degree")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public synchronized Response setDegree(
            @DefaultValue("NONE") @FormParam("rep_degree_forecasting") Forecaster forecaster,
            @DefaultValue("0") @FormParam("rep_degree_size") int degree
    ) {

        log.info("forecaster: " + forecaster);
        log.info("degree: " + degree);

        if( degree<=0 && !forecaster.isAutoTuning() ){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        //ControllerOld.getInstance().updateDegree(degree, tuning);


        // TODO solve String json = gson.toJson(ControllerOld.getInstance().getState());
        String json = "{ \"to\":\"implement\" }";
        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }

    @PUT
    @Path("/protocol")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public synchronized Response setProtocol(
            @DefaultValue("NONE") @FormParam("rep_protocol_forecasting") Forecaster forecaster,
            @FormParam("rep_protocol") ReplicationProtocol protocol
    ) {

        log.info("forecaster: " + forecaster);
        log.info("protocol: " + protocol);

        //ControllerOld.getInstance().updateProtocol(protocol, tuning);

        //TODO solve: String json = gson.toJson(ControllerOld.getInstance().getState());
        String json = gson.toJson("");
        Response.ResponseBuilder builder = Response.ok(json);
        return makeCORS(builder);
    }


}