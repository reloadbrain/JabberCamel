package com.bencompany.jabbercamel.camel;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Processes messages coming from Camel into a JabberMessage
 */
@Component
@PropertySource({ "classpath:localhost.properties" })
public class JabberProcessor implements Processor {

	@Value("${bot.name}")
	public String botname;

	Logger logger = Logger.getLogger("JabberProcessor");

	@Autowired
	TopicHandler topic; // Handles putting messages onto the WebSockets Topic
	@Autowired
	LinkHandler linkHandler; // Handles URL's pasted in messages
	@Autowired
	ObjectMapper om; // Maps objects to JSON
	@Autowired
	JabberDao dao; // Database access
	@Autowired
	ChatHandler chatHandler; // handles chat responses

	@Override
	public void process(Exchange camelExchange) throws Exception {
		logger.info("Processing new message");

		JabberMessage msg = convertToMessage(camelExchange);

		// don't do any message processing outside of this!
		
		// TODO: remove any logic from this class, put all logic into relevant modules to assist with 
		// #22 Modular Architecture
		try {
			// save link
			if (msg.getMessage().contains("http")) {
				linkHandler.process(msg, dao);
			}

			// command, don't save the message!
			if (msg.getMessage().contains(botname)
					|| msg.getUsername().contains(botname)) {
				logger.info("Hey, that's my name!");
				chatHandler.handleMessage(msg);
			} else {
				// standard message saving
				dao.putMessage(msg);
				topic.pushToTopic(msg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			chatHandler.debugMessage(e.getMessage());
		}
		
		// set JSON as camel message body
		camelExchange.getOut().setBody(om.writeValueAsString(msg));

	}

	/*
	 * Converts message from Camel exchange to JabberMessage
	 */
	private JabberMessage convertToMessage(Exchange exch) {
		JabberMessage msg = new JabberMessage();
		String user = extractUser(exch);
		msg.setMessage((String) exch.getIn().getBody());
		msg.setUsername(user);
		msg.setTimestamp(new Date().toString());
		logger.info("New Message Processed: " + msg.toString());
		return msg;
	}

	/*
	 * Extracts username from XMPP User string (user@domain.com/resource) Uses
	 * resource as a name due to conferences using the conference name as
	 * 'user'.
	 */
	private String extractUser(Exchange exch) {
		String userString = (String) exch.getIn().getHeader("CamelXmppFrom");
		String[] user = userString.split("/");
		return user[1];
	}

}
