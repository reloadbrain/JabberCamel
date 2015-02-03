package com.bencompany.jabbercamel.camel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.utils.GoogleUrlShortener;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;

@Component
public class ChatHandler {

	@EndpointInject(uri="direct:chatResponse")
	  ProducerTemplate producer;
	
	public void handleMessage(JabberMessage msg) {
	
		if (msg.getMessage().contains("rss")) {
			producer.sendBody(GoogleUrlShortener.shorten("http://bencompany.net"));
			/*
			try {
				getRss();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FeedException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
	
	/*public void getRss() throws IllegalArgumentException, FeedException, IOException {
		SyndFeedInput input = new SyndFeedInput();
		URL feedUrl = new URL("http://www.abc.net.au/news/feed/51120/rss.xml");
		URLConnection conn = feedUrl.openConnection();
		XmlReader xml = new XmlReader(conn);
		SyndFeed feed = input.build(xml);
		StringBuilder sb = new StringBuilder();
		sb.append(feed.getAuthor() + System.getProperty("line.separator"));
		ArrayList<SyndEntry> entries = null;
		entries = (ArrayList<SyndEntry>) feed.getEntries();
		for (int i = 0; i < 5; i++ ){ 
			SyndEntry entry = entries.get(i);
			sb.append(entry.getTitle());
			sb.append(entry.getLink() + System.getProperty("line.separator"));
		}
		producer.sendBody(sb.toString());
	}*/
	
	public void getRss() {
		String resp = "<html xmlns='http://jabber.org/protocol/xhtml-im'>"
				+ "<body xmlns='http://www.w3.org/1999/xhtml'>"
				+ "<span style='font-size:20px; color:green>Hey!</span></body></html>";
		producer.sendBody(resp);
	}
	 
}
