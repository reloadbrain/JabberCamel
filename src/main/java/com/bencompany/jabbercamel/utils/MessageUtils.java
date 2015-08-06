package com.bencompany.jabbercamel.utils;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import com.bencompany.jabbercamel.model.JabberMessage;

public class MessageUtils {

	static Logger logger = Logger.getLogger(MessageUtils.class);
	/*
	 * Extracts username from XMPP User string (user@domain.com/resource) Uses
	 * resource as a name due to conferences using the conference name as
	 * 'user'.
	 */
	private static String extractUser(Exchange exch) {
		String userString = (String) exch.getIn().getHeader("CamelXmppFrom");
		String[] user = userString.split("/");
		return user[1];
	}
	
	/*
	 * Converts message from Camel exchange to JabberMessage
	 */
	public static JabberMessage convertToMessage(Exchange exch) {
		JabberMessage msg = new JabberMessage();
		String user = extractUser(exch);
		msg.setMessage((String) exch.getIn().getBody());
		msg.setUsername(user);
		msg.setTimestamp(new Date().toString());
		logger.info("New Message Processed: " + msg.toString());
		return msg;
	}
	
	
}
