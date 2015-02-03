package com.bencompany.jabbercamel;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "/test-context.xml")
public class ModelTests {
	
	@Test
	public void createJabberMessage() {
		JabberMessage msg = new JabberMessage("Ben", "13/14/1991", "Hello World!");
		assertEquals("Hello World!",msg.getMessage());
		assertEquals("Ben",msg.getUsername());
		assertEquals( "13/14/1991",msg.getTimestamp());
	}

	

}
