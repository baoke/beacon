package com.buptmap.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.BeaconService;
import com.buptmap.Service.PatrolService;
import com.buptmap.Service.StaffService;
import com.buptmap.model.Testbeacon;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class PatrolAction extends ActionSupport {
	
	private Map<String,Object> resultObj;
	private String jsonstr;
	private PatrolService patrolService;
	private StaffService staffService;
	private BeaconService beaconService;
	private String user_id;
	private String pwd;
	private String company_id;
	private String mac_id;
	private String building_id;
	private String staff_id;
	private String floor_id;
	
	
	
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}
	public String getMac_id() {
		return mac_id;
	}
	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}
	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	public String getJsonstr() {
		return jsonstr;
	}
	public void setJsonstr(String jsonstr) {
		this.jsonstr = jsonstr;
		
	}
	
	public BeaconService getBeaconService() {
		return beaconService;
	}
	public void setBeaconService(BeaconService beaconService) {
		this.beaconService = beaconService;
	}
	public PatrolService getPatrolService() {
		return patrolService;
	}
	public void setPatrolService(PatrolService patrolService) {
		this.patrolService = patrolService;
	}
	
	
	public StaffService getStaffService() {
		return staffService;
	}
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String login() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			System.out.println("111");
			resultObj = staffService.login(user_id, pwd);
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			
		}
	}
	//巡检工具上传巡检信息
	public String report(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Testbeacon> testList = new ArrayList<Testbeacon>();
    	String staff_id = null;
        HttpServletRequest request=ServletActionContext.getRequest();  
		try {
			if (jsonstr == null || jsonstr == "") {
				map.put("success", false);
				map.put("message", "没有数据");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONObject object = JSONObject.fromObject(jsonstr);
			String type = object.getString("type");
			String idString = object.getString("user_id");
			JSONArray testArray = JSONArray.fromObject(object.getString("data"));

			if (object.getString("user_id") == null || object.getString("key") == null) {
	    		map.put("success", false);
				map.put("message", "id or key为空");
				resultObj = JSONObject.fromObject(map);
	        	return SUCCESS;
			}
	    	if( !staffService.verify(object.getString("user_id"),  object.getString("key"))){
	        	map.put("success", false);
				map.put("message", "tempid_time_out");
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
	        }
	    	
			if (patrolService.update(testArray, idString)) {
				map.put("success", true);
				map.put("message", "更新成功");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			else {
				map.put("success", false);
				map.put("message", "更新失败");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "数据格式错误");
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			
		}
		
	}
	
	//部署工具上传beacon信息
	
	public String re_deploy(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Testbeacon> testList = new ArrayList<Testbeacon>();
    	String staff_id = null;
        HttpServletRequest request=ServletActionContext.getRequest();  
		try {
			System.out.println(jsonstr);
			if (jsonstr == null || jsonstr == "") {
				map.put("success", false);
				map.put("message", "没有数据");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONObject object = JSONObject.fromObject(jsonstr);
			String type = object.getString("type");
			String idString = object.getString("user_id");
			JSONArray testArray = JSONArray.fromObject(object.getString("data"));

			if (object.getString("user_id") == null || object.getString("key") == null) {
	    		map.put("success", false);
				map.put("message", "id or key为空");
				resultObj = JSONObject.fromObject(map);
	        	return SUCCESS;
			}
	    	if( !staffService.verify(object.getString("user_id"),  object.getString("key"))){
	        	map.put("success", false);
				map.put("message", "tempid_time_out");
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
	        }
	    	
			if (beaconService.re_deploy(testArray, idString)) {
				map.put("success", true);
				map.put("message", "更新成功");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			else {
				map.put("success", false);
				map.put("message", "更新失败");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "数据格式错误");
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			
		}
		
	}
	
	
	public String selectall(){

		System.out.println("findbybuilding");
		JSONArray patrolArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> conditions = new HashMap<String, String>();
		
		try {

			if (jsonstr == null || jsonstr == "") {
				map.put("success", false);
				map.put("message", "没有数据");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONObject object = JSONObject.fromObject(jsonstr);
			
			String company_id = object.get("company_id").toString();
			if(! company_id.equals("")){
				conditions.put("company_id", company_id);
				
			}
			String building_id = object.get("building").toString();
			if(! building_id.equals("")){
				conditions.put("building", building_id);
				
			}
			String staff_id = object.get("staff_id").toString();
			if(! staff_id.equals("")){
				conditions.put("staff_id", staff_id);
				
			}
			String  floor= object.get("floor1").toString();
			if(! floor.equals("")){
				conditions.put("floor", floor);
				
			}
			String  starttime= object.get("datetimepicker").toString();
			if(! starttime.equals("")){
				conditions.put("starttime", starttime);
				
			}
			String  endtime= object.get("datetimepicker2").toString();
			if(! endtime.equals("")){
				conditions.put("endtime",endtime);
				
			}

			if(conditions.isEmpty()){
				map.put("success", false);
				map.put("message", "没有数据");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			patrolArray = patrolService.selectall(conditions);
		
			if (patrolArray != null && patrolArray.size() != 0) {
				map.put("success", true);
				map.put("total", patrolArray.size());
				map.put("patrol", patrolArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(patrolArray != null) { patrolArray.clear(); patrolArray = null; }
		}
	
	}
	
	public String weijiance(){

		
		JSONArray patrolArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		try {

			
			
			patrolArray = patrolService.weijiance();
		
			if (patrolArray != null && patrolArray.size() != 0) {
				map.put("success", true);
				map.put("total", patrolArray.size());
				map.put("patrol", patrolArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(patrolArray != null) { patrolArray.clear(); patrolArray = null; }
		}
	
	}
	
	public String findone(){
		JSONArray patrolArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			patrolArray = patrolService.findone(mac_id);
		
			if (patrolArray != null && patrolArray.size() != 0) {
				map.put("success", true);
				map.put("total", patrolArray.size());
				map.put("patrol", patrolArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(patrolArray != null) {patrolArray.clear(); patrolArray = null; }
		}
	}
	
	public String getbuildingid(){
		JSONArray patrolArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {	
			
			    
			   patrolArray= patrolService.getbuildingid(mac_id);
				
			   if (patrolArray != null && patrolArray.size() != 0) {
					map.put("success", true);
					map.put("patrol", patrolArray);
					map.put("total", patrolArray.size());
				   resultObj = JSONObject.fromObject(map);
				}
				else {
					map.put("success", false);
					resultObj = JSONObject.fromObject(map);
				}
				return "success";
		} 
		catch (Exception e) {
			
			e.printStackTrace();
			map.put("success", false);
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		   
			
	}
	
	
	
	
	public String findbybuilding(){
		JSONArray patrolArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			patrolArray = patrolService.findbybuilding(building_id);
		
			if (patrolArray != null && patrolArray.size() != 0) {
				map.put("success", true);
				map.put("total", patrolArray.size());
				map.put("patrol", patrolArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(patrolArray != null) {patrolArray.clear(); patrolArray = null; }
		}
	}
	
	
	public String floorbeacon(){
		
		JSONArray beaconArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			beaconArray = patrolService.floorbeacon(building_id,floor_id);
		
			if (beaconArray != null && beaconArray.size() != 0) {
				map.put("success", true);
				map.put("total", beaconArray.size());
				map.put("beacon", beaconArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(beaconArray != null) { beaconArray.clear(); beaconArray = null; }
		}
	}
}
