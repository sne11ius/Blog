package nu.wasis.blog.routes;

import nu.wasis.service.PostService;
import nu.wasis.util.GPlusUtils;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetPostsRoute extends Route {

    public GetPostsRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        return GPlusUtils.GSON.toJson(PostService.INSTANCE.getPosts());
    }
}
