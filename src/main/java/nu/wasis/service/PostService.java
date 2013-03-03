package nu.wasis.service;

import java.util.List;

import nu.wasis.blog.model.Post;
import nu.wasis.util.Constants;
import nu.wasis.util.MongoUtils;

import org.apache.commons.lang.NotImplementedException;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;

public class PostService {

    final Datastore ds = new Morphia().createDatastore(MongoUtils.getMongo(), Constants.DB_NAME);

    public List<Post> getPosts() {
        return ds.find(Post.class).asList();
    }

    public Post getPost(final String postId) {
        throw new NotImplementedException();
    }

    public void addPost(final Post post) {
        ds.save(post);
    }

    public void deletePost(final Post post) {
        ds.delete(post);
    }

}
