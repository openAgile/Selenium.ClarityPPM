package com.versionone.selenium;

public class V1QueryResponseMocks {
	
	public static String workBreakdownQueryResponse() {
		return "" +
	           "[\r\n" + 
	           "  [\r\n" + 
	           "    {\r\n" + 
	           "      \"_oid\": \"Epic:1587\",\r\n" + 
	           "      \"Name\": \"Mobile Helpdesk Application\",\r\n" + 
	           "      \"Number\": \"E-01058\",\r\n" + 
	           "      \"PlannedStart\": \"2014-01-02T00:00:00.0000000\",\r\n" + 
	           "      \"PlannedEnd\": \"2015-01-01T00:00:00.0000000\",\r\n" + 
	           "      \"CreateDate\": \"2014-04-05T00:33:13.5430000\",\r\n" + 
	           "      \"Subs:Epic\": [\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1588\",\r\n" + 
	           "          \"Name\": \"Push Notifications\",\r\n" + 
	           "          \"Number\": \"E-01059\",\r\n" + 
	           "          \"AssetType\": \"Epic\",\r\n" + 
	           "          \"AssetState\": \"Active\",\r\n" + 
	           "          \"CreateDate\": \"2014-04-05T00:33:46.4170000\",\r\n" + 
	           "          \"PlannedStart\": \"2014-04-02T00:00:00.0000000\",\r\n" + 
	           "          \"PlannedEnd\": \"2014-04-30T00:00:00.0000000\",\r\n" + 
	           "          \"Scope.BeginDate\": \"2014-04-05T00:00:00.0000000\",\r\n" + 
	           "          \"Scope.EndDate\": null\r\n" + 
	           "        }\r\n" + 
	           "      ],\r\n" + 
	           "      \"Subs:PrimaryWorkitem\": [\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Story:1623\",\r\n" + 
	           "          \"Name\": \"Test Story\",\r\n" + 
	           "          \"Number\": \"B-01032\",\r\n" + 
	           "          \"AssetType\": \"Story\",\r\n" + 
	           "          \"AssetState\": \"Deleted\",\r\n" + 
	           "          \"CreateDate\": \"2014-04-21T14:54:33.2830000\",\r\n" + 
	           "          \"Timebox.BeginDate\": null,\r\n" + 
	           "          \"Timebox.EndDate\": null,\r\n" + 
	           "          \"Scope.BeginDate\": \"2014-04-05T00:00:00.0000000\",\r\n" + 
	           "          \"Scope.EndDate\": null\r\n" + 
	           "        }\r\n" + 
	           "      ]\r\n" + 
	           "    }\r\n" + 
	           "  ]\r\n" + 
	           "]";
	}
	
	public static String timesheetsQueryResponse() {
		return "" +
	           "[\r\n" + 
	           "  [\r\n" + 
	           "    {\r\n" + 
	           "      \"_oid\": \"Epic:1587\",\r\n" + 
	           "      \"Name\": \"Mobile Helpdesk Application\",\r\n" + 
	           "      \"Number\": \"E-01058\",\r\n" + 
	           "      \"Subs:Workitem\": [\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1588\",\r\n" + 
	           "          \"Name\": \"Push Notifications\",\r\n" + 
	           "          \"Number\": \"E-01059\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": [\r\n" + 
	           "            {\r\n" + 
	           "              \"_oid\": \"Actual:1718\",\r\n" + 
	           "              \"Date\": \"2014-06-01T00:00:00.0000000\",\r\n" + 
	           "              \"Value\": \"4\",\r\n" + 
	           "              \"Member.Email\": \"andre.agile@company.com\"\r\n" + 
	           "            }\r\n" + 
	           "          ]\r\n" + 
	           "        },\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1597\",\r\n" + 
	           "          \"Name\": \"Ticket Generation\",\r\n" + 
	           "          \"Number\": \"E-01066\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": []\r\n" + 
	           "        }\r\n" + 
	           "      ]\r\n" + 
	           "    }\r\n" + 
	           "  ]\r\n" + 
	           "]";
	}
	
	public static String timesheetsQueryResponseWithNoWorkitems() {
		return "" +
	           "[\r\n" + 
	           "  [\r\n" + 
	           "    {\r\n" + 
	           "      \"_oid\": \"Epic:1587\",\r\n" + 
	           "      \"Name\": \"Mobile Helpdesk Application\",\r\n" + 
	           "      \"Number\": \"E-01058\",\r\n" + 
	           "      \"Subs:Workitem\": [\r\n" + 
	           "      ]\r\n" + 
	           "    }\r\n" + 
	           "  ]\r\n" + 
	           "]";
	}
	
	public static String timesheetsNullEmailQueryResponse() {
		return "" +
	           "[\r\n" + 
	           "  [\r\n" + 
	           "    {\r\n" + 
	           "      \"_oid\": \"Epic:1587\",\r\n" + 
	           "      \"Name\": \"Mobile Helpdesk Application\",\r\n" + 
	           "      \"Number\": \"E-01058\",\r\n" + 
	           "      \"Subs:Workitem\": [\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1588\",\r\n" + 
	           "          \"Name\": \"Push Notifications\",\r\n" + 
	           "          \"Number\": \"E-01059\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": [\r\n" + 
	           "            {\r\n" + 
	           "              \"_oid\": \"Actual:1718\",\r\n" + 
	           "              \"Date\": \"2014-06-01T00:00:00.0000000\",\r\n" + 
	           "              \"Value\": \"4\",\r\n" + 
	           "              \"Member.Email\":null\r\n" +  
	           "            }\r\n" + 
	           "          ]\r\n" + 
	           "        },\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1597\",\r\n" + 
	           "          \"Name\": \"Ticket Generation\",\r\n" + 
	           "          \"Number\": \"E-01066\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": []\r\n" + 
	           "        }\r\n" + 
	           "      ]\r\n" + 
	           "    }\r\n" + 
	           "  ]\r\n" + 
	           "]";

	}
	
	public static String timesheetsQueryResponseWithMultipleHours() {
		return "" +
	           "[\r\n" + 
	           "  [\r\n" + 
	           "    {\r\n" + 
	           "      \"_oid\": \"Epic:1587\",\r\n" + 
	           "      \"Name\": \"Mobile Helpdesk Application\",\r\n" + 
	           "      \"Number\": \"E-01058\",\r\n" + 
	           "      \"Subs:Workitem\": [\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1588\",\r\n" + 
	           "          \"Name\": \"Push Notifications\",\r\n" + 
	           "          \"Number\": \"E-01059\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": [\r\n" + 
	           "            {\r\n" + 
	           "              \"_oid\": \"Actual:1718\",\r\n" + 
	           "              \"Date\": \"2014-06-01T00:00:00.0000000\",\r\n" + 
	           "              \"Value\": \"4\",\r\n" + 
	           "              \"Member.Email\": \"andre.agile@company.com\"\r\n" + 
	           "            },\r\n" + 
	           "            {\r\n" + 
	           "              \"_oid\": \"Actual:1719\",\r\n" + 
	           "              \"Date\": \"2014-06-01T00:00:00.0000000\",\r\n" + 
	           "              \"Value\": \"4\",\r\n" + 
	           "              \"Member.Email\": \"andre.agile@company.com\"\r\n" + 
	           "            }\r\n" + 	           
	           "          ]\r\n" + 
	           "        },\r\n" + 
	           "        {\r\n" + 
	           "          \"_oid\": \"Epic:1597\",\r\n" + 
	           "          \"Name\": \"Ticket Generation\",\r\n" + 
	           "          \"Number\": \"E-01066\",\r\n" + 
	           "          \"SubsMeAndDown.ChildrenMeAndDown.Actuals\": []\r\n" + 
	           "        }\r\n" + 
	           "      ]\r\n" + 
	           "    }\r\n" + 
	           "  ]\r\n" + 
	           "]";
	}
	
	public static String getProjectQueryResponse() {
		return "" +
	           "[\r\n" + 
	           "[\r\n" + 
	           "{\r\n" + 
	           "\"_oid\":\"Epic:1587\",\r\n" + 
	           "\"Name\":\"Mobile Helpdesk Application\",\r\n" + 
	           "\"Number\":\"E-01058\",\r\n" + 
	           "\"Description\":\"This project is for an application that will allow helpdesk engineers to manage their tickets from their mobile phones.\",\r\n" + 
	           "\"Reference\":\"mobile_helpdesk_app\",\r\n" + 
	           "\"PlannedStart\":\"2014-01-01T00:00:00.0000000\",\r\n" + 
	           "\"PlannedEnd\":\"2015-01-01T00:00:00.0000000\",\r\n" + 
	           "\"CreateDate\":\"2014-04-05T00:33:13.5430000\",\r\n" + 
	           "\"AssetState\":\"Active\",\r\n" + 
	           "\"Links\":[\r\n" + 
	           "{\r\n" + 
	           "\"_oid\":\"Link:1915\",\r\n" + 
	           "\"Name\":\"mobile_helpdesk_app\"\r\n" + 
	           "},\r\n" + 
	           "{\r\n" + 
	           "\"_oid\":\"Link:1916\",\r\n" + 
	           "\"Name\":\"VersionOne\"\r\n" + 
	           "}\r\n" + 
	           "]\r\n" + 
	           "}\r\n" + 
	           "]\r\n" + 
	           "]";
	}
	
	public static String getQueryNullResponse() {
		return "" +
	           "[\r\n" + 
	           " [\r\n" + 
	           " ]\r\n" + 
	           "]";
	}
	
	public static String createProjectQueryResponse() {
		return "" +
	           "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
	           "<Asset href=\"/V1Integrations/rest-1.v1/Data/Epic/1816/2742\" id=\"Epic:1816:2742\">" + 
	           "<Attribute name=\"Name\">New Epic</Attribute>" + 
	           "<Attribute name=\"Description\">This is a description.</Attribute>" + 
	           "<Attribute name=\"Reference\">mobile_helpdesk_app</Attribute>" + 
	           "<Attribute name=\"PlannedStart\">2014-06-01</Attribute>" + 
	           "<Attribute name=\"PlannedEnd\">2014-12-31</Attribute>" + 
	           "<Relation name=\"Scope\"><Asset href=\"/V1Integrations/rest-1.v1/Data/Scope/0\" idref=\"Scope:0\" /></Relation>" + 
	           "<Relation name=\"Category\"><Asset href=\"/V1Integrations/rest-1.v1/Data/EpicCategory/207\" idref=\"EpicCategory:207\" /></Relation>" + 
	           "</Asset>";
	}
	
	public static String getAssetNumberQueryResponse() {
		return "" +
	           "[\r\n" + 
	           " [\r\n" + 
	           "  {\r\n" + 
	           "   \"_oid\":\"Epic:1587\",\r\n" + 
	           "   \"Number\":\"E-01058\"\r\n" + 
	           "  }\r\n" + 
	           " ]\r\n" + 
	           "]";
	}
	
	public static String getAssetOIDQueryResponse() {
		return "" +
			   "[\r\n" + 
			   " [\r\n" + 
			   "  {\r\n" + 
			   "   \"_oid\":\"Epic:1844\"\r\n" + 
			   "  }\r\n" + 
			   " ]\r\n" + 
			   "]";
	}
	
	public static String getCreateMemberResponse() {
		return "" +
			   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			   "<Asset href=\"/V1Integrations/rest-1.v1/Data/Member/6983/12842\" id=\"Member:6983:12842\">\r\n" + 
			   "<Attribute name=\"Name\">Test User</Attribute>\r\n" + 
			   "<Attribute name=\"Username\">testuser</Attribute>\r\n" + 
			   "<Attribute name=\"Password\" />\r\n" + 
			   "<Attribute name=\"Nickname\">testuser</Attribute>\r\n" + 
			   "<Attribute name=\"IsCollaborator\">false</Attribute>\r\n" + 
			   "<Attribute name=\"NotifyViaEmail\">false</Attribute>\r\n" + 
			   "<Attribute name=\"SendConversationEmails\">false</Attribute>\r\n" + 
			   "<Attribute name=\"Description\" />\r\n" + 
			   "<Relation name=\"DefaultRole\">\r\n" + 
			   "<Asset href=\"/V1Integrations/rest-1.v1/Data/Role/2\" idref=\"Role:2\" />\r\n" + 
			   "</Relation>\r\n" + 
			   "</Asset>";
	}

}
