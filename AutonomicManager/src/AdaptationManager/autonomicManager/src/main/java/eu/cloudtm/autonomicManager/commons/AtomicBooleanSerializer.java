package eu.cloudtm.autonomicManager.commons;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 8/31/13
 */
public class AtomicBooleanSerializer implements JsonSerializer<AtomicBoolean> {

    @Override
    public JsonElement serialize(AtomicBoolean atomicBoolean, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(atomicBoolean.get());
    }
}
