package nu.wasis.blog.routes;

import nu.wasis.service.PostService;
import nu.wasis.util.GPlusUtils;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;

public class GetSinglePostRoute extends Route {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(GetSinglePostRoute.class);

    public GetSinglePostRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        try {
            final String id = request.params(":postId");
            return GPlusUtils.GSON.toJson(PostService.INSTANCE.getPost(id));
        } catch (final Exception e) {
            response.status(404);
            return null;
        }
    }
}
