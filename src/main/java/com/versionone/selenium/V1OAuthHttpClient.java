package com.versionone.selenium;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.json.JSONException;

import com.ca.clarity.avee.api.InterfaceSettings;
import com.niku.union.log.NikuLogger;
import com.versionone.selenium.InterfaceSettingsImpl;

/**
 * Used to make VersionOne API calls using OAuth2 authentication.
 */
public class V1OAuthHttpClient {

	private static final NikuLogger logger = NikuLogger.getLogger(V1OAuthHttpClient.class);
	private final String USER_AGENT = "clarity-versionone-connector/1.0";
	private OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
	private InterfaceSettingsImpl settings;
	private OAuthToken credentials;

	/**
	 * Sets the interface settings and OAuthToken credentials.
	 *
	 * @param settings The interface settings passed from Clarity PPM.
	 * @throws JSONException
	 * @throws IOException
	 * @throws V1Exception
	 */
	public V1OAuthHttpClient(InterfaceSettings settings) throws JSONException, IOException, V1Exception {
		this.settings = new InterfaceSettingsImpl(settings);
		credentials = GetSettingsToken();
	}

	/**
	 * Creates the OAuthToken credentials.
	 *
	 * @return OAuthToken The basic OAuth credentials object.
	 */
	private OAuthToken GetSettingsToken() {
		return new BasicOAuthToken(settings.getSetting(InterfaceSettings.OAUTH_ACCESS_TOKEN),
				600L,
				settings.getSetting(InterfaceSettings.OAUTH_REFRESH_TOKEN),
				settings.getSetting(InterfaceSettings.OAUTH_SCOPE));
	}

	/**
	 * Executes a V1 API query given the endpoint to use and optional request body.
	 *
	 * @param queryUrl The V1 API query url including the endpoint.
	 * @param body (Optional) The body of the HTTP request. If provided, an HTTP POST is used, if not, an HTTP GET is used.
	 * @return OAuthResourceResponse The HTTP response.
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws V1Exception
	 */
	public String executeQuery(String queryUrl, String body) throws OAuthSystemException, OAuthProblemException, UnsupportedEncodingException, IOException, V1Exception {

		String fullQueryUrl = settings.getSetting(InterfaceSettings.OAUTH_SERVER_BASE_URI) + queryUrl;

		String method = "GET";
		if (body != null) {
			method = "POST";
		}
		OAuthResourceResponse response = tryHTTPRequest(method, fullQueryUrl, body);

		String returnBody = "";
        Integer status = response.getResponseCode();

        //Good response - return the HTTP response body.
        if (status == 200) {
			returnBody = response.getBody();
        }

        else if (status == 400) {
            throw new V1Exception("HTTP 400 ERROR: Bad request. " + response.getBody());
        }

        //Attempt to refresh the access token.
        else if (status == 401) {
				logger.debug("Refreshing access token...");
				OAuthClientRequest request = OAuthClientRequest
						.tokenLocation(settings.getSetting(InterfaceSettings.OAUTH_TOKEN_URI))
						.setGrantType(GrantType.REFRESH_TOKEN)
						.setClientId(settings.getSetting(InterfaceSettings.OAUTH_CLIENT_ID))
						.setClientSecret(settings.getSetting(InterfaceSettings.OAUTH_CLIENT_SECRET))
						.setRedirectURI(settings.getSetting(InterfaceSettings.OAUTH_REDIRECT_URI))
						.setRefreshToken(credentials.getRefreshToken())
						.buildBodyMessage();
				credentials = oAuthClient.accessToken(request).getOAuthToken();
				logger.debug(String.format("Access token refreshed.", response.getResponseCode()));

				//Re-try the original request.
				response = tryHTTPRequest(method, fullQueryUrl, body);
				status = response.getResponseCode();

				if (status == 401){
					throw new V1Exception("HTTP 401 ERROR: Unauthorized. The access tokens may have expired. " + response.getBody());
				}
				returnBody = response.getBody();
        }

        else if (status == 500) {
            throw new V1Exception("HTTP 500 ERROR: Internal server error. The URL may not be correct. " + response.getBody());
        }

        else if (status == 404) {
            throw new V1Exception("HTTP 404 ERROR: Resource not found. The requested item may not exist. " + response.getBody());
        }

        else{
            throw new V1Exception("HTTP " + status + " ERROR: Problem getting data. " + response.getBody());
        }

		return returnBody;
	}

	/**
	 * Executes the HTTP request.
	 *
	 * @param method HTTP GET or POST
	 * @param fullQueryUrl The V1 API query url including the endpoint.
	 * @param body (Optional) The body of the HTTP request.
	 * @return OAuthResourceResponse The HTTP response.
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	private OAuthResourceResponse tryHTTPRequest(String method, String fullQueryUrl, String body) throws OAuthSystemException, OAuthProblemException {

		logger.debug(String.format("Query method: %s", method));
		logger.debug(String.format("Query url: %s", fullQueryUrl));
		logger.debug(String.format("Query body: %s", body));

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(fullQueryUrl)
			.setAccessToken(credentials.getAccessToken())
			.buildHeaderMessage();

		if (body != null) {
			bearerClientRequest.setBody(body);
		}

		bearerClientRequest.setHeader("User-Agent", USER_AGENT);
		OAuthResourceResponse response = oAuthClient.resource(bearerClientRequest, method, OAuthResourceResponse.class);

		logger.debug(String.format("Http response code: %d", response.getResponseCode()));
		logger.debug(String.format("Http response body: %s", response.getBody()));

		return response;
	}

	/**
	 * Provides access to the interface settings.
	 *
	 * @return InterfaceSettings The interface settings object.
	 */
	public InterfaceSettings getInterfaceSettings() {
		logger.trace("Called getInterfaceSettings.");
		return this.settings;
	}

}
