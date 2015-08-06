package com.bencompany.jabbercamel.modules;


import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.camel.JabberDao;
import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkHandler implements Module {
	Logger logger = LoggerFactory.getLogger(LinkHandler.class);

	@Autowired JabberDao dao; // Database access

	/*
	 * Converts JabberMessage object to List of Links. Each link will have a count of 1.
	 */
	public List<Link> convertMessageToLinks(JabberMessage msg) {
        ArrayList<Link> links = new ArrayList<Link>();
        List<String> stringUrls = stripLinks(msg.getMessage());
        if (stringUrls != null) {
	        for (String url : stringUrls) {
	        	Link link = new Link();
	    		link.setCount(1);
	    		link.setOp(msg.getUsername());
	    		link.setLastPostedBy(msg.getUsername());
	            link.setUrl(url);
				links.add(link);
	        }
        }
        return links;
	}
	
	/*
	 * Strips links from message
	 */
	private List<String> stripLinks(String message) {
		String[] list = message.split(" ");
		ArrayList<String> urls = new ArrayList<String>();
		
		for (int i = 0; i < list.length; i++) {
            String word = list[i];
            
			if (word.contains("http") && !word.startsWith("<")) {
				urls.add(word);
			}
		}
		return urls;
	}

	/*
	 * Increments link count and updates last poster
	 */
	
	private List<Link> updateLinks(List<Link> links, String latestUser) {
		List<Link> updatedLinks = new ArrayList<Link>();
		if (links != null && !links.isEmpty()) {
			for (Link link : links) {
				// check if link already exists, and if so increment existing count
				Link existingLink = dao.getLinkByURL(link.getUrl());
				if (existingLink != null) {
					long updatedCount = existingLink.getCount();
					updatedCount = ++updatedCount;
					existingLink.setCount(updatedCount);
					updatedLinks.add(existingLink);
				} else {
					// otherwise just add a new one
					updatedLinks.add(link);
				}
			}
		} else {
			logger.info("updateLinks: Empty list, returning");
		}
		return updatedLinks;
	}

	@Override
	public boolean process(JabberMessage msg) {
		if (msg.getMessage().contains("http")) {
			logger.info("Saving links: " + msg.getMessage());
			List<Link> updatedLinks = convertMessageToLinks(msg);
			updatedLinks = updateLinks(updatedLinks, msg.getUsername());
			dao.putLinks(updatedLinks);
			logger.info("Link saving complete");
			
		}
		// TODO: return better info, check for exceptions, etc.
		return true;
	}
}
