package com.dt.jira.plugin.tab;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.dt.jira.plugin.ao.SubtaskConfig;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.base.Preconditions.checkNotNull;

public class SubTaskServiceImpl implements SubTaskService {
	
	 private final ActiveObjects ao;
	

	public SubTaskServiceImpl(ActiveObjects ao) {
		 this.ao = checkNotNull(ao);
	}

	@Override
	public SubtaskConfig save(String projectKey, String subTaskName,String subTaskId,
			boolean isEnable) {
	
		 //see if this jiraProject Key already exists
    	SubtaskConfig subtask =  null; //getSubtaskConfig(projectKey, subTaskId);
    	//if not, create a new entity row
		if (subtask == null) subtask = ao.create(SubtaskConfig.class);
		subtask.setSubTaskId(subTaskId);
		subtask.setSubTaskName(subTaskName);
		subtask.setEnabled(isEnable);
		subtask.setProjectKey(projectKey);
		
		subtask.save();
        return subtask;
		
	}

	@Override
	public List<SubtaskConfig> find(String projectKey) {
		 List<SubtaskConfig> subTaskList= new ArrayList<SubtaskConfig>();
		SubtaskConfig[] result = ao.find(SubtaskConfig.class, "PROJECT_KEY = ?", projectKey);
		for (SubtaskConfig subTsk : result) {
			subTaskList.add(subTsk);
        }
		return subTaskList;
	}

	@Override
	public List<SubtaskConfig> getSubTasks() {
		List<SubtaskConfig> lst = newArrayList(ao.find(SubtaskConfig.class));
		return lst;
	}

	/*@Override
	public SubtaskConfig getSubtaskConfig(String subtaskId) {
		List<SubtaskConfig> lst = newArrayList(ao.find(SubtaskConfig.class));
		for (SubtaskConfig subTsk : lst) {
			if(subtaskId.equalsIgnoreCase(subTsk.getSubTaskId())) return subTsk; 
        }
		return null;
	}*/
	
	@Override
	public SubtaskConfig getSubtaskConfig(String projectKey, String subtaskId) {
		List<SubtaskConfig> lst = newArrayList(ao.find(SubtaskConfig.class));
		for (SubtaskConfig subTsk : lst) {
			if(subTsk.getProjectKey().equals(projectKey) && subtaskId.equalsIgnoreCase(subTsk.getSubTaskId())) return subTsk; 
        }
		return null;
	}
	@Override
	public void delete(String projectKey) {	
		SubtaskConfig[] result = ao.find(SubtaskConfig.class, "PROJECT_KEY = ?", projectKey);;
		ao.delete(result);
	}
}
