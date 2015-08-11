package com.buptmap.action;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.buptmap.Service.ProjectService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Lynn
 *
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")

public class ProjectAction extends ActionSupport {
	
	private int project_id;
	private String title;
	private String staff_id;
	private String address;
	private String begin;
	private String end;
	private String description;
	private String time;
	
	
	private Map<String,Object> resultObj;
	private ProjectService projectService;
	private String jsonstring;
	
	
	
	public String getJsonstring() {
		return jsonstring;
	}
	public void setJsonstring(String jsonstring) {
		try {
			this.jsonstring =new String( jsonstring.getBytes("ISO-8859-1"),"UTF-8"); 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String findall()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray =projectService.findall();
				
			if (resultArray != null && resultArray.size() >= 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("project", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		
		
	}
	public String findproject( ){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			JSONArray resultArray =projectService.findproject(staff_id);
			
			if (resultArray != null && resultArray.size() >0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("project", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "目前没有您所属项目。");
				resultObj = JSONObject.fromObject(map);
			}			
			
			return SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
			
			
		}
		
	}
	public String add()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstring);
			if(projectService.add(jsonstring))
			{
				map.put("success", true);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
						
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
			
		}
		
		return SUCCESS;
	}

	public String edit() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstring);
			if(projectService.edit(jsonstring))
			{
				map.put("success", true);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
						
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
			
		}
		return SUCCESS;
	}
	
}
