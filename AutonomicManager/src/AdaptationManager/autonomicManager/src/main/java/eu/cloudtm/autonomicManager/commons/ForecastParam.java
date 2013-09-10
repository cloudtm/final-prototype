package eu.cloudtm.autonomicManager.commons;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 7/12/13
 */
public enum ForecastParam {

   ReplicationProtocol(0, "REP_PROT", eu.cloudtm.autonomicManager.commons.ReplicationProtocol.class),
   ReplicationDegree(1, "REP_DEGREE", Integer.class),
   NumNodes(2, "NUM_NODES", Integer.class),

    ;

   private final int id;
   private final String key;
   private final Class clazz;

   private ForecastParam(int id, String name, Class clazz) {
      this.id = id;
      this.key = name;
      this.clazz = clazz;
   }

   public static ForecastParam getById(int id) {
      for (ForecastParam param : values()) {
         if (param.getId() == id)
            return param;
      }
      return null;
   }

   public static ForecastParam getByName(String name) {
      for (ForecastParam param : values()) {
         if (param.getKey() == name)
            return param;
      }
      return null;
   }

   public int getId() {
      return id;
   }

   public String getKey() {
      return key;
   }

   public Class getClazz() {
      return this.clazz;
   }
}
