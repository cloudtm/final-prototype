package eu.cloudtm.autonomicManager.RESTServer.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.core.Response;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/5/13
 */
public abstract class AbstractResource {

    private Log log = LogFactory.getLog(AbstractResource.class);

    private String _corsHeaders;

    @OPTIONS
    public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    protected Response makeCORS(Response.ResponseBuilder req, String returnMethod) {
        Response.ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT");

        if (!"".equals(returnMethod)) {
            rb.header("Access-Control-Allow-Headers", returnMethod);
        }

        return rb.build();
    }

    protected Response makeCORS(Response.ResponseBuilder req) {
        return makeCORS(req, _corsHeaders);
    }
}
