package nu.wasis.util;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

public class MongoUtils {

    private static final Logger LOG = Logger.getLogger(MongoUtils.class);

    private static Mongo mongoInstance;

    public static synchronized Mongo getMongo() {
        if (null == mongoInstance) {
            try {
                mongoInstance = new Mongo();
            } catch (final UnknownHostException e) {
                LOG.error(e);
                throw new RuntimeException(e);
            }
        }
        return mongoInstance;
    }

}
