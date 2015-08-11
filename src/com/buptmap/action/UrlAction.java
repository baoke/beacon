package com.buptmap.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.buptmap.Service.UrlService;
import com.opensymphony.xwork2.ActionSupport;
/**
 * @author weiier
 */

@Component
@Scope("prototype")
public class UrlAction extends ActionSupport {

	private String jsonstr;
	private String json;
	private String staff_id;
	private int urlId;
	private String device_id;
	public String local_logo;
	
	private Map<String,Object> resultObj;
	private UrlService urlService;
	
	public String add() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			System.out.println(jsonstr+"hhhhh");		
			map = urlService.addMessage(jsonstr);
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			System.out.println(jsonstr+"ggggg");
			map = this.urlService.editMessage(jsonstr);
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	
	public String delete() throws Exception {
		try{		
			resultObj = urlService.delete(urlId);
			if(resultObj == null) {	
				resultObj = new JSONObject();
				resultObj.put("success", false);
				resultObj.put("message", "URLs doesn't exist,check your parameter");
			}
		}catch(Exception e){
			e.printStackTrace();
			resultObj = new JSONObject();
			resultObj.put("success", false);
			resultObj.put("message", e.toString());
		}
		return SUCCESS;
	}
	
	public String check() throws Exception {
		JSONArray messageArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			messageArray = urlService.checkDevice(jsonstr);
			boolean url = urlService.checkMessage(jsonstr);
			
			if ((messageArray != null && messageArray.size() != 0) || !url) {
				map.put("success", false);
				map.put("total", messageArray.size());
				map.put("device", messageArray);
				map.put("url",url);
			}else{
				map.put("success",true);
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	public String showList() throws Exception {
		JSONArray messageArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//messageArray = urlService.showList(staff_id);
			messageArray = urlService.showListWithSession(staff_id);
			if (messageArray != null && messageArray.size() != 0) {
				map.put("success", true);
				map.put("total", messageArray.size());
				map.put("message", messageArray);
			}else{
				map.put("success",false);
				map.put("message", "URLs doesn't exist,check your parameter");
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	public String showDeList() throws Exception {
		JSONArray messageArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			messageArray = urlService.showDetailWithSession(urlId, staff_id);
			if (messageArray != null && messageArray.size() != 0) {
				map.put("success", true);
				map.put("total", messageArray.size());
				map.put("message", messageArray);
			}else{
				map.put("success",false);
				map.put("message", "URLs doesn't exist,check your parameter");
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	public String getLogoUrl() throws Exception {
		Map<String, String> logo_url = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{	
			logo_url = urlService.getLogoUrl(local_logo);
			if(logo_url.containsKey("url")){
				map.put("success", true);
				map.put("url", logo_url.get("url"));
			}else{
				map.put("success", false);
				map.put("message", "获取服务器logo失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
		}
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getLocal_logo() {
		return local_logo;
	}

	public void setLocal_logo(String local_logo) {
		this.local_logo = local_logo;
	}

	public String getJsonstr() {
		return jsonstr;
	}
	
	public void setJsonstr(String jsonstr) {
		try {
			this.jsonstr =new String( jsonstr.getBytes("ISO-8859-1"),"UTF-8"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		try {
			this.json =new String( json.getBytes("ISO-8859-1"),"GBK"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}


	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public UrlService getUrlService() {
		return urlService;
	}

	@Resource
	public void setUrlService(UrlService urlService) {
		this.urlService = urlService;
	}
	
	
}