package com.enapays.servlets.social;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String consumer = request.getParameter("consumer");
		String consumerS = request.getParameter("consumerS");
		String token = request.getParameter("token");
		String tokenS = request.getParameter("tokenS");
		String tweet = request.getParameter("tweet");
		postTwitter(consumer, consumerS, token, tokenS, tweet);
		
	}
	
	private void postTwitter(String consumer, String consumerSecret, String token, String tokenS, String tweet) {
		  if(consumer != null && consumerSecret != null && token != null && tokenS != null){
		   Twitter twitter = new TwitterFactory().getInstance();
		   twitter.setOAuthConsumer(consumer, consumerSecret);
		   try {
		    AccessToken acToken = new AccessToken(token,tokenS );      
		    twitter.setOAuthAccessToken(acToken);
		    twitter.updateStatus(tweet);
		   } catch (TwitterException te) {
		    if (401 == te.getStatusCode()) {
		     System.out.println("Unable to get the access token.");
		    } else {
		     te.printStackTrace();
		    }
		   }
		  }else{
		   System.out.println("Ningun valor debe de ir vacío");   
		  }
	}

}
