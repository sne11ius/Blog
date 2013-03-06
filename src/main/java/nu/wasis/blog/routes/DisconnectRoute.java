package nu.wasis.blog.routes;

import java.io.IOException;

import nu.wasis.util.GPlusUtils;
import nu.wasis.util.PrivateConstants;
import spark.Request;
import spark.Response;
import spark.Route;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;

public final class DisconnectRoute extends Route {
    public DisconnectRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        response.type("application/json");
        final String tokenData = request.session().attribute("token");
        if (tokenData == null) {
            // not connected
            response.status(401);
            return GPlusUtils.GSON.toJson("Current user not connected.");
        }
        try {
            // Build credential from stored token data.
            final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(GPlusUtils.JSON_FACTORY)
                                                                              .setTransport(GPlusUtils.TRANSPORT)
                                                                              .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                PrivateConstants.CLIENT_SECRET)
                                                                              .build()
                                                                              .setFromTokenResponse(GPlusUtils.JSON_FACTORY.fromString(tokenData,
                                                                                                                                       GoogleTokenResponse.class));
            // Execute HTTP GET request to revoke current token.
            GPlusUtils.TRANSPORT.createRequestFactory()
                                .buildGetRequest(new GenericUrl(
                                                                String.format("https://accounts.google.com/o/oauth2/revoke?token=%s",
                                                                              credential.getAccessToken()))).execute();
            // Reset the user's session.
            request.session().removeAttribute("token");
            return GPlusUtils.GSON.toJson("Successfully disconnected.");
        } catch (final IOException e) {
            response.status(400);
            return GPlusUtils.GSON.toJson("Failed to revoke token for given user.");
        }
    }
}
