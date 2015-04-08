package com.bencompany.jabbercamel.camel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

@Component
public class LinkHandler {
	Logger logger = LoggerFactory.getLogger(LinkHandler.class);
	
	@Autowired JabberDao dao; // Database access
	
	//TODO: Strip link out of rest of message
	public void putLink(JabberMessage msg) {
		logger.info("Saving link:" + msg.getMessage());
		Link existingLink = dao.getLink(msg.getMessage());
		if (existingLink != null) {
			logger.info("Link exists, updating:" + msg.getMessage());
			updateLink(existingLink, msg.getUsername());
		} else {
			logger.info("Link doesn't exist, creating:" + msg.getMessage());
			try {
			Link link = convertMessageToLink(msg);
			dao.putLink(link);
			}
			catch (Exception e) {
				logger.error("Could not find link in message. Ignoring");
			}
		}
	}
	
	/*
	 * Converts JabberMessage object to Link object
	 */
	public Link convertMessageToLink(JabberMessage msg) throws Exception {
		Link link = new Link();
		link.setCount(1);
		link.setOp(msg.getUsername());
		link.setUrl(stripLink(msg.getMessage()));
		link.setLastPostedBy(msg.getUsername());
		return link;
		
	}
	
	/*
	 * Strips link from message
	 */
	private String stripLink(String message) throws Exception {
		String[] list = message.split(" ");
		String url = null;
		
		for (int i = 0; i < list.length; i++) {
			if (list[i].contains("http")) {
				url = list[i];
			}
		}
		if (url == null) {
			throw new Exception("URL could not be found");
		} else {
			return url;
		}
	}

	/*
	 * Increments link count and updates last poster
	 */
	private void updateLink(Link existingLink, String latestUser) {
		long count = existingLink.getCount();
		existingLink.setCount(++count);
		existingLink.setLastPostedBy(latestUser);
		dao.putLink(existingLink);
	}


}
