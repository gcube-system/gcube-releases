package org.gcube.portlets.user.td.gwtservice.shared.task;

import java.io.Serializable;

/**
 * Are the possible states of a task on the service
 * 
 * 
 * @author "Giancarlo Panichi" 
 * <a href="mailto:g.panichi@isti.cnr.it">g.panichi@isti.cnr.it</a> 
 *
 */
public enum State implements Serializable{
	INITIALIZING("Initializing"), 
	IN_PROGRESS("In Progress"),
	VALIDATING_RULES("Validating Rules"),
	GENERATING_VIEW("Generating View"),
	STOPPED("Stopped"),
	SUCCEDED("Succeded"),
	ABORTED("Aborted"),
	FAILED("Failed");
	
	/**
	 * @param text
	 */
	private State(final String id) {
		this.id = id;
	}

	private final String id;

	@Override
	public String toString() {
		return id;
	}
	
	
}
