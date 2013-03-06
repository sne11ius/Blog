package nu.wasis.blog.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Entity;

@Entity("Posts")
public class Post extends AbstractDBObject {

    private String title;
    private String body;
    private User author;
    private Date date = new Date();

    private List<Comment> comments = new LinkedList<>();

    public Post() {
    }

    public Post(final String title, final String body, final User author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(final List<Comment> comments) {
        this.comments = comments;
    }

}
