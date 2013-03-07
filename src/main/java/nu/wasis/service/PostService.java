package nu.wasis.service;

import java.util.Collections;
import java.util.List;

import nu.wasis.blog.model.Comment;
import nu.wasis.blog.model.Post;
import nu.wasis.util.Constants;
import nu.wasis.util.MongoUtils;
import nu.wasis.util.PostDateComparator;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;

public class PostService {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(PostService.class);

    final Datastore ds = new Morphia().createDatastore(MongoUtils.getMongo(), Constants.DB_NAME);

    public static final PostService INSTANCE = new PostService();

    private PostService() {
        // singleton
    }

    public List<Post> getPosts() {
        final List<Post> allPosts = ds.find(Post.class).asList();
        Collections.sort(allPosts, new PostDateComparator());
        return allPosts;
    }

    public Post getPost(final String postId) {
        return ds.get(Post.class, new ObjectId(postId));
    }

    public void save(final Post post) {
        ds.save(post);
    }

    public void deletePost(final Post post) {
        ds.delete(post);
    }

    public void deleteAllPosts() {
        for (final Post post : getPosts()) {
            deletePost(post);
        }
    }

    public void addComment(final String postId, final Comment comment) {
        final Post post = getPost(postId);
        post.getComments().add(comment);
        save(post);
    }

}
