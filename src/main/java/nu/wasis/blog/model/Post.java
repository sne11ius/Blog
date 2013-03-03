package nu.wasis.blog.model;

import com.github.jmkgreen.morphia.annotations.Entity;

@Entity("Posts")
public class Post extends AbstractDBObject {

    private String title;
    private String body;
    private User author;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

}
