package com.dt.jira.plugin.ao;

import net.java.ao.Entity;

public interface SubtaskConfig extends Entity {
	void setSubTaskId(String subTaskId);
	void setSubTaskName(String subTaskName);
	void setEnabled(boolean enabled);	
	void setProjectKey(String projectKey);
	String getSubTaskId();
	String getProjectKey();
	String getSubTaskName();
	boolean getEnabled();

}
