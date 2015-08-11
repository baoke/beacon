package com.buptmap.action;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.BeaconService;
import com.buptmap.Service.StaffService;
import com.buptmap.util.MD5Util;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author baoke
 *
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class StaffAction extends ActionSupport{
	private StaffService staffService;
	private BeaconService beaconService;
	private String user_id;
	private String major;
	private String minor;
	private String pwd;
	private String start;
	private String end;
	private Map<String,Object> resultObj;
	private String jsonstr;
	private String filepath;
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public BeaconService getBeaconService() {
		return beaconService;
	}

	public void setBeaconService(BeaconService beaconService) {
		this.beaconService = beaconService;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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
		try {
			this.jsonstr = new String(jsonstr.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String login() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			System.out.println(pwd);
			String pwd_md5 = MD5Util.string2MD5(pwd);
			resultObj = staffService.log_in(user_id, pwd_md5);
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

	public String findone(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = staffService.findone(user_id);
				
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("staff", resultArray);
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

	//查找某个管理员or代理的所有下级
	public String findall(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = staffService.findall(user_id);
				
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("staff", resultArray);
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

	
	public String edit(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
				
			if (!staffService.verify_session(jsonstr)) {
				map.put("success", false);
				map.put("message", "权限分配不合理");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			if (staffService.edit(jsonstr)) {
				map.put("success", true);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "该用户不存在！");
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
	
	public String add(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
				
			if (!staffService.verify_session(jsonstr)) {
				map.put("success", false);
				map.put("message", "权限分配不合理");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			if (staffService.add(jsonstr)) {
				map.put("success", true);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			else {
				map.put("success", false);
				map.put("message", "该用户ID已存在");
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
	}
	
	public String delete(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
				
			if (staffService.delete(user_id)) {
				map.put("success", true);
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
	/*
	public String test_l() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			System.out.println(jsonstr);
			JSONArray jsonArray1=JSONArray.fromObject(jsonstr);
			String jsonString="[{\"value\":\"FDA50693-A4E2-4FB1-AFCF-C6EB07647825\",\"majors\":[{\"value\":\"10004\",\"sections\":[{\"value0\":\"0\",\"value1\":\"0\"},{\"value0\":\"57000\",\"value1\":\"57001\"},{\"value0\":\"57113\",\"value1\":\"57113\"}]}]}]";
			JSONArray jsonArray2=JSONArray.fromObject(jsonString);
			JSONArray result = staffService.find_differ_session(jsonArray1, jsonArray2);
			if (result!=null&&result.size()>0) {
				map.put("success", true);
				map.put("result",result );
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			else {
				map.put("success", false);
				map.put("resulr", "空");
				resultObj = JSONObject.fromObject(map);
				return "success";				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		
	}
	*/
	
	//按条件jsonstr查询beacon
	public String findbeacon(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
			
			JSONArray resultArray = beaconService.findBeacon(jsonstr);
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("Beacon", resultArray);
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
	
	//李晋调用的接口
	public String addSession(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (filepath == null || filepath.equals("")) {
			map.put("success", false);
			map.put("message", "数据传送为空");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		File file = new File(filepath);
		if (!file.exists()){
			map.put("success", false);
			map.put("message", "文件不存在");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		try {
			System.out.println(filepath);
			resultObj = staffService.add_session(filepath);
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
	
	public String find_vacant(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = staffService.find_vacant(user_id);
				
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("vacant", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "无空闲ID");
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

/*	public String upload_xls(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
				
			if (staffService.upload_xls(user_id)) {
				map.put("success", true);
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
	}*/
	
	public String test(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
				
			if (staffService.test()) {
				map.put("success", true);
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
	
}
