package nu.wasis.blog;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import nu.wasis.util.PrivateConstants;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

final class IndexRoute extends Route {
    private static final Logger LOG = Logger.getLogger(IndexRoute.class);

    IndexRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        final Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(Blog.class, "/templates");
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        final String state = new BigInteger(130, new SecureRandom()).toString(32);
        request.session().attribute("state", state);
        final String nickname = getCurrentUsername(request);
        final StringWriter writer = new StringWriter();
        try {
            final Template template = cfg.getTemplate("index.ftl");
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("posts", Blog.postService.getPosts());
            map.put("client_id", PrivateConstants.CLIENT_ID);
            map.put("state", state);
            map.put("nickname", nickname);
            map.put("loggedin", isLoggedIn(request));
            template.process(map, writer);
        } catch (final IOException e) {
            Blog.LOG.error(e);
        } catch (final TemplateException e) {
            Blog.LOG.error(e);
        }
        return writer;
    }

    private boolean isLoggedIn(final Request request) {
        return null != request.session().attribute("token");
    }

    private String getCurrentUsername(final Request request) {
        final String tokenData = request.session().attribute("token");
        if (tokenData == null) {
            LOG.error("Not logged in.");
            return "[unknown]";
        }
        try {
            final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(Blog.JSON_FACTORY)
                                                                              .setTransport(Blog.TRANSPORT)
                                                                              .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                PrivateConstants.CLIENT_SECRET)
                                                                              .build()
                                                                              .setFromTokenResponse(Blog.JSON_FACTORY.fromString(tokenData,
                                                                                                                                 GoogleTokenResponse.class));
            final Plus service = new Plus.Builder(Blog.TRANSPORT, Blog.JSON_FACTORY, credential).setApplicationName(PrivateConstants.APPLICATION_NAME)
                                                                                                .build();
            final Person me = service.people().get("me").execute();
            return me.getDisplayName();
        } catch (final IOException e) {
            LOG.error(e);
            return "[unknown]";
        }
    }
}
