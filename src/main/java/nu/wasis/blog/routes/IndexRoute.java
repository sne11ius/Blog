package nu.wasis.blog.routes;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import nu.wasis.blog.Blog;
import nu.wasis.service.PostService;
import nu.wasis.util.GPlusUtils;
import nu.wasis.util.PrivateConstants;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public final class IndexRoute extends Route {

    private static final Logger LOG = Logger.getLogger(IndexRoute.class);

    public IndexRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        final Configuration cfg = createConfig();
        final String state = new BigInteger(130, new SecureRandom()).toString(32);
        request.session().attribute("state", state);
        final StringWriter writer = new StringWriter();
        try {
            final Template template = cfg.getTemplate("index.ftl");
            final Map<String, Object> map = createTemplateMap(state, request);
            template.process(map, writer);
        } catch (final IOException e) {
            LOG.error(e);
        } catch (final TemplateException e) {
            LOG.error(e);
        }
        return writer;
    }

    private Map<String, Object> createTemplateMap(final String state, final Request request) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("posts", PostService.INSTANCE.getPosts());
        map.put("client_id", PrivateConstants.CLIENT_ID);
        map.put("state", state);
        map.put("nickname", GPlusUtils.getCurrentUsername(request));
        map.put("loggedin", GPlusUtils.isLoggedIn(request));
        map.put("isowner", GPlusUtils.isOwnerLoggedIn(request));
        return map;
    }

    private Configuration createConfig() {
        final Configuration config = new Configuration();
        config.setClassForTemplateLoading(Blog.class, "/templates");
        config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        return config;
    }

}
