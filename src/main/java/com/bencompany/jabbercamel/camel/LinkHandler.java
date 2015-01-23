package com.bencompany.jabbercamel.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

@Component
public class LinkHandler {

	@Autowired JabberDao dao; // Database access
	Logger logger = LoggerFactory.getLogger(LinkHandler.class);
	
	public void putLink(JabberMessage msg) {
		logger.info("Saving link:" + msg.getMessage());
		Link existingLink = dao.getLink(msg.getMessage());
		if (existingLink != null) {
			logger.info("Link exists, updating:" + msg.getMessage());
			updateLink(existingLink, msg.getUsername());
		} else {
			logger.info("Link doesn't exist, creating:" + msg.getMessage());
			Link link = convertMessageToLink(msg);
			dao.putLink(link);
		}
	}
	
	private Link convertMessageToLink(JabberMessage msg) {
		Link link = new Link();
		link.setCount(1);
		link.setOp(msg.getUsername());
		link.setUrl(msg.getMessage());
		link.setLastPostedBy(msg.getUsername());
		return link;
		
	}

	private void updateLink(Link existingLink, String latestUser) {
		long count = existingLink.getCount();
		existingLink.setCount(++count);
		existingLink.setLastPostedBy(latestUser);
		dao.putLink(existingLink);
	}


}
