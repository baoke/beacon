package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Project;


@Component("projectDao")

public class ProjectDAO {
	
	private HibernateTemplate hibernateTemplate = null;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray findall(){
		List<Project>result=new ArrayList<Project>();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		try {
			result = hibernateTemplate.find("from Project ");
			//result = hibernateTemplate.find("from Staff s");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					Project temProject=result.get(i);
					jsonObject.put("project_id", temProject.getProject_id());
					jsonObject.put("title",temProject.getTitle());
					jsonObject.put("staff_id", temProject.getStaff_id());
					jsonObject.put("address", temProject.getAddress());
					jsonObject.put("begin", temProject.getBegin());
					jsonObject.put("end", temProject.getEnd());
					jsonObject.put("description", temProject.getDescription());
					jsonObject.put("time", temProject.getTime());
			
					jsonArray.add(jsonObject);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		return jsonArray;
	}
	public JSONArray findproject(String staff_id){
		List<Project>result=new ArrayList<Project>();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		try {
			/*
			if (staff_id.equalsIgnoreCase("super")) {
				jsonArray=findall();
				return jsonArray;				
			}
			else {
			//准备查看子代理的子代理，递归
				List<Staff>staffs = new ArrayList<Staff>();
				staffs = hibernateTemplate.find("from Staff s where s.parent_id='"+staff_id+"'");
				if (staffs!=null && staffs.size()>0) {
					for (int i = 0; i < staffs.size(); i++) {
						Staff findsStaff =staffs.get(i);
						System.out.println("前："+findsStaff.getStaff_id()+jsonArray.size());
						if (findproject(findsStaff.getStaff_id())!=null && findproject(findsStaff.getStaff_id()).size()>0) {
							jsonArray.add(findproject(findsStaff.getStaff_id()));
						}
						System.out.println("后："+jsonArray.size());
					}
				}
				result = hibernateTemplate.find("from Project p where p.staff_id='"+staff_id+"'");
				if (result != null && result.size() > 0) {
					for (int i = 0; i < result.size(); i++) {
						Project temProject=result.get(i);
						jsonObject.put("project_id", temProject.getProject_id());
						jsonObject.put("title",temProject.getTitle());
						jsonObject.put("staff_id", temProject.getStaff_id());
						jsonObject.put("address", temProject.getAddress());
						jsonObject.put("begin", temProject.getBegin());
						jsonObject.put("end", temProject.getEnd());
						jsonObject.put("description", temProject.getDescription());
						jsonObject.put("time", temProject.getTime());
				
						jsonArray.add(jsonObject);
					}	
				}
			}
			*/
			if (staff_id.equalsIgnoreCase("super")) {
				jsonArray=findall();
				return jsonArray;				
			}
			System.out.println("from Project p where p.staff_id=' "+staff_id+" '");
			result = hibernateTemplate.find("from Project p where p.staff_id='"+staff_id+"'");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					Project temProject=result.get(i);
					jsonObject.put("project_id", temProject.getProject_id());
					jsonObject.put("title",temProject.getTitle());
					jsonObject.put("staff_id", temProject.getStaff_id());
					jsonObject.put("address", temProject.getAddress());
					jsonObject.put("begin", temProject.getBegin());
					jsonObject.put("end", temProject.getEnd());
					jsonObject.put("description", temProject.getDescription());
					jsonObject.put("time", temProject.getTime());
			
					jsonArray.add(jsonObject);
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("final："+jsonArray.size());
		return jsonArray;
	}
	
	public boolean add(String jsonString){
		//jsonArray=JSONArray.fromObject(jsonString);
		try {
			jsonObject=JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Project>result=new ArrayList<Project>();
		result=hibernateTemplate.find("from Project p where p.staff_id= '"+jsonObject.getString("staff_id")+"'");
		//if(result.size()==0)
		//{
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Project temProject=new Project();
			temProject.setAddress(jsonObject.getString("address"));
			temProject.setBegin(jsonObject.getString("begin"));
			temProject.setDescription(jsonObject.getString("description"));
			temProject.setEnd(jsonObject.getString("end"));
			//temProject.setProject_id(Integer.valueOf(jsonObject.getString("project_id")));
			temProject.setStaff_id(jsonObject.getString("staff_id"));
			//temProject.setTime(jsonObject.getString("time"));
			temProject.setTime(date.toString());
			temProject.setTitle(jsonObject.getString("title"));
			
			hibernateTemplate.save(temProject);
			
			return true;
		//}
		//else {
			//return false;
		//}
		
		
	}
	public boolean edit(String jsonString){
		jsonObject=JSONObject.fromObject(jsonString);
		List<Project>result=new ArrayList<Project>();
		result=hibernateTemplate.find("from Project p where p.project_id= "+Integer.valueOf(jsonObject.getString("project_id")));
		if(result.size()==1)
		{
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Project temProject=result.get(0);
			temProject.setAddress(jsonObject.getString("address"));
			temProject.setBegin(jsonObject.getString("begin"));
			temProject.setDescription(jsonObject.getString("description"));
			temProject.setEnd(jsonObject.getString("end"));
			//temProject.setProject_id(Integer.valueOf(jsonObject.getString("project_id")));
			temProject.setStaff_id(jsonObject.getString("staff_id"));
			//temProject.setTime(jsonObject.getString("time"));
			temProject.setTime(date.toString());
			temProject.setTitle(jsonObject.getString("title"));
			
			hibernateTemplate.update(temProject);			
			return true;
		}
		else {
			return false;
		}
		
	}
	
	
}

