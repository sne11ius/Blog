package nu.wasis.blog.model;

import java.util.Date;

import com.github.jmkgreen.morphia.annotations.Embedded;

@Embedded
public class Comment {

    private User user;
    private String body;

    private Date date = new Date();

    public Comment() {
        // empty d'tor is empty - for bean stuff
    }

    public Comment(final User user, final String body) {
        super();
        this.user = user;
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

}
