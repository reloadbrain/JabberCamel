package com.bencompany.jabbercamel.utils;

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
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class ChatUtils {

	Logger logger = Logger.getLogger(ChatUtils.class);
	
	// responds to MUC
	@EndpointInject(uri="direct:chatResponse")
	public static ProducerTemplate producer;
	
	// sends message to hardcoded developer
	@EndpointInject(uri="direct:debugPm")
	public static ProducerTemplate debugPm;
	
	// sends message to user specified in 'recipient' header
	@EndpointInject(uri="direct:pm")
	public static ProducerTemplate pm;
	
	@Autowired
	public static GoogleUrlShortener urlShortener;
	
	/*
	 * Send message to MUC
	 */
	public static void say(String message) {
		producer.sendBody("Uh oh: " + message);
	}

	/*
	 * Send message to developer
	 */
	public static void debugMessage(String message) {
		debugPm.sendBody("Something's gone wrong:" + message);
	}
	
	/*
	 * Send message to user
	 */
	public static void message(String name, String message) {
		pm.sendBodyAndHeader(message, "recipient", name);
	}
	 
}
