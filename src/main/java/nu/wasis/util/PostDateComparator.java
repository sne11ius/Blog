package nu.wasis.util;

import java.util.Comparator;

import nu.wasis.blog.model.Post;

public class PostDateComparator implements Comparator<Post> {

    @Override
    public int compare(final Post post1, final Post post2) {
        return post1.getDate().compareTo(post2.getDate()) * -1;
    }

}
