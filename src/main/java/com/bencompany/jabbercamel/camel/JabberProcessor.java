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
 * Processes messages coming from Camel into a JabberMessage, then saves to database and sends to topic.
 */
@Component
@PropertySource({ "classpath:localhost.properties" })
public class JabberProcessor implements Processor {
	
	@Value("${bot.name}")
	public String botname;
	
	Logger logger = Logger.getLogger("JabberProcessor");
	
	@Autowired TopicHandler topic; // Handles putting messages onto the WebSockets Topic
	@Autowired LinkHandler linkHandler; // Handles URL's pasted in messages
	@Autowired ObjectMapper om; // Maps objects to JSON
	@Autowired JabberDao dao; // Database access
	@Autowired ChatHandler chatHandler; // handles chat responses
	
	@Override
	public void process(Exchange arg0) throws Exception {
		logger.info("Processing!");
		
		JabberMessage msg = convertToMessage(arg0);
		
		// save link
		if (msg.getMessage().contains("http")) {
			linkHandler.putLink(msg);
		}
		
		if (msg.getMessage().contains(botname)) {
			logger.info("Hey, that's my name!");
			chatHandler.handleMessage(msg);
		}
		
		// standard message saving
		dao.putMessage(msg);
		topic.pushToTopic(msg);
		arg0.getOut().setBody(om.writeValueAsString(msg));
		
	}
	
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
	 * Extracts username from XMPP User string (user@domain.com/resource)
	 * Uses resource as a name due to conferences using the conference name as 'user'.
	 */
	private String extractUser(Exchange exch) {
		String userString = (String) exch.getIn().getHeader("CamelXmppFrom");
		String[] user = userString.split("/");
		return user[1];
	}

}
