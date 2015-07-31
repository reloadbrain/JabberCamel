package com.bencompany.jabbercamel.camel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkHandler {
	Logger logger = LoggerFactory.getLogger(LinkHandler.class);

	@Autowired JabberDao dao; // Database access

	public void putLinks(JabberMessage msg) {
		logger.info("Saving link:" + msg.getMessage());
		List<Link> existingLinks = dao.getLinks(msg.getMessage());
		if ((existingLinks != null) && (!existingLinks.isEmpty())) {
			logger.info("Links exist, updating:" + msg.getMessage());
			updateLinks(existingLinks, msg.getUsername());
		} else {
			logger.info("Links don't exist, creating:" + msg.getMessage());
			try {
			List<Link> links = convertMessageToLink(msg);
			dao.putLinks(links);
			}
			catch (Exception e) {
				logger.error("Could not find link in message. Ignoring");
			}
		}
	}
	
	/*
	 * Converts JabberMessage object to Link object
	 */
	public List<Link> convertMessageToLink(JabberMessage msg) throws Exception {
        ArrayList<Link> links = new ArrayList<Link>();

		Link link = new Link();
		link.setCount(1);
		link.setOp(msg.getUsername());
		link.setLastPostedBy(msg.getUsername());

        for (String url : stripLink(msg.getMessage())) {
            link.setUrl(url);
			links.add(link);
        }

		return links;
	}
	
	/*
	 * Strips link from message
	 */
	private List<String> stripLink(String message) throws Exception {
		String[] list = message.split(" ");
		ArrayList<String> urls = new ArrayList<String>();
		
		for (int i = 0; i < list.length; i++) {
            String word = list[i];

			if (word.contains("http")) {
				urls.add(word);
			}
		}
		if (urls.isEmpty()) {
			throw new Exception("URL could not be found");
		} else {
			return urls;
		}
	}

	/*
	 * Increments link count and updates last poster
	 */
	private void updateLinks(List<Link> existingLinks, String latestUser) {
        long count;

        for (Link existingLink : existingLinks) {
            count = existingLink.getCount();
            existingLink.setCount(++count);
            existingLink.setLastPostedBy(latestUser);
        }


        dao.putLinks(existingLinks);
	}


}
