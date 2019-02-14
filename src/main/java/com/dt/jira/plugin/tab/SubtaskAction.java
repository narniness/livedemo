package com.dt.jira.plugin.tab;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Enumeration;
import com.atlassian.jira.web.ExecutingHttpRequest;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.dt.jira.plugin.ao.SubtaskConfig;
import webwork.action.ServletActionContext;

public class SubtaskAction extends JiraWebActionSupport{
	
	private String jiraProjectKey;
	private SubTaskService subTaskService;
	
	public SubTaskService getSubTaskService() {
		return subTaskService;
	}

	public void setSubTaskService(SubTaskService subTaskService) {
		this.subTaskService = subTaskService;
	}

	public String getJiraProjectKey() {
		return jiraProjectKey;
	}

	public void setJiraProjectKey(String jiraProjectKey) {
		this.jiraProjectKey = jiraProjectKey;
	}
	public SubtaskAction(SubTaskService subTaskService){
		this.subTaskService =subTaskService; 
	}
	@Override
	public String doDefault() throws Exception {
		this.request = ExecutingHttpRequest.get();
	
		//System.out.println("***************** before delete ***************" + jiraProjectKey);
		subTaskService.delete(jiraProjectKey);
		
		//System.out.println("***************** after delete  ***************"+jiraProjectKey);
		
		Enumeration<String>  enumeration= ExecutingHttpRequest.get().getParameterNames();
		while(enumeration.hasMoreElements()){
			  String key = enumeration.nextElement();
			  if(key.equals("jiraProjectKey") || key.equals("Save"))
				continue;
			
			 String tValue = ExecutingHttpRequest.get().getParameter(key);
			 if(!tValue.equals("true")){
					boolean disabled = false;
					subTaskService.save(jiraProjectKey, "Todo", key, disabled);
					 //System.out.println("key : "+key + " tValue : "+tValue);
			 }
		}	 
		this.request.getSession().setAttribute("fromTab","false");
		ServletActionContext.getResponse().sendRedirect(
				ServletActionContext.getRequest().getContextPath() + "/browse/"
						+ jiraProjectKey);
		return NONE;
	}
}
