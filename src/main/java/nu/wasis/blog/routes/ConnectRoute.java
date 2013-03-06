package nu.wasis.blog.routes;

import java.io.IOException;

import nu.wasis.util.GPlusUtils;
import nu.wasis.util.PrivateConstants;
import spark.Request;
import spark.Response;
import spark.Route;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;

public final class ConnectRoute extends Route {
    public ConnectRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        response.type("application/json");
        final String tokenData = request.session().attribute("token");
        if (tokenData != null) {
            // already connected
            response.status(400);
            return GPlusUtils.GSON.toJson("Current user is already connected.");
        }
        // Ensure that this is no request forgery going on, and that the user
        // sending us this connect request is the user that was supposed to.
        if (!request.queryParams("state").equals(request.session().attribute("state"))) {
            response.status(401);
            return GPlusUtils.GSON.toJson("Invalid state parameter.");
        }

        final String gPlusId = request.queryParams("gplus_id");
        final String code = request.body();

        try {
            // Upgrade the authorization code into an access and refresh token.
            final GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                                                                                              GPlusUtils.TRANSPORT,
                                                                                              GPlusUtils.JSON_FACTORY,
                                                                                              PrivateConstants.CLIENT_ID,
                                                                                              PrivateConstants.CLIENT_SECRET,
                                                                                              code, "postmessage").execute();
            // Create a credential representation of the token data.
            final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(GPlusUtils.JSON_FACTORY)
                                                                              .setTransport(GPlusUtils.TRANSPORT)
                                                                              .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                PrivateConstants.CLIENT_SECRET)
                                                                              .build()
                                                                              .setFromTokenResponse(tokenResponse);

            // Check that the token is valid.
            final Oauth2 oauth2 = new Oauth2.Builder(GPlusUtils.TRANSPORT, GPlusUtils.JSON_FACTORY, credential).build();
            final Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();
            // If there was an error in the token info, abort.
            if (tokenInfo.containsKey("error")) {
                response.status(401);
                return GPlusUtils.GSON.toJson(tokenInfo.get("error").toString());
            }
            // Make sure the token we got is for the intended user.
            if (!tokenInfo.getUserId().equals(gPlusId)) {
                response.status(401);
                return GPlusUtils.GSON.toJson("Token's user ID doesn't match given user ID.");
            }
            // Make sure the token we got is for our app.
            if (!tokenInfo.getIssuedTo().equals(PrivateConstants.CLIENT_ID)) {
                response.status(401);
                return GPlusUtils.GSON.toJson("Token's client ID does not match app's.");
            }
            // Store the token in the session for later use.
            request.session().attribute("token", tokenResponse.toString());
            return GPlusUtils.GSON.toJson("Successfully connected user.");
        } catch (final TokenResponseException e) {
            response.status(500);
            return GPlusUtils.GSON.toJson("Failed to upgrade the authorization code.");
        } catch (final IOException e) {
            response.status(500);
            return GPlusUtils.GSON.toJson("Failed to read token data from Google. " + e.getMessage());
        }
    }
}
