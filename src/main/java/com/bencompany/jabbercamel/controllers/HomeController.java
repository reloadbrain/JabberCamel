package com.bencompany.jabbercamel.controllers;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bencompany.jabbercamel.camel.JabberDao;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@SuppressWarnings("unused")
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

	public String getTimestamp(Locale locale) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

}
