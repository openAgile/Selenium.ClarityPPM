package com.versionone.selenium;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Used for manipulating V1 Workitems (Epics, Stories, Defects, Tasks, Tests), primarily for integration testing. 
 */
public class V1TestHelper {
	
//	public enum Operation {
//		Copy("Copy"),
//		Delete("Delete"),
//		Inactivate("Inactivate"),
//		Close("QuickClose"),
//		Reactivate("Reactivate"),
//		Undelete("Undelete");
//		
//        private String value;
//        private Operation(String value) {
//            this.value = value;
//        }
//	}
	
//	public static void executeOperation(V1OAuthHttpClient httpClient, String assetType, String assetOID, Operation operation) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception {
//		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID) + "?op=" + operation.value;
//		httpClient.executeQuery(url, "");
//	}
	
	public static String getAssetOIDNumber(String assetOID) {
		return assetOID.substring(assetOID.indexOf(":") + 1, assetOID.length());
	}
	
	private static String parseAssetOID(String response) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(response));
		Element rootNode = document.getRootElement();
		String moment = rootNode.getAttributeValue("id");
		return moment.substring(0, moment.lastIndexOf(":"));
	}
	
	public static String getAssetOID(String instance, String assetType, String assetNumber) throws ClientProtocolException, IOException, ParseException {
		String url = instance + "/query.v1";
		String response = V1HttpClient.executePostQuery(url, createGetAssetOIDQuery(assetType, assetNumber), "admin", "admin", "application/json");
		return parseGetAssetOIDResponse(response);
	}
	
	public static String getAssetOIDByName(String instance, String assetType, String assetName) throws ClientProtocolException, IOException, ParseException {
		String url = instance + "/query.v1";
		String query = "" +
				"from: " + assetType + "\r\n" + 
				"where:\r\n" + 
				"  Name: " + assetName;
		
		String response = V1HttpClient.executePostQuery(url, query, "admin", "admin", "application/json");
		return parseGetAssetOIDResponse(response);
	}
	
	public static String parseGetAssetOIDResponse(String response) throws org.json.simple.parser.ParseException {

		JSONParser parser = new JSONParser();
		JSONArray resultsets = (JSONArray)parser.parse(response);
		JSONArray resultset = (JSONArray)resultsets.get(0);

		if (resultset.size() == 0) return null;

		JSONObject result = (JSONObject)resultset.get(0);
		return (String)result.get("_oid");
	}
	
	public static String createGetAssetOIDQuery(String assetType, String assetNumber) {
		return "" +
		"from: " + assetType + "\r\n" + 
		"where:\r\n" + 
		" Number: " + assetNumber;
	}
	
	public static String createChildEpic(String instance, String name, String parentOID) throws ClientProtocolException, IOException, JDOMException {
		String url = instance + "/rest-1.v1/Data/Epic";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = V1HttpClient.executePostQuery(url, xml, "admin", "admin", "application/xml");
		return parseAssetOID(response);
	}
	
	public static String createActual(String instance, String assetOID, String memberOID, String date, String value) throws UnsupportedEncodingException, IOException, JDOMException {
		String url = instance + "/rest-1.v1/Data/Actual";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Date\" act=\"set\">" + date + "</Attribute>\r\n" + 
					 "    <Relation name=\"Member\" act=\"set\"><Asset idref=\"" + memberOID + "\"/></Relation>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Attribute name=\"Value\" act=\"set\">" + value + "</Attribute>\r\n" + 
					 "    <Relation name=\"Workitem\" act=\"set\"><Asset idref=\"" + assetOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = V1HttpClient.executePostQuery(url, xml, "admin", "admin", "application/xml");
		return parseAssetOID(response);
	}
	
//	public static String createActual(V1OAuthHttpClient httpClient, String assetOID, String date, String value) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Actual";
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Attribute name=\"Date\" act=\"set\">" + date + "</Attribute>\r\n" + 
//					 "    <Relation name=\"Member\" act=\"set\"><Asset idref=\"Member:20\"/></Relation>\r\n" + 
//					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
//					 "    <Attribute name=\"Value\" act=\"set\">" + value + "</Attribute>\r\n" + 
//					 "    <Relation name=\"Workitem\" act=\"set\"><Asset idref=\"" + assetOID + "\"/></Relation>\r\n" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//	
//	public static String updateChildEpic(V1OAuthHttpClient httpClient, String name, String assetOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Epic/" + getAssetOIDNumber(assetOID);
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//	
//	public static String updateWorkitemScope(V1OAuthHttpClient httpClient, String assetType, String assetOID, String scopeOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID);
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "		<Relation name=\"Scope\" act=\"set\"><Asset idref=\"" + scopeOID + "\"/></Relation>" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//
	public static String createChildStory(String instance, String name, String parentOID) throws UnsupportedEncodingException, IOException, JDOMException {
		String url = instance + "/rest-1.v1/Data/Story";
		String xml = "" +
					 "<Asset>\r\n" + 
					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
					 "</Asset>";
		String response = V1HttpClient.executePostQuery(url, xml, "admin", "admin", "application/xml");
		return parseAssetOID(response);
	}
//	
//	public static String createChildDefect(V1OAuthHttpClient httpClient, String name, String parentOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Defect";
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
//					 "    <Relation name=\"Scope\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
//					 "    <Relation name=\"Super\" act=\"set\"><Asset idref=\"" + parentOID + "\"/></Relation>\r\n" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}	
//	
//	public static String createScopeWithDates(V1OAuthHttpClient httpClient, String name, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Scope";
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
//					 "    <Relation name=\"Parent\" act=\"set\"><Asset idref=\"Scope:0\"/></Relation>\r\n" + 
//					 "    <Relation name=\"Scheme\" act=\"set\"><Asset idref=\"Scheme:1001\"/></Relation>\r\n" + 
//					 "    <Attribute name=\"BeginDate\" act=\"set\">" + startDate + "</Attribute>\r\n" + 
//					 "    <Attribute name=\"EndDate\" act=\"set\">" + endDate + "</Attribute>\r\n" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//	
//	public static String createTimebox(V1OAuthHttpClient httpClient, String name, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Timebox";
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
//					 "    <Attribute name=\"BeginDate\" act=\"set\">" + startDate + "</Attribute>\r\n" + 
//					 "    <Attribute name=\"EndDate\" act=\"set\">" + endDate + "</Attribute>\r\n" + 
//					 "    <Relation name=\"State\" act=\"set\"><Asset idref=\"State:101\"/></Relation>\r\n" + 
//					 "    <Relation name=\"Schedule\" act=\"set\"><Asset idref=\"Schedule:1000\"/></Relation>\r\n" + 
//					 "</Asset>";
//
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}	
//	
//	public static String updateWorkitemTimebox(V1OAuthHttpClient httpClient, String assetType, String assetOID, String timeboxOID) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/" + assetType + "/" + getAssetOIDNumber(assetOID);
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "		<Relation name=\"Timebox\" act=\"set\"><Asset idref=\"" + timeboxOID + "\"/></Relation>" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//	
//	public static String createLink(V1OAuthHttpClient httpClient, String assetOID, String name, String linkUrl) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//		String url = "/rest-1.v1/Data/Link";
//		String xml = "" +
//					 "<Asset>\r\n" + 
//					 "    <Relation name=\"Asset\" act=\"set\"><Asset idref=\"" + assetOID + "\"/></Relation>\r\n" + 
//					 "    <Attribute name=\"Name\" act=\"set\">" + name + "</Attribute>\r\n" + 
//					 "    <Attribute name=\"OnMenu\" act=\"set\">True</Attribute>\r\n" + 
//					 "    <Attribute name=\"URL\" act=\"set\">" + linkUrl + "</Attribute>\r\n" + 
//					 "</Asset>";
//		String response = httpClient.executeQuery(url, xml);
//		return parseAssetOID(response);
//	}
//	

	
}
