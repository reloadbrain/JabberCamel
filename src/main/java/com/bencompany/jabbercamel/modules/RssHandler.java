package com.bencompany.jabbercamel.modules;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.utils.ChatUtils;
import com.bencompany.jabbercamel.utils.GoogleUrlShortener;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class RssHandler implements Module {
	Logger logger = Logger.getLogger(RssHandler.class);
	
	@Autowired
	GoogleUrlShortener urlShortener;

	
	@Override
	public boolean process(JabberMessage msg) {
		if (msg.getMessage().contains("abc")) {
			try {
				String user = msg.getUsername();
				logger.info("Getting RSS feed for user:" + msg.getUsername());
				
				// get feed and convert to ArrayList
				SyndFeedInput input = new SyndFeedInput();
				URL feedUrl = new URL("http://www.abc.net.au/news/feed/51120/rss.xml");
				URLConnection conn = feedUrl.openConnection();
				XmlReader xml = new XmlReader(conn);
				SyndFeed feed = input.build(xml);
				StringBuilder sb = new StringBuilder();
				ArrayList<SyndEntry> entries = null;
				entries = (ArrayList<SyndEntry>) feed.getEntries();
				
				// construct string response
				sb.append(feed.getAuthor() + System.getProperty("line.separator"));
				for (int i = 0; i < 5; i++ ){ 
					SyndEntry entry = entries.get(i);
					sb.append(entry.getTitle() + "  ");
					sb.append(urlShortener.shorten(entry.getLink()) + System.getProperty("line.separator"));
				}
				logger.info("Sending RSS to user:" + user);
				ChatUtils.message(user, sb.toString());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		}
		return true;
	}
}
