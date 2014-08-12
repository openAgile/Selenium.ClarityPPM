package com.versionone.selenium;

import java.util.HashMap;
import java.util.Map;

import com.ca.clarity.avee.api.InterfaceSettings;

public class InterfaceSettingsImpl implements InterfaceSettings {

	private Map<String, String> settings;

	public InterfaceSettingsImpl() {
		this.settings = new HashMap<String, String>();
	}

	public InterfaceSettingsImpl(InterfaceSettings settings) {
		this.settings = new HashMap<String, String>();
		this.settings.put(InterfaceSettings.OAUTH_CLIENT_ID, settings.getSetting(InterfaceSettings.OAUTH_CLIENT_ID));
		this.settings.put(InterfaceSettings.OAUTH_CLIENT_NAME, settings.getSetting(InterfaceSettings.OAUTH_CLIENT_NAME));
		this.settings.put(InterfaceSettings.OAUTH_CLIENT_SECRET, settings.getSetting(InterfaceSettings.OAUTH_CLIENT_SECRET));
		this.settings.put(InterfaceSettings.OAUTH_REDIRECT_URI, settings.getSetting(InterfaceSettings.OAUTH_REDIRECT_URI));
		this.settings.put(InterfaceSettings.OAUTH_AUTH_URI, settings.getSetting(InterfaceSettings.OAUTH_AUTH_URI));
		this.settings.put(InterfaceSettings.OAUTH_TOKEN_URI, settings.getSetting(InterfaceSettings.OAUTH_TOKEN_URI));
		this.settings.put(InterfaceSettings.OAUTH_SERVER_BASE_URI, settings.getSetting(InterfaceSettings.OAUTH_SERVER_BASE_URI));
		this.settings.put(InterfaceSettings.OAUTH_ACCESS_TOKEN, settings.getSetting(InterfaceSettings.OAUTH_ACCESS_TOKEN));
		this.settings.put(InterfaceSettings.OAUTH_REFRESH_TOKEN, settings.getSetting(InterfaceSettings.OAUTH_REFRESH_TOKEN));
		this.settings.put(InterfaceSettings.OAUTH_SCOPE, settings.getSetting(InterfaceSettings.OAUTH_SCOPE));
		this.settings.put(InterfaceSettings.CLARITY_ENDPOINT_URL, settings.getSetting(InterfaceSettings.CLARITY_ENDPOINT_URL));
		this.settings.put(InterfaceSettings.CLARITY_UI_ENDPOINT, settings.getSetting(InterfaceSettings.CLARITY_UI_ENDPOINT));
		this.settings.put(InterfaceSettings.CLARITY_XOG_ENDPOINT, settings.getSetting(InterfaceSettings.CLARITY_XOG_ENDPOINT));
		this.settings.put(InterfaceSettings.CLARITY_ODATA_ENDPOINT, settings.getSetting(InterfaceSettings.CLARITY_ODATA_ENDPOINT));
		this.settings.put(InterfaceSettings.USE_PROJECT_NAME_IF_AGILE_NAME_IS_NULL, settings.getSetting(InterfaceSettings.USE_PROJECT_NAME_IF_AGILE_NAME_IS_NULL));
	}

	public String getSetting(String key) {
		return this.settings.get(key);
	}

	public void setSetting(String key, String value) {
		this.settings.put(key, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n OAUTH_SERVER_BASE_URI: " + this.getSetting(InterfaceSettings.OAUTH_SERVER_BASE_URI));
		sb.append("\n OAUTH_CLIENT_NAME: " + this.getSetting(InterfaceSettings.OAUTH_CLIENT_NAME));
		sb.append("\n OAUTH_CLIENT_ID: " + this.getSetting(InterfaceSettings.OAUTH_CLIENT_ID));
		sb.append("\n OAUTH_CLIENT_SECRET: " + this.getSetting(InterfaceSettings.OAUTH_CLIENT_SECRET));
		sb.append("\n OAUTH_SCOPE: " + this.getSetting(InterfaceSettings.OAUTH_SCOPE));
		sb.append("\n OAUTH_AUTH_URI: " + this.getSetting(InterfaceSettings.OAUTH_AUTH_URI));
		sb.append("\n OAUTH_TOKEN_URI: " + this.getSetting(InterfaceSettings.OAUTH_TOKEN_URI));
		sb.append("\n OAUTH_REDIRECT_URI: " + this.getSetting(InterfaceSettings.OAUTH_REDIRECT_URI));
		sb.append("\n OAUTH_ACCESS_TOKEN: " + this.getSetting(InterfaceSettings.OAUTH_ACCESS_TOKEN));
		sb.append("\n OAUTH_REFRESH_TOKEN: " + this.getSetting(InterfaceSettings.OAUTH_REFRESH_TOKEN));
		sb.append("\n CLARITY_ENDPOINT_URL: " + this.getSetting(InterfaceSettings.CLARITY_ENDPOINT_URL));
		sb.append("\n CLARITY_UI_ENDPOINT: " + this.getSetting(InterfaceSettings.CLARITY_UI_ENDPOINT));
		sb.append("\n CLARITY_XOG_ENDPOINT: " + this.getSetting(InterfaceSettings.CLARITY_XOG_ENDPOINT));
		sb.append("\n CLARITY_ODATA_ENDPOINT: " + this.getSetting(InterfaceSettings.CLARITY_ODATA_ENDPOINT));
		sb.append("\n USE_PROJECT_NAME_IF_AGILE_NAME_IS_NULL: " + this.getSetting(InterfaceSettings.USE_PROJECT_NAME_IF_AGILE_NAME_IS_NULL));
		return sb.toString();
	}

}
