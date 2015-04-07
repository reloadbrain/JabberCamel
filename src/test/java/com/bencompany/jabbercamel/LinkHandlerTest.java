package com.bencompany.jabbercamel;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bencompany.jabbercamel.camel.*;
import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:test-context.xml")
public class LinkHandlerTest {

	@Autowired LinkHandler lh;
	@Autowired JabberDao dao;
	
	@Test
	public void testLinkStripsCorrectly() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://google.com/fwd.txt This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		Link link = null;
		try {
			link = lh.convertMessageToLink(msg);
		} catch (Exception e) {
			fail("Couldnt convert message to link");
		}
		if (link.getUrl().equals("http://google.com/fwd.txt")) {
			
		} else {
			fail("Message not stripped correctly");
		}
	}
	
	@Test
	public void testLinkPersists() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://google.com/fwd.txt This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		Link link;
		try {
			lh.putLink(msg);
			link = dao.getLink("http://google.com/fwd.txt");
			assert(link.getCount() == 1);
			if (link.getCount() != 1) {
				fail("Link count meant to be 1 but is:" + link.getCount());
			}
		}catch (Exception e) {
			fail();
		}
		fail("I'm bad at logic");
	}

}
