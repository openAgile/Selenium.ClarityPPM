package com.versionone.selenium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ca.clarity.avee.api.InterfaceSettings;
import com.versionone.selenium.InterfaceSettingsImpl;


public class V1GrantTool {

	private static String C1_NAME = "ClaritySvc";
	private static String V1_URL;
	private static String V1_USERNAME;
	private static String V1_PASSWORD;
	private static String OAUTH_CLIENT_ID;
	private static String OAUTH_CLIENT_NAME;
	private static String OAUTH_CLIENT_SECRET;
	private static String OAUTH_REDIRECT_URI;
	private static String OAUTH_AUTH_URI;
	private static String OAUTH_TOKEN_URI;
	private static String OAUTH_SERVER_BASE_URI;
	private static String OAUTH_ACCESS_TOKEN;
	private static String OAUTH_REFRESH_TOKEN;
	private static String OAUTH_SCOPE = "apiv1 query-api-1.0 ~/notification.v1";
	private static String OAUTH_GRANT_CODE;

	/**
	 * Creates OAuth credentials and returns them as a SQL statement for use in the Clarity database.
	 */
	public static void createOAuthCredsSQL(String url, String username, String password, String createSQLFile) throws ClientProtocolException, IOException, JDOMException, ParseException, OAuthSystemException, OAuthProblemException {

		V1_URL = url;
		V1_USERNAME = username;
		V1_PASSWORD = password;

		createServiceMemberAccount();
		createPermittedApp();
		createAuthorization();
		createAccessTokens();

		if (Boolean.valueOf(createSQLFile)) {
			createSQLStatementAsFile(createSQLStatement());
		} else {
			System.out.println(createSQLStatement());
		}
	}

	/**
	 * Creates OAuth credentials and returns them as an InterfaceSettings object.
	 */
	public static InterfaceSettings createOAuthCredsInterfaceSettings(String url, String username, String password) throws ClientProtocolException, IOException, JDOMException, ParseException, OAuthSystemException, OAuthProblemException {

		V1_URL = url;
		V1_USERNAME = username;
		V1_PASSWORD = password;

		createServiceMemberAccount();
		createPermittedApp();
		createAuthorization();
		createAccessTokens();

		return createInterfaceSettings();
	}

	private static void createServiceMemberAccount() throws ClientProtocolException, IOException, JDOMException {
		String url = V1_URL + "/rest-1.v1/Data/Member";
		String query = "" +
					   "<Asset>\r\n" +
					   "    <Attribute name=\"Name\" act=\"set\">Clarity PPM Service</Attribute>\r\n" +
					   "    <Attribute name=\"Username\" act=\"set\">" + C1_NAME + "</Attribute>\r\n" +
					   "    <Attribute name=\"Password\" act=\"set\">" + C1_NAME + "</Attribute>\r\n" +
					   "    <Attribute name=\"Nickname\" act=\"set\">" + C1_NAME + "</Attribute>\r\n" +
					   "    <Attribute name=\"IsCollaborator\" act=\"set\">false</Attribute>\r\n" +
					   "    <Attribute name=\"NotifyViaEmail\" act=\"set\">false</Attribute>\r\n" +
					   "    <Attribute name=\"SendConversationEmails\" act=\"set\">false</Attribute>\r\n" +
					   "    <Relation name=\"DefaultRole\" act=\"set\"><Asset idref=\"Role:2\"/></Relation>\r\n" +
					   "    <Attribute name=\"Description\" act=\"set\">Service account used by the Clarity PPM Integration for VersionOne.</Attribute>\r\n" +
					   "</Asset>";
		String response = V1HttpClient.executePostQuery(url, query, V1_USERNAME, V1_PASSWORD, "application/xml");
		assignProjectPermissions(parseMemberOID(response));
	}

	public static String parseMemberOID(String response) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(response));
		Element rootNode = document.getRootElement();
		return rootNode.getAttributeValue("id");
	}

	private static void assignProjectPermissions(String memberOID) throws ClientProtocolException, IOException {
		String url = V1_URL + "/rest-1.v1/Data/Scope/0";
		String query = "" +
					   "<Asset>\r\n" +
					   "    <Relation name=\"Members\"><Asset idref=\"" + memberOID + "\" act=\"add\"/></Relation>\r\n" +
					   "</Asset>";
		V1HttpClient.executePostQuery(url, query, V1_USERNAME, V1_PASSWORD, "application/xml");
	}

	private static void createPermittedApp() throws ClientProtocolException, IOException, ParseException {
		String url = V1_URL + "/ClientRegistration.mvc/Register";
		String query = "" +
					   "{\r\n" +
					   " \"client_name\": \"Clarity PPM Integration\",\r\n" +
					   " \"client_type\": \"Public\",\r\n" +
					   " \"client_apikey\": \"\",\r\n" +
					   " \"redirect_uri\": \"urn:ietf:wg:oauth:2.0:oob\"\r\n" +
					   "}";
		String response = V1HttpClient.executePostQuery(url, query, C1_NAME, C1_NAME, "application/json");
		parseClientSecrets(response);
	}

	private static void parseClientSecrets(String response) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject secrets = (JSONObject)parser.parse(response);
		OAUTH_CLIENT_ID = (String)secrets.get("client_id");
		OAUTH_CLIENT_NAME = (String)secrets.get("client_name");
		OAUTH_REDIRECT_URI = (String)secrets.get("redirect_uri");
		OAUTH_CLIENT_SECRET = (String)secrets.get("client_secret");
		OAUTH_SERVER_BASE_URI = (String)secrets.get("server_base_uri");
		OAUTH_AUTH_URI = (String)secrets.get("auth_uri");
		OAUTH_TOKEN_URI = (String)secrets.get("token_uri");
	}

	private static void createAuthorization() throws OAuthSystemException, ClientProtocolException, IOException {
		String url = OAuthClientRequest
						.authorizationLocation(OAUTH_AUTH_URI)
						.setClientId(OAUTH_CLIENT_ID)
						.setRedirectURI(OAUTH_REDIRECT_URI)
						.setScope(OAUTH_SCOPE)
						.setResponseType("code")
						.buildQueryMessage()
						.getLocationUri();
		String response = V1HttpClient.executeGetQuery(url, C1_NAME, C1_NAME);
		grantAccess(parseAuthorizationCode(response));
	}

	private static String parseAuthorizationCode(String response) {
		Pattern pattern = Pattern.compile("<input type=\\\"hidden\\\" name=\\\"auth_request\\\" value=\\\"(.*)\\\"/>");
		Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	private static void grantAccess(String authCode) throws ClientProtocolException, IOException {
		String query = "allow=true&auth_request=" + URLEncoder.encode(authCode, "UTF-8");
		String response = V1HttpClient.executePostQuery(OAUTH_AUTH_URI, query, C1_NAME, C1_NAME, "application/x-www-form-urlencoded");
		OAUTH_GRANT_CODE = parseGrantSuccessCode(response);
	}

	private static String parseGrantSuccessCode(String response) {
		Pattern pattern = Pattern.compile("<textarea id=\"successcode\">(.*)</textarea>");
		Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	private static void createAccessTokens() throws OAuthSystemException, OAuthProblemException, ClientProtocolException, IOException {
		OAuthClientRequest request = OAuthClientRequest
						.tokenLocation(OAUTH_TOKEN_URI)
						.setGrantType(GrantType.AUTHORIZATION_CODE)
						.setClientId(OAUTH_CLIENT_ID)
						.setClientSecret(OAUTH_CLIENT_SECRET)
						.setRedirectURI(OAUTH_REDIRECT_URI)
						.setCode(OAUTH_GRANT_CODE)
						.buildBodyMessage();
		OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
		OAuthToken credentials = oauthClient.accessToken(request).getOAuthToken();
		OAUTH_ACCESS_TOKEN = credentials.getAccessToken();
		OAUTH_REFRESH_TOKEN = credentials.getRefreshToken();
	}

	private static String createSQLStatement() {
		return "" +
			 "USE niku;\r\n" +
			 "UPDATE niku.av_admin_settings\r\n" +
			 "SET	[AV_USERNAME] = NULL,\r\n" +
			 "	[AV_PWD] = NULL,\r\n" +
			 "	[FORCE_URL] = '" + OAUTH_SERVER_BASE_URI + "',\r\n" +
			 "	[ENDPOINT_URL] = '" + OAUTH_SERVER_BASE_URI + "',\r\n" +
			 "	[REMOTE_SECURITY_TYPE] = 'OAUTH',\r\n" +
			 "	[REMOTE_API_CLASS] = 'com.versionone.clarity.api.RemoteProviderAPIImpl',\r\n" +
			 "	[IS_ACTIVE] = 1,\r\n" +
			 "	[NAME] = 'VersionOne',\r\n" +
			 "	[CODE] = 'version_one',\r\n" +
			 "	[OAUTH_CLIENT_ID] = '" + OAUTH_CLIENT_ID + "',\r\n" +
			 "	[OAUTH_CLIENT_NAME] = '" + OAUTH_CLIENT_NAME + "',\r\n" +
			 "	[OAUTH_CLIENT_SECRET] = '" + OAUTH_CLIENT_SECRET + "',\r\n" +
			 "	[OAUTH_REDIRECT_URI] = '" + OAUTH_REDIRECT_URI + "',\r\n" +
			 "	[OAUTH_AUTH_URI] = '" + OAUTH_AUTH_URI + "',\r\n" +
			 "	[OAUTH_TOKEN_URI] = '" + OAUTH_TOKEN_URI + "',\r\n" +
			 "	[OAUTH_SERVER_BASE_URI] = '" + OAUTH_SERVER_BASE_URI + "',\r\n" +
			 "	[OAUTH_ACCESS_TOKEN] = '" + OAUTH_ACCESS_TOKEN + "',\r\n" +
			 "	[OAUTH_REFRESH_TOKEN] = '" + OAUTH_REFRESH_TOKEN + "',\r\n" +
			 "	[OAUTH_SCOPE] = '" + OAUTH_SCOPE + "'\r\n" +
			 "WHERE CODE = 'version_one';";
	}

	private static void createSQLStatementAsFile(String sql) {
		try {
            File file = new File("ClarityOAuthCredentials.sql");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(sql);
            output.close();
		} catch (IOException e) {
             e.printStackTrace();
        }
	}

	private static InterfaceSettings createInterfaceSettings() {
		InterfaceSettings settings = new InterfaceSettingsImpl();
		settings.setSetting(InterfaceSettings.OAUTH_CLIENT_ID, OAUTH_CLIENT_ID);
		settings.setSetting(InterfaceSettings.OAUTH_CLIENT_NAME, OAUTH_CLIENT_NAME);
		settings.setSetting(InterfaceSettings.OAUTH_CLIENT_SECRET, OAUTH_CLIENT_SECRET);
		settings.setSetting(InterfaceSettings.OAUTH_REDIRECT_URI, OAUTH_REDIRECT_URI);
		settings.setSetting(InterfaceSettings.OAUTH_AUTH_URI, OAUTH_AUTH_URI);
		settings.setSetting(InterfaceSettings.OAUTH_TOKEN_URI, OAUTH_TOKEN_URI);
		settings.setSetting(InterfaceSettings.OAUTH_SERVER_BASE_URI, OAUTH_SERVER_BASE_URI);
		settings.setSetting(InterfaceSettings.OAUTH_ACCESS_TOKEN, OAUTH_ACCESS_TOKEN);
		settings.setSetting(InterfaceSettings.OAUTH_REFRESH_TOKEN, OAUTH_REFRESH_TOKEN);
		settings.setSetting(InterfaceSettings.OAUTH_SCOPE, OAUTH_SCOPE);
		settings.setSetting(InterfaceSettings.CLARITY_ENDPOINT_URL, "http://localhost/niku");
		settings.setSetting(InterfaceSettings.CLARITY_UI_ENDPOINT, "/nu");
		settings.setSetting(InterfaceSettings.CLARITY_XOG_ENDPOINT, "/xog");
		settings.setSetting(InterfaceSettings.CLARITY_ODATA_ENDPOINT, "/odata");
		settings.setSetting(InterfaceSettings.USE_PROJECT_NAME_IF_AGILE_NAME_IS_NULL, "true");
		return settings;
	}

}
