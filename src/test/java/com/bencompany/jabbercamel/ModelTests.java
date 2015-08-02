package com.bencompany.jabbercamel;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;
import com.bencompany.jabbercamel.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:test-context.xml")
public class ModelTests {
	
	@Test
	public void createJabberMessage() {
		JabberMessage msg = new JabberMessage("Ben", "13/14/1991", "Hello World!");
		assertEquals("Hello World!",msg.getMessage());
		assertEquals("Ben",msg.getUsername());
		assertEquals( "13/14/1991",msg.getTimestamp());
	}
	
	@Test
	public void createLink() {
		Link link = new Link("http://google.com", 1, "testuser", "testuser2");
		assertEquals("http://google.com", link.getUrl());
		assertEquals(1, link.getCount_());
		assertEquals("testuser", link.getOp());
		assertEquals("testuser2", link.getLastPostedBy());
	}
	
	@Test
	public void createUser() {
		User testUser = new User(1, "ben", 1);
		assertEquals(1, testUser.getId());
		assertEquals("ben", testUser.getUserName());
		assertEquals(1, testUser.getMessageCount());
	}

	

}
