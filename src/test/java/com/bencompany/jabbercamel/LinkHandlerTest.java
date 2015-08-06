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
		msg.setMessage("Hello There! http://google.com/fwdi.txt This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		List<Link> links = null;
		try {
			links = lh.convertMessageToLinks(msg);
		} catch (Exception e) {
			fail("Couldnt convert message to link");
		}
		if (links.get(0).getUrl().equals("http://google.com/fwdi.txt")) {

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
		pass = pass && (links.get(0).getUrl().equals("http://reddit.com")
                    && (links.get(1).getUrl().equals("http://google.com")));

        if (!pass)
			fail("Can't handle two URL's");
	}

	// TODO: randomise URL or include previous URL count
	@Test
	public void testLinkPersists() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://google.com/fwd.txt This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		List<Link> links;
		try {
			lh.process(msg);
			links = dao.getLinks(lh.convertMessageToLinks(msg));
			if ((links == null) || (links.isEmpty())) {
				fail("No links returned by dao");
			}
			if (links.size() != 1) {
				fail("Dao returned wrong number of links");
			}
			Link link = links.get(0);
			assert(link.getOccurences() == 1);
			if (link.getOccurences() != 1) {
				fail("Link count meant to be 1 but is:" + link.getOccurences());
			}
			if (!link.getUrl().equals("http://google.com/fwd.txt")) {
				fail("Link urls are different");
			}
		}catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testMultipleLinksPersist() {
		JabberMessage msg = new JabberMessage();
		msg.setMessage("Hello There! http://google.com/bwt.txt http://github.com This is a test");
		msg.setId(1);
		msg.setUsername("Test");
		msg.setTimestamp("NOW!");
		List<Link> links;
		try {
			lh.process(msg);
			links = dao.getLinks(lh.convertMessageToLinks(msg));
			if ((links == null) || (links.isEmpty())) {
				fail("No links returned by dao");
			}
			if (links.size() != 2) {
				fail("Dao returned wrong number of links");
			}
			assert (links.get(0).getOccurences() == 1);
			if (links.get(0).getOccurences() != 1) {
				fail("Link count meant to be 1 but is:" + links.get(0).getOccurences());
			}
			assert (links.get(1).getOccurences() == 1);
			if (links.get(1).getOccurences() != 1) {
				fail("Link count meant to be 1 but is:" + links.get(1).getOccurences());
			}
			if (!links.get(0).getUrl().equals("http://google.com/bwt.txt")) {
				fail("First link url is different");
			}
			if  (!links.get(1).getUrl().equals("http://github.com")) {
				fail("Second link url is different");
			}
		}catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testRepostOneLinkPersist() {
		JabberMessage msg1 = new JabberMessage();
		msg1.setMessage("Hello There! http://google.com/bwt5.txt http://github.com/git1 This is a test");
		msg1.setId(1);
		msg1.setUsername("Test1");
		msg1.setTimestamp("NOW!");

		JabberMessage msg2 = new JabberMessage();
		msg2.setMessage("Hello There! http://google.com/bwt5.txt");
		msg2.setId(1);
		msg2.setUsername("Test2");
		msg2.setTimestamp("NOW!");

		List<Link> links;
		try {
			lh.process(msg1);
			lh.process(msg2);

			//this call will return the 2 links from message 1, but the repeated google link
			//should have count == 2 and user = Test2
			links = dao.getLinks(lh.convertMessageToLinks(msg1));

			if ((links == null) || (links.isEmpty())) {
				fail("No links returned by dao");
			}
			if (links.size() != 2) {
				fail("Dao returned wrong number of links");
			}
			assert (links.get(0).getOccurences() == 2);
			if (links.get(0).getOccurences() != 2) {
				fail("Link count meant to be 1 but is:" + links.get(0).getOccurences());
			}
			assert (links.get(1).getOccurences() == 1);
			if (links.get(1).getOccurences() != 1) {
				fail("Link count meant to be 1 but is:" + links.get(1).getOccurences());
			}
			if (!links.get(0).getUrl().equals("http://google.com/bwt5.txt")) {
				fail("First link url is different");
			}
			if  (!links.get(1).getUrl().equals("http://github.com/git1")) {
				fail("Second link url is different");
			}
			if (!links.get(0).getOp().equals("Test1")) {
				fail("First link op is incorrect");
			}
			if (!links.get(0).getLastPostedBy().equals("Test2")) {
				fail("First link lastPostedBy is incorrect");
			}
			if  (!links.get(1).getOp().equals("Test1")) {
				fail("Second link op is incorrect");
			}
			if  (!links.get(1).getLastPostedBy().equals("Test1")) {
				fail("Second link lastPostedBy is incorrect");
			}
		}catch (Exception e) {
			fail();
		}
	}
}
