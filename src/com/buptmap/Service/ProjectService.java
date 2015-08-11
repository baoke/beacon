package com.buptmap.Service;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import com.buptmap.DAO.ProjectDAO;

@Component("projectService")

public class ProjectService {
	private ProjectDAO projectDAO;

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	@Resource(name="projectDao")
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	public JSONArray findall(){
		return projectDAO.findall();		
	}
	public JSONArray findproject(String staff_id){
		return projectDAO.findproject(staff_id);
	}
	public boolean add(String jsonString){
		return projectDAO.add(jsonString);
	}
	public boolean edit(String jsonString){
		return projectDAO.edit(jsonString);
	}
	
	

}
