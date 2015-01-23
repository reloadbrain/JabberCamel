package com.bencompany.jabbercamel.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bencompany.jabbercamel.camel.JabberDao;
import com.bencompany.jabbercamel.model.Link;
import com.bencompany.jabbercamel.model.User;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jabber Commands
 * @author bharris
 *
 */
@Controller
public class CommandsController {
	
	// Allows saving and retrieving from DB
	@Autowired
	public JabberDao dao;

	// Message template for sending messages to topic
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	
	
	/**
	 * Returns the last 20 messages as JSON
	 */
	@RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getMessages() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dao.getLast20Messages());
	}

	/**
	 * Returns list of active users as JSON
	 */
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getActiveUsers() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<User> users = dao.getActiveUsers();
		return mapper.writeValueAsString(users);
	}
	
	/**
	 * Returns list of popular links as JSON
	 */
	@RequestMapping(value = "/getLinks", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getPopularLinks() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Link> links = dao.getPopularLinks();
		return mapper.writeValueAsString(links);
	}
	/**
	 * Test endpoint to check that topic is sending messages properly
	 */
	@RequestMapping(value="/greetings", method=RequestMethod.GET)
	public String handle() {
		Locale locale = new Locale("en");
		this.messagingTemplate.convertAndSend("/topic/jabbermessages","["+getTimestamp(locale)+"]:Message Recieved");
		return "Sent";
	}

	public String getTimestamp(Locale locale) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

}
