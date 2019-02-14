package com.dt.jira.plugin.tab;

import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.plugin.projectpanel.ProjectTabPanel;
import com.atlassian.jira.plugin.projectpanel.impl.AbstractProjectTabPanel;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.util.VelocityParamFactory;
import com.dt.jira.plugin.ao.SubtaskConfig;
/**
 * The class is defined to create the custom tab on project tab.
 * Which used to configure the subtasks at project level.
 * @author kiran.muthoju
 *
 */
public class DTProjectTabPanel extends AbstractProjectTabPanel implements
		ProjectTabPanel {
	private static final Logger log = LoggerFactory
			.getLogger(DTProjectTabPanel.class);
	// private InnotasLink innotasLink;
	/** used to check the current users permission to edit the metadata. */
	private final PermissionManager permissionManager;
	/** the project admin permission key. */
	private final JiraAuthenticationContext authenticationContext;
	/** the subtask manipulation. */
	private final SubTaskManager subTaskManager;
	
	private final SubTaskService subTaskService;

	private final int PROJECT_ADMIN = 23;
/**
 * Constructor 
 * @param permissionManager
 * @param jiraAuthenticationContext
 * @param subTaskManager
 */
	public DTProjectTabPanel(PermissionManager permissionManager,
			JiraAuthenticationContext jiraAuthenticationContext,
			SubTaskManager subTaskManager, SubTaskService subTaskService) {
		super(jiraAuthenticationContext);
		this.permissionManager = permissionManager;
		this.authenticationContext = jiraAuthenticationContext;
		this.subTaskManager = subTaskManager;
		this.subTaskService = subTaskService;

	}
	/**
	 * Is panel visible 
	 * @return boolean true/false.
	 */
	public boolean showPanel(BrowseContext context) {
		return true;
	}
	/**
	 * Get the html view on the panel
	 * @return <String> 
	 */
	@Override
	public String getHtml(BrowseContext ctx) {
		VelocityParamFactory vpf = ComponentAccessor.getVelocityParamFactory();
		Map<String,String> issueTypemap = new HashMap<String,String>();
		final Map<String, Object> params = vpf
				.getDefaultVelocityParams(authenticationContext);
		params.put("projectKey", ctx.getProject().getKey());
		params.put("projectName", ctx.getProject().getName());
		log.info("*************** " + ctx.getProject());
		System.out.println("***************DTProjectTabPanel " + ctx.getProject());
		
		try {
			params.put("hasAdminPermission",
					permissionManager.hasPermission(PROJECT_ADMIN, ctx.getProject(), authenticationContext.getLoggedInUser()));
		} catch (NoSuchMethodError e) {
			params.put("hasAdminPermission",
					permissionManager.hasPermission(PROJECT_ADMIN, ctx.getProject(), authenticationContext.getUser()));

		}

		// params.put("innotaslink", innotasLink);
		Collection<IssueType> issueTypes = subTaskManager
				.getSubTaskIssueTypeObjects();
		log.info(" Size *************** " + issueTypes.size()
				+ "String: " + issueTypes.toString());
		for (IssueType issueType : issueTypes) {
			String id = issueType.getId();
			String name = issueType.getName();
			String desc = issueType.getDescription();
			log.info(" Id : " + id + " name: " + name + "Desc: "
					+ desc);
			issueTypemap.put(id,name);

		}
		params.put("subtaskmap", issueTypemap);
		
		List<SubtaskConfig>  subTskList= subTaskService.find(ctx.getProject().getKey());
		for(int i=0;i<subTskList.size(); i++){
			SubtaskConfig  config = subTskList.get(i);
			//System.out.println(" projec key : "+ config.getProjectKey());
			//System.out.println(" Subtask id : "+ config.getSubTaskId());
			//System.out.println(" Nameey : "+ config.getSubTaskName());
			//System.out.println(" enabled : "+ config.getEnabled());			
		}
		params.put("subtskconfig", subTskList);
		
		return descriptor.getHtml("view", params);
	}

}
