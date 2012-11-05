package com.enapays.servlets.social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enapays.model.FacebookToken;

public class FacebookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String appKey;
	private static String postMessage;
	private static Map tokens;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (request.getParameter("access_token") != null) {
			aquireToken(request);
			postToFacebook(request, response);
			return;
		}
		if (!validateParams(request)) {
			System.out.println("parametros no validos");
			requestAppKey(response);
		}
		if (existValidTokenForApp(request)) {
			postToFacebook(request, response);
		} else {
			requestPermissions(request, response);
		}
	}

	private void aquireToken(HttpServletRequest request) {
		String access_token = request.getParameter("access_token");
		String appKey = getAppKeyFromSession(request);
		String expirationTime = request.getParameter("expires_in");
		FacebookToken token = new FacebookToken(access_token, appKey,
				Integer.parseInt(expirationTime));
		setToken(request, token);
	}

	private boolean validateParams(HttpServletRequest request) {
		return getAppKey(request) != null && getPost(request) != null;
	}

	private void requestAppKey(HttpServletResponse response) throws IOException {
		response.getWriter().write("Param AppKey and Post required");
		response.getWriter().flush();
		return;
	}

	private boolean existValidTokenForApp(HttpServletRequest request) {
		Map tokens = getTokens(request);

		String appKey = getAppKey(request);
		String postMessage = getPost(request);

		addAppKeyToSession(request, appKey);
		addPostToSession(request, postMessage);

		if (!tokens.containsKey(appKey)) {
			return false;
		}

		Date now = new Date();

		FacebookToken token = (FacebookToken) tokens.get(appKey);
		Date tokenDate = token.getCreationTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(tokenDate);
		cal.add(Calendar.SECOND, token.getTokenExpirationTime());

		Date expirationDate = cal.getTime();
		return expirationDate.before(now);
	}

	private void postToFacebook(HttpServletRequest request,
			HttpServletResponse response) {
		FacebookToken token = getToken(request);
		try {
			URL facebookUrl = new URL(
					"https://graph.facebook.com/josue.delDF/feed?access_token="
							+ token.getToken());

			URLConnection conn = facebookUrl.openConnection();
			conn.setDoOutput(true);

			OutputStreamWriter ow = new OutputStreamWriter(
					conn.getOutputStream());

			ow.write("message=" + FacebookServlet.postMessage);

			ow.flush();
			ow.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line;

			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			
			response.sendRedirect("/social.fb.crm/close.jsp");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}

	private void requestPermissions(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		addAppKeyToSession(request, getAppKey(request));
		addPostToSession(request, getPost(request));

		String url = "https://www.facebook.com/dialog/oauth?client_id="
				+ getAppKey(request)
				+ "&redirect_uri=http://www.ingeniousoft.com:8080/social.fb.crm/index.jsp&scope=publish_actions&response_type=token";

		response.sendRedirect(url);
	}

	private String getAppKey(HttpServletRequest request) {
		return request.getParameter("AppKey");
	}

	private String getPost(HttpServletRequest request) {
		return request.getParameter("PostMessage");
	}

	private void addAppKeyToSession(HttpServletRequest request, String appKey) {
		FacebookServlet.appKey = appKey;
	}

	private String getAppKeyFromSession(HttpServletRequest request) {
		return FacebookServlet.appKey;
	}

	private void addPostToSession(HttpServletRequest request, String postMessage) {
		FacebookServlet.postMessage = postMessage;
	}

	private String getPostFromSession(HttpServletRequest request) {
		return FacebookServlet.postMessage;
	}

	private Map getTokens(HttpServletRequest request) {
		if (FacebookServlet.tokens == null) {
			FacebookServlet.tokens = new HashMap();
		}

		return FacebookServlet.tokens;
	}

	private void setTokens(HttpServletRequest request, Map tokens) {
		FacebookServlet.tokens = tokens;
	}

	private FacebookToken getToken(HttpServletRequest request) {
		Map tokens = getTokens(request);

		return (FacebookToken) tokens.get(FacebookServlet.appKey);
	}

	private void setToken(HttpServletRequest request, FacebookToken token) {
		Map tokens = getTokens(request);
		tokens.put(token.getApiKey(), token);

		setTokens(request, tokens);

	}
}
