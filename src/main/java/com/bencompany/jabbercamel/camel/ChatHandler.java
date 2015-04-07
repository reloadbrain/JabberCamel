package com.bencompany.jabbercamel.camel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
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
	@EndpointInject(uri="direct:chatResponse")
	  ProducerTemplate producer;
	
	@EndpointInject(uri="direct:debugPm")
	  ProducerTemplate debugPm;
	
	@EndpointInject(uri="direct:pm")
	  ProducerTemplate pm;
	
	@Autowired
	PropertiesComponent properties;
	
	@Autowired
	CamelContext ctx;
	
	public void handleMessage(JabberMessage msg) {
	
		if (msg.getMessage().contains("rss")) {
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
		SyndFeedInput input = new SyndFeedInput();
		URL feedUrl = new URL("http://www.abc.net.au/news/feed/51120/rss.xml");
		URLConnection conn = feedUrl.openConnection();
		XmlReader xml = new XmlReader(conn);
		SyndFeed feed = input.build(xml);
		StringBuilder sb = new StringBuilder();
		sb.append(feed.getAuthor() + System.getProperty("line.separator"));
		ArrayList<SyndEntry> entries = null;
		entries = (ArrayList<SyndEntry>) feed.getEntries();
		for (int i = 0; i < 5; i++ ){ 
			SyndEntry entry = entries.get(i);
			sb.append(entry.getTitle() + "  ");
			sb.append(GoogleUrlShortener.shorten(entry.getLink()) + System.getProperty("line.separator"));
		}
		logger.info("Sending RSS to user:" + user);
		pm.sendBodyAndHeader(sb.toString(), "recipient", user);
	}
	
	public void say(String message) {
		producer.sendBody("Uh oh: " + message);
	}

	public void message(String name, String message) {
		debugPm.sendBody("msg sent to" + name);
		pm.sendBodyAndHeader(message, "recipient", name);
	}
	 
}
