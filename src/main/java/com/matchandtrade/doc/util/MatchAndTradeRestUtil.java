package com.matchandtrade.doc.util;

import java.util.HashMap;
import java.util.Map;

import com.github.rafasantos.restdocmaker.template.Snippet;
import com.github.rafasantos.restdocmaker.template.SnippetFactory;
import com.github.rafasantos.restdocmaker.util.JsonUtil;
import com.matchandtrade.doc.config.PropertiesLoader;
import com.matchandtrade.rest.v1.json.UserJson;

import io.restassured.http.Header;

public class MatchAndTradeRestUtil {
	
	private static String baseUrl = PropertiesLoader.serverUrl();
	private static Header lastAuthorizationHeader;
	private static final SnippetFactory snippetFactory = new SnippetFactory();
	
	private enum Endpoint {
		AUTHENTICATE("matchandtrade-web-api/v1/authenticate"),
		SIGN_OFF("matchandtrade-web-api/v1/sign-out"),
		AUTHENTICATIONS("matchandtrade-web-api/v1/authentications"),
		TRADES("matchandtrade-web-api/v1/trades"),
		ATTACHMENTS("matchandtrade-web-api/v1/attachments"),
		SEARCH("matchandtrade-web-api/v1/search"),
		TRADE_RESULTS("results"),
		ITEMS("items"),
		ITEM_ATTACHMENTS("attachments"),
		INFO("info"),
		WANT_ITEMS("want-items"),
		TRADE_MEMBERSHIPS("matchandtrade-web-api/v1/trade-memberships"),
		OFFERS("offers"),
		USERS("matchandtrade-web-api/v1/users");

		private String path;
		
		private Endpoint(String path) {
			this.path = path;
		}
		
		public String asURL(String baseUrl) {
			return baseUrl + "/" + path;
		}
	}
	
	public static String authenticateUrl() {
		return Endpoint.AUTHENTICATE.asURL(baseUrl);
	}

	public static String authenticateInfoUrl() {
		return Endpoint.AUTHENTICATE.asURL(baseUrl) + "/" + Endpoint.INFO.path;
	}
	
	public static String authenticationsUrl() {
		return Endpoint.AUTHENTICATIONS.asURL(baseUrl);
	}

	public static String signOffUrl() {
		return Endpoint.AUTHENTICATE.asURL(baseUrl) + "/" + Endpoint.SIGN_OFF.path;
	}
	public static String searchUrl() {
		return Endpoint.SEARCH.asURL(baseUrl);
	}
	public static String usersUrl() {
		return Endpoint.USERS.asURL(baseUrl);
	}
	public static String usersUrl(Integer userId) {
		return Endpoint.USERS.asURL(baseUrl) + "/" + userId;
	}
	public static String tradesUrl() {
		return Endpoint.TRADES.asURL(baseUrl);
	}
	public static String attachmentsUrl() {
		return Endpoint.ATTACHMENTS.asURL(baseUrl) + "/";
	}
	public static String attachmentsUrl(Integer tradeMembershipId, Integer itemId, Integer attachmentId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path + "/" + itemId + "/" + Endpoint.ITEM_ATTACHMENTS.path + "/" + attachmentId;
	}
	public static String attachmentsUrl(Integer tradeMembershipId, Integer itemId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path + "/" + itemId + "/" + Endpoint.ITEM_ATTACHMENTS.path + "/";
	}
	public static String tradesUrl(Integer tradeId) {
		return Endpoint.TRADES.asURL(baseUrl) + "/" + tradeId;
	}
	public static String wantItemsUrl(Integer tradeMembershipId, Integer itemId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path + "/" + itemId + "/" + Endpoint.WANT_ITEMS.path;
	}
	public static String wantItemsUrl(Integer tradeMembershipId, Integer itemId, Integer wantItemId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path + "/" + itemId + "/" + Endpoint.WANT_ITEMS.path + "/" + wantItemId;
	}
	public static String itemsUrl(Integer tradeMembershipId, Integer itemId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path + "/" + itemId;
	}
	public static String itemsUrl(Integer tradeMembershipId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.ITEMS.path;
	}
	public static String tradeMembershipsUrl() {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl);
	}
	public static String tradeMembershipsUrl(Integer tradeMembershipId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId;
	}
	public static String tradeResultsUrl(Integer tradeId) {
		return Endpoint.TRADES.asURL(baseUrl) + "/" + tradeId + "/" + Endpoint.TRADE_RESULTS.path;
	}
	public static String offerUrl(Integer tradeMembershipId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.OFFERS.path;
	}
	public static String offerUrl(Integer tradeMembershipId, Integer offerId) {
		return Endpoint.TRADE_MEMBERSHIPS.asURL(baseUrl) + "/" + tradeMembershipId + "/" + Endpoint.OFFERS.path + "/" + offerId;
	}
	

	
	public static Header getLastAuthorizationHeader() {
		if (lastAuthorizationHeader == null) {
			return nextAuthorizationHeader();
		} else {
			return lastAuthorizationHeader;
		}
	}
	
	public static Map<String, String> getLastAuthorizationHeaderAsMap() {
		Map<String, String> result = new HashMap<>();
		Header authorizationHeader = getLastAuthorizationHeader();
		result.put(authorizationHeader.getName(), authorizationHeader.getValue());
		return result;
	}

	public static Integer getLastAuthenticatedUserId() {
		SnippetFactory snippetFactory = new SnippetFactory(getLastAuthorizationHeader());
		Snippet snippet = snippetFactory.makeSnippet(authenticationsUrl() + "/");
		return snippet.getResponse().body().path("userId");
	}

	public static UserJson getLastAuthenticatedUser() {
		SnippetFactory snippetFactory = new SnippetFactory(getLastAuthorizationHeader());
		Snippet snippet = snippetFactory.makeSnippet(usersUrl() + "/" + getLastAuthenticatedUserId());
		snippet.getResponse().then().statusCode(200);
		return JsonUtil.fromString(snippet.getResponse().asString(), UserJson.class);
	}
	
	public static Header nextAuthorizationHeader() {
		Snippet authenticateSnippet = snippetFactory.makeSnippet(MatchAndTradeRestUtil.authenticateUrl());
		String headerValue = authenticateSnippet.getResponse().getHeader("Authorization");
		lastAuthorizationHeader = new Header("Authorization", headerValue);
		return lastAuthorizationHeader;
	}

}
