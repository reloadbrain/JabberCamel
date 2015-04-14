package com.bencompany.jabbercamel.camel;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.utils.GoogleUrlShortener;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class ChatHandler {

	Logger logger = Logger.getLogger(ChatHandler.class);
	
	// responds to MUC
	@EndpointInject(uri="direct:chatResponse")
	  ProducerTemplate producer;
	
	// sends message to hardcoded developer
	@EndpointInject(uri="direct:debugPm")
	  ProducerTemplate debugPm;
	
	// sends message to user specified in 'recipient' header
	@EndpointInject(uri="direct:pm")
	  ProducerTemplate pm;
	
	@Autowired
	GoogleUrlShortener urlShortener;
	
	/*
	 * Main handler method for class
	 */
	public void handleMessage(JabberMessage msg) {
		if (msg.getMessage().contains("abc")) {
			try {
				getRss(msg.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	/*
	 * Gets RSS feed of ABC News and PM's user
	 */
	public void getRss(String user) throws IllegalArgumentException, FeedException, IOException {
		logger.info("Getting RSS feed for user:" + user);
		
		// get feed and convert to ArrayList
		SyndFeedInput input = new SyndFeedInput();
		URL feedUrl = new URL("http://www.abc.net.au/news/feed/51120/rss.xml");
		URLConnection conn = feedUrl.openConnection();
		XmlReader xml = new XmlReader(conn);
		SyndFeed feed = input.build(xml);
		StringBuilder sb = new StringBuilder();
		ArrayList<SyndEntry> entries = null;
		entries = (ArrayList<SyndEntry>) feed.getEntries();
		
		// construct string response
		sb.append(feed.getAuthor() + System.getProperty("line.separator"));
		for (int i = 0; i < 5; i++ ){ 
			SyndEntry entry = entries.get(i);
			sb.append(entry.getTitle() + "  ");
			sb.append(urlShortener.shorten(entry.getLink()) + System.getProperty("line.separator"));
		}
		logger.info("Sending RSS to user:" + user);
		pm.sendBodyAndHeader(sb.toString(), "recipient", user);
	}
	
	/*
	 * Send message to MUC
	 */
	public void say(String message) {
		producer.sendBody("Uh oh: " + message);
	}

	/*
	 * Send message to developer
	 */
	public void debugMessage(String message) {
		debugPm.sendBody("Something's gone wrong:" + message);
	}
	
	/*
	 * Send message to user
	 */
	public void message(String name, String message) {
		pm.sendBodyAndHeader(message, "recipient", name);
	}
	 
}
