package com.dt.jira.plugin.tab;

import java.util.List;
import java.util.Map;

import com.atlassian.activeobjects.tx.Transactional;
import com.dt.jira.plugin.ao.SubtaskConfig;


@Transactional
public interface SubTaskService {
	
	SubtaskConfig save(String projectKey, String subtaskName,String subtaskId, boolean isEnable);	
	SubtaskConfig getSubtaskConfig(String projectKey,String subtaskId);
	List<SubtaskConfig> find(String projectKey);
	List<SubtaskConfig> getSubTasks();
	void delete(String projectKey);	
}
