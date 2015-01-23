package com.bencompany.jabbercamel.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bencompany.jabbercamel.camel.JabberDao;
import com.bencompany.jabbercamel.model.User;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// Allows saving and retrieving from DB
	@Autowired
	public JabberDao dao;

	// Message template for sending messages to topic
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		String formattedDate = getTimestamp(locale);
		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

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
