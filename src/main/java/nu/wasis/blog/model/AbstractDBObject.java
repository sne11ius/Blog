package nu.wasis.blog.model;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Id;
import com.google.gson.Gson;

public class AbstractDBObject {

    @Id
    private ObjectId id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
