package com.bencompany.jabbercamel.camel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.Link;
import com.bencompany.jabbercamel.model.User;

/*
 * Database access to persist and retrieve Jabber messages and users
 */
@Repository
public class JabberDao {
	
	/*
	 * Use a PersistenceContext to inject EntityManagers. Use a PersistenceUnit to inject an EntityManagerFactory
	 * if you want to manually control your EntityManagers
	 */
	@PersistenceContext
	EntityManager em;
	
	public JabberDao() { }
	
	@PostConstruct
	public void createEntityManager() {
	}
	/*
	 * Retrieves last 20 messages from DB as JabberMessage
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JabberMessage> getLast20Messages() {
		Query query = em.createNativeQuery("SELECT * FROM Message ORDER BY id DESC LIMIT 20", JabberMessage.class);
		List<JabberMessage> results = query.getResultList();
		return results;
	}
	
	/*
	 * Retrieves users from DB as User
	 */
	@SuppressWarnings("unchecked")
	public List<User> getActiveUsers() {
		Query query = em.createNativeQuery("SELECT * FROM User ORDER BY messageCount DESC", User.class);
		List<User> results = query.getResultList();
		return results; 
	}
	
	@Transactional
	public List<Link> getLinks(String url) {
		try {
			Query query = em.createNativeQuery("SELECT * FROM Link WHERE url = ?", Link.class);
			query.setParameter(1, url);
			List<Link> links = query.getResultList();
			return links;
		} catch (Exception e){
			return null;
		}
		
	}
	@Transactional
	public List<Link> getPopularLinks() {
		try {
			Query query = em.createNativeQuery("SELECT * FROM Link ORDER BY count DESC", Link.class);
			List<Link> links = query.getResultList();
			return links;
		} catch (Exception e){
			return null;
		}
	}
	/*
	 * Saves JabberMessage to database, and persists the updated user.
	 */
	@Transactional
	public void putMessage(JabberMessage msg) throws Exception {
		User updatedUser = updateUser(msg.getUsername());
		em.persist(updatedUser);
		em.persist(msg);
	}
	
	/*
	 * Updates the Message Count of a User. Create user if it doesn't exist, returns user to persist
	 */
	@Transactional
	private User updateUser(String user) {
		Query getUserQuery = em.createNativeQuery("SELECT * FROM User WHERE userName = ?1", User.class);
		getUserQuery.setParameter(1, user);
		
		User updatedUser = null;
		boolean userCreated = false;
		
		try {
		updatedUser = (User) getUserQuery.getSingleResult();
		} catch (Exception e) {
			updatedUser = new User();
			updatedUser.setUserName(user);
			updatedUser.setMessageCount(1);
			userCreated = true;
		} 
		
		if (!userCreated){
			long messageCount = updatedUser.getMessageCount();
			updatedUser.setMessageCount(++messageCount);
		}
		return updatedUser;
		
	}

	@Transactional
	public void putLinks(List<Link> existingLinks) {
		em.persist(existingLinks);
	}
}
