package eu.cloudtm.autonomicManager.actuators.clients;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/27/13
 */
@Deprecated
public class SlaveKillerResponse {

    // {
    //   "host":"172.31.0.24",
    //   "port":"9998",
    //   "component":"TpccBenchmark",
    //   "action":"stop",
    //   "result":"Success"
    // }

    private String host;
    private String port;
    private String component;
    private String action;
    private String result;

    public SlaveKillerResponse(String host, String port, String component, String action, String result) {
        this.host = host;
        this.port = port;
        this.component = component;
        this.action = action;
        this.result = result;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getComponent() {
        return component;
    }

    public String getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("[ ")
                .append("HOST: " + getHost() + " ")
                .append("PORT: " + getPort() + " ")
                .append("COMPONENT:" + getComponent() + " ")
                .append("ACTION: " + getAction() + " ")
                .append("RESULT: " + getResult() + " ")
                .append("]");
        return builder.toString();
    }

}
