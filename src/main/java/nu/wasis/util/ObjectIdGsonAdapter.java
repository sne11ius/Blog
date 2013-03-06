package nu.wasis.util;

import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

// from https://github.com/greenlaw110/play-morphia/blob/master/src/play/modules/morphia/utils/ObjectIdGsonAdapter.java
public class ObjectIdGsonAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                                                                                                                     throws JsonParseException {
        return new ObjectId(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(final ObjectId src, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

}
