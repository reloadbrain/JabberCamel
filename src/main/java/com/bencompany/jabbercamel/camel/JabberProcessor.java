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
import com.bencompany.jabbercamel.modules.LinkHandler;
import com.bencompany.jabbercamel.modules.RssHandler;
import com.bencompany.jabbercamel.utils.ChatUtils;
import com.bencompany.jabbercamel.utils.MessageUtils;
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
	RssHandler rssHandler;
	
	
	@Override
	public void process(Exchange camelExchange) throws Exception {
		logger.info("Processing new message");

		JabberMessage msg = MessageUtils.convertToMessage(camelExchange);


		// Main area for calling all modules to be processed. 
		try {
			// Front-End and DB
			dao.putMessage(msg);
			topic.pushToTopic(msg);
			
			// Modules
			linkHandler.process(msg);
			rssHandler.process(msg);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			ChatUtils.debugMessage(e.getMessage());
		}
		
		// set JSON as camel message body
		camelExchange.getOut().setBody(om.writeValueAsString(msg));

	}

}
