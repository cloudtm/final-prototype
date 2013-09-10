package eu.cloudtm.autonomicManager.commons;

/**
 * Created with IntelliJ IDEA.
 * User: fabio
 * Date: 7/9/13
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EvaluatedParam {

    ACF                         ( 0, "ACF" ),
    CORE_PER_CPU                ( 1, "CORE_PER_CPU" ),
    MAX_ACTIVE_THREADS          ( 2, "MAX_ACTIVE_THREADS" ),
    SYSTEM_TYPE                 ( 3, "SYSTEM_TYPE" ),
    DATA_ACCESS_FREQUENCIES     ( 4, "DATA_ACCESS_FREQUENCIES" ),
    TX_INVOKER_FREQUENCY        ( 5, "TX_INVOKER_FREQUENCY" ),
    TX_RESPONSE_TIME            ( 6, "TX_RESPONSE_TIME" ),
    ISOLATION_LEVEL             ( 7, "ISOLATION_LEVEL"),

    ;


    private final int id;
    private final String key;

    private EvaluatedParam(int id, String name){
        this.id = id;
        this.key = name;
    }

    public int getId(){
        return id;
    }

    public String getKey(){
        return key;
    }

    public static EvaluatedParam getById(int id){
        for( EvaluatedParam param : values() ){
            if( param.getId() == id )
                return param;
        }
        return null;
    }


}
