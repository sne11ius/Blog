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

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

}
