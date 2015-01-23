package com.bencompany.jabbercamel.camel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.bencompany.jabbercamel.model.JabberMessage;
import com.bencompany.jabbercamel.model.User;

/*
 * Database access to persist and retrieve Jabber messages and users
 */
@Repository
public class JabberDao {
	
	@PersistenceUnit
	EntityManagerFactory emf;
	EntityManager em;
	
	public JabberDao() { }
	
	@PostConstruct
	public void createEntityManager() {
		em = emf.createEntityManager();
	}
	/*
	 * Retrieves last 20 messages from DB as JabberMessage
	 */
	@SuppressWarnings("unchecked")
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
	
	/*
	 * Saves JabberMessage to database, and persists the updateduser.
	 */
	public void putMessage(JabberMessage msg) throws Exception {
		User updatedUser = updateUser(msg.getUsername());

		em.getTransaction().begin();
		em.persist(updatedUser);
		em.persist(msg);
		em.getTransaction().commit();
	}
	
	/*
	 * Updates the Message Count of a User. Create user if it doesn't exist, returns user to persist
	 */
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
	
}
