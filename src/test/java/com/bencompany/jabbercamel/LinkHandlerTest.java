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
import com.bencompany.jabbercamel.modules.LinkHandler;

import java.util.List;

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
		List<Link> links = null;
		try {
			links = lh.convertMessageToLinks(msg);
		} catch (Exception e) {
			fail("Couldnt convert message to link");
		}
		if (links.get(0).getUrl().equals("http://google.com/fwd.txt")) {

		} else {
			fail("Message not stripped correctly");
		}
	}

	// TODO: this
	//@Test
	public void NoHttpInLink() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("www.reddit.com");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		List<Link> links = null;
		try {
			links = lh.convertMessageToLinks(msg);
		} catch (Exception e) {
			fail("Couldnt convert message to link");
		}
		if (links.get(0).getUrl().equals("www.reddit.com")) {
			// pass
		} else {
			fail("Didn't pick up www URL");
		}
	}

	@Test
	public void TestTwoLinksAreHandledOkay() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://reddit.com http://google.com");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		List<Link> links = null;
		try {
			links = lh.convertMessageToLinks(msg);
		} catch (Exception e) {
			fail("Couldnt convert message to link");
		}
		boolean pass = true;
		for (Link link : links) {
            pass = pass &&
                    (link.getUrl().equals("http://reddit.com")
                    || link.getUrl().equals("http://google.com"));
		}

        if (!pass)
			fail("Can't handle two URL's");
	}

	// TODO: randomise URL or include previous URL count
	//@Test
	public void testLinkPersists() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://google.com/fwd.txt This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		Link link;
		try {
			lh.process(msg);
			link = dao.getLinks("http://google.com/fwd.txt").get(0);
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
