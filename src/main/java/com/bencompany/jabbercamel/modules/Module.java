package com.bencompany.jabbercamel.modules;

import com.bencompany.jabbercamel.model.JabberMessage;

public interface Module {
	
	/**
	 * This method will contain the central logic for a module, and return true/false if it succeeded or failed.
	 * TODO: return better information
	 * @return
	 */
	public boolean process(JabberMessage msg);
}
