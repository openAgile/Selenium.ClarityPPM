package com.versionone.selenium;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ca.clarity.avee.api.InterfaceSettings;
import com.versionone.selenium.V1Exception;
import com.versionone.selenium.V1HttpClient;
import com.versionone.selenium.V1OAuthHttpClient;


/**
 * Used for manipulating V1 Workitems (Epics, Stories, Defects, Tasks, Tests), primarily for integration testing. 
 */
public class V1TestHelper {
	
	public enum Operation {
		Copy("Copy"),
		Delete("Delete"),
		Inactivate("Inactivate"),
		Close("QuickClose"),
		Reactivate("Reactivate"),
		Undelete("Undelete");
		
        private String value;
        private Operation(String value) {
            this.value = value;
        }
	}
	
	public static void executeOperation(V1OAuthHttpClient httpClient, String assetType, String assetOID, Operation operation) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception {
		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID) + "?op=" + operation.value;
		httpClient.executeQuery(url, "");
	}
	
	public static String getAssetOIDNumber(String assetOID) {
		return assetOID.substring(assetOID.indexOf(":") + 1, assetOID.length());
	}
	
	public static String parseAssetOIDFromMoment(String response) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(response));
		Element rootNode = document.getRootElement();
		String moment = rootNode.getAttributeValue("id");
		return moment.substring(0, moment.lastIndexOf(":"));
	}
	
	public static String parseAssetOID(String response) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(response));
		Element asset = document.getRootElement().getChild("Asset");
		String assetOID = asset.getAttributeValue("id");
		return assetOID;
	}
	
	public static String createChildEpic(V1OAuthHttpClient httpClient, String name, String parentOID, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Epic";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
					 "    <Attribute name=\"PlannedStart\" act=\"set\">" + startDate + "</Attribute>\r\n" + 
					 "    <Attribute name=\"PlannedEnd\" act=\"set\">" + endDate + "</Attribute>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String updateChildEpic(V1OAuthHttpClient httpClient, String name, String assetOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Epic/" + getAssetOIDNumber(assetOID);
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String updateWorkitemScope(V1OAuthHttpClient httpClient, String assetType, String assetOID, String scopeOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID);
		String xml = "" +
					 "<Asset>\r\n" + 
					 "		<Relation name=\"Scope\" act=\"set\"><Asset idref=\"" + scopeOID + "\"/></Relation>" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}

	public static String createChildStory(V1OAuthHttpClient httpClient, String name, String parentOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Story";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String createChildDefect(V1OAuthHttpClient httpClient, String name, String parentOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Defect";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}	
	
	public static String createScopeWithDates(V1OAuthHttpClient httpClient, String name, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Scope";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Parent\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Scheme\" act=\"set\"><Asset idref=\"" + getDefaultScheme(httpClient) + "\"/></Relation>\r\n" + 
					 "    <Attribute name=\"BeginDate\" act=\"set\">" + startDate + "</Attribute>\r\n" + 
					 "    <Attribute name=\"EndDate\" act=\"set\">" + endDate + "</Attribute>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String getDefaultScheme(V1OAuthHttpClient httpClient) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Scheme?where=Name=%27Default%20Scheme%27";
		String response = httpClient.executeQuery(url, null);
		return parseAssetOID(response);
	}
	
	public static String createTimebox(V1OAuthHttpClient httpClient, String name, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Timebox";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Attribute name=\"BeginDate\" act=\"set\">" + startDate + "</Attribute>\r\n" + 
					 "    <Attribute name=\"EndDate\" act=\"set\">" + endDate + "</Attribute>\r\n" + 
					 "    <Relation name=\"State\" act=\"set\"><Asset idref=\"State:101\"/></Relation>\r\n" + 
					 "    <Relation name=\"Schedule\" act=\"set\"><Asset idref=\"" + getDefaultScheduleOID(httpClient.getInterfaceSettings().getSetting(InterfaceSettings.OAUTH_SERVER_BASE_URI)) + "\"/></Relation>\r\n" + 
					 "</Asset>";

		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}	
	
	public static String updateWorkitemTimebox(V1OAuthHttpClient httpClient, String assetType, String assetOID, String timeboxOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID);
		String xml = "" +
					 "<Asset>\r\n" + 
					 "		<Relation name=\"Timebox\" act=\"set\"><Asset idref=\"" + timeboxOID + "\"/></Relation>" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String createLink(V1OAuthHttpClient httpClient, String assetOID, String name, String linkUrl) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Link";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Relation name=\"Asset\" act=\"set\"><Asset idref=\"" + assetOID + "\"/></Relation>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Attribute name=\"OnMenu\" act=\"set\">True</Attribute>\r\n" + 
					 "    <Attribute name=\"URL\" act=\"set\">" + linkUrl + "</Attribute>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static String createActual(V1OAuthHttpClient httpClient, String assetOID, String date, String value) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
		String url = "/rest-1.v1/Data/Actual";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Date\" act=\"set\">" + date + "</Attribute>\r\n" + 
					 "    <Relation name=\"Member\" act=\"set\"><Asset idref=\"Member:20\"/></Relation>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Attribute name=\"Value\" act=\"set\">" + value + "</Attribute>\r\n" + 
					 "    <Relation name=\"Workitem\" act=\"set\"><Asset idref=\"" + assetOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = httpClient.executeQuery(url, xml);
		return parseAssetOIDFromMoment(response);
	}
	
	public static void injectConfigurations(String instanceUrl) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception {
		String url = instanceUrl + "/ui.v1?gadget=Gadgets/Admin/Configuration/SystemConfig";
		String urlToInject = instanceUrl.substring(0, instanceUrl.lastIndexOf("/"));
		String payload = "" +
						 "{\r\n" + 
						 "  \"gadget\": \"\\/Gadgets\\/Admin\\/Configuration\\/SystemConfig\",\r\n" + 
						 "  \"Settings\": {\r\n" + 
						 "  },\r\n" + 
						 "  \"id\": \"_hnkjsvr\",\r\n" + 
						 "  \"ContextManager\": {\r\n" + 
						 "    \"PrimaryScopeContext\": \"Scope:0\",\r\n" + 
						 "    \"ScopeRollup\": true,\r\n" + 
						 "    \"AssetContext\": \"NULL\",\r\n" + 
						 "    \"AssetListContext\": \"\",\r\n" + 
						 "    \"Bubble\": \"\",\r\n" + 
						 "    \"ScopeLabel\": \"-\"\r\n" + 
						 "  },\r\n" + 
						 "  \"ConfigureSystem\": {\r\n" + 
						 "    \"License\": {\r\n" + 
						 "      \"BuildProject,BuildRun\": false,\r\n" + 
						 "      \"Capacity\": false,\r\n" + 
						 "      \"ChangeSet\": true,\r\n" + 
						 "      \"Defect\": true,\r\n" + 
						 "      \"Goal\": true,\r\n" + 
						 "      \"Issue\": true,\r\n" + 
						 "      \"Request\": true,\r\n" + 
						 "      \"Retrospective\": true,\r\n" + 
						 "      \"Team\": true\r\n" + 
						 "    },\r\n" + 
						 "    \"NamedFeature\": {\r\n" + 
						 "      \"ProjectWorkspaces\": false,\r\n" + 
						 "      \"GuestCollaborators\": false\r\n" + 
						 "    },\r\n" + 
						 "    \"TrackingLevel\": {\r\n" + 
						 "      \"Story\": \"Story:Mix\",\r\n" + 
						 "      \"Defect\": \"Defect:Mix\"\r\n" + 
						 "    },\r\n" + 
						 "    \"TrackEffort\": true,\r\n" + 
						 "    \"TerminologyOverrides\": {\r\n" + 
						 "      \"Story\": \"\",\r\n" + 
						 "      \"StoryPlural\": \"\",\r\n" + 
						 "      \"Iteration\": \"\",\r\n" + 
						 "      \"IterationPlural\": \"\"\r\n" + 
						 "    }\r\n" + 
						 "  },\r\n" + 
						 "  \"SystemOptions\": {\r\n" + 
						 "    \"AllowSelfServicePasswordReset\": true,\r\n" + 
						 "    \"TimeZoneSelection\": \"UTC\",\r\n" + 
						 "    \"ServerBaseUrl\": \"" + urlToInject + "\"\r\n" + 
						 "  },\r\n" + 
						 "  \"Apply\": true\r\n" + 
						 "}";
		V1HttpClient.executePostQuery(url, payload, "admin", "admin", "application/json; charset=UTF-8");
	}
	
	public static void injectSchedule(String instanceUrl) throws ClientProtocolException, IOException, JDOMException {
		String url = instanceUrl + "/rest-1.v1/Data/Scope/0";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Relation name=\"Schedule\" act=\"set\"><Asset idref=\"" + getDefaultScheduleOID(instanceUrl) + "\"/></Relation>\r\n" + 
					 "</Asset>";
		V1HttpClient.executePostQuery(url, xml, "admin", "admin", "application/xml");
	}
	
	public static void injectAdminEmailAddress(String instanceUrl) throws ClientProtocolException, IOException {
		String url = instanceUrl + "/rest-1.v1/Data/Member/20";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Email\" act=\"set\">admin@versionone.com</Attribute>\r\n" + 
					 "</Asset>";
		V1HttpClient.executePostQuery(url, xml, "admin", "admin", "application/xml");
	}
	
	public static String getDefaultScheduleOID(String instanceUrl) throws ClientProtocolException, IOException, JDOMException {
		String url = instanceUrl + "/rest-1.v1/Data/Schedule?where=Name=%27Default%20Schedule%27";
		String response = V1HttpClient.executeGetQuery(url, "admin", "admin");
		return parseAssetOID(response);
	}
	
}
