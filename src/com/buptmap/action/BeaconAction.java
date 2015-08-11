package com.buptmap.action;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.buptmap.Service.BeaconService;
import com.buptmap.Service.StaffService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author baoke
 *
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class BeaconAction extends ActionSupport {
	private String building_id;
	private String floor_id;
	private String mac_id;
	private String unitid;
	private String company_id;
	private Map<String,Object> resultObj;
	private BeaconService beaconService;
	private StaffService staffService;
	private String id;
	private String pwd;
	private String jsonstr;
	private String user_id;
	private String key;

	public String getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}
	public String getMac_id() {
		return mac_id;
	}
	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	public BeaconService getBeaconService() {
		return beaconService;
	}
	public void setBeaconService(BeaconService beaconService) {
		this.beaconService = beaconService;
	}
	public StaffService getStaffService() {
		return staffService;
	}
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getJsonstr() {
		return jsonstr;
	}
	public void setJsonstr(String jsonstr) {
		try {
			this.jsonstr = new String(jsonstr.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	//根据mac_id查找beacon
	@SuppressWarnings("unchecked")
	public String findone(){
		JSONArray beaconArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			beaconArray = beaconService.findOne(mac_id);
		
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

	//下载最新db数据
	@SuppressWarnings("unchecked")
	public String download() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println("action");
			if (beaconService.download(unitid, company_id)) {
				map.put("success", true);
				
				map.put("file", "d:/apache-tomcat-7.0.56-windows-x64/apache-tomcat-7.0.56/webapps/testMap/NEWBEACONDEPOY902.db");//"C://Users//baoke//Desktop//IBeacon//testMap//test.db"
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
				return "failed";
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "failed";
		}
	
		
	}
	
	//查找某个公司部署过beacon的建筑
	public String getbuilding(){
		JSONArray buildingArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			buildingArray = beaconService.getbuilding(company_id);
		
			if (buildingArray != null && buildingArray.size() != 0) {
				map.put("success", true);
				map.put("total", buildingArray.size());
				map.put("building", buildingArray);
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
			if(buildingArray != null) { buildingArray.clear(); buildingArray = null; }
		}
	}
	
	//查找某个公司在某个建筑内部署的IBeacon列表
	public String findbybuilding(){
		System.out.println("findbybuilding");
		JSONArray beaconArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			beaconArray = beaconService.findbybuilding(building_id,company_id);
		
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
	
	//查找某个员工id部署的 所有beacon
	public String findbystaff(){
		System.out.println("findbystaff");
		JSONArray beaconArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			beaconArray = beaconService.findbystaff(user_id);
		
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

	//关于员工权限的单独验证
	public String test(){
			Map<String, Object> map = new HashMap<String, Object>();
	    	JSONArray resultArray = new JSONArray();
	    	String resultString;
			try {	
				if (user_id == null || key == null) {
		    		map.put("success", false);
					map.put("message", "id or key为空");
					resultObj = JSONObject.fromObject(map);
		        	return SUCCESS;
				}
				/*
		    	if( !staffService.verify(user_id, key)){
		        	map.put("success", false);
					map.put("message", "tempid_time_out");
					resultObj = JSONObject.fromObject(map);
					return SUCCESS;
		        }
*/
		    	resultString = beaconService.userSession(user_id);
		    	System.out.println(resultString);
		    	if (resultString.equals("all")) {
					map.put("success", true);
					map.put("role", "all");
					resultObj = JSONObject.fromObject(map);
					return "success";
				}
		    	else if (resultString != null && resultString.length() > 0 ) {
		    		resultArray = JSONArray.fromObject(resultString);
		    		map.put("success", true);
					map.put("role", resultArray);
					resultObj = JSONObject.fromObject(map);
					return "success";
		    		
				}
				else {
					map.put("success", false);
					resultObj = JSONObject.fromObject(map);
					return "success";
				}
				
				
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
	
	
}
