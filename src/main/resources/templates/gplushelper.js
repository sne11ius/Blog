var helper = (function() {
    var authResult = undefined;

    return {
        /**
         * Connects the server-side app after the user successfully signs in.
         *
         * @param {Object} authResult An Object which contains the access token and
         *   other authentication information.
         */
        onSignInCallback : function(authResult) {
            if (authResult['access_token']) {
                // The user is signed in
                this.authResult = authResult;
                // After we load the Google+ API, render the profile data from Google+.
                gapi.client.load('plus', 'v1', this.renderProfile);
            }
        },
        /**
         * Retrieves and renders the authenticated user's Google+ profile.
         */
        renderProfile : function() {
            var request = gapi.client.plus.people.get({
                'userId' : 'me'
            });
            request.execute(function(profile) {
                helper.connectServer(profile.id);
            });
        },
        /**
         * Calls the server endpoint to disconnect the app for the user.
         */
        disconnectServer : function() {
            $.ajax({
                type : 'POST',
                url : window.location.href + '/disconnect',
                success : function(result) {
                    window.location.reload();
                }
            });
        },
        connectServer : function(gplusId) {
            $.ajax({
                type : 'POST',
                url : window.location.href + '/connect?state=${state}&gplus_id=' + gplusId,
                contentType : 'application/octet-stream; charset=utf-8',
                success : function(result) {
                    window.location.reload();
                },
                data : this.authResult.code
            });
        }
    };
})();

/**
 * Calls the helper method that handles the authentication flow.
 *
 * @param {Object} authResult An Object which contains the access token and
 *   other authentication information.
 */
function onSignInCallback(authResult) {
    helper.onSignInCallback(authResult);
}
