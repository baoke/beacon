package com.buptmap.action;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.BeaconcountService;


/**
 * @author baoke
 *
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")

public class BeaconcountAction {

	private BeaconcountService beaconcountService;
	private Map<String,Object> resultObj;
	private String send_id;
	private String receive_id;
	private String type_id;
	private int amount;
	private String jsonstr;
	public BeaconcountService getBeaconcountService() {
		return beaconcountService;
	}

	public void setBeaconcountService(BeaconcountService beaconcountService) {
		this.beaconcountService = beaconcountService;
	}

	public Map<String, Object> getResultObj() {
		return resultObj;
	}

	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	

	public String getSend_id() {
		return send_id;
	}

	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}

	public String getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(String receive_id) {
		this.receive_id = receive_id;
	}

	
	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	//针对一个代理和其下级，将每一次的分配记录取出返回
	public String find(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = beaconcountService.find(receive_id, send_id);
				
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("record", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		
	}
	
	//添加一个新的分配记录
	public String add() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
				
			if (beaconcountService.add(jsonstr)) {
				map.put("success", true);
				
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
	}

	
	//包含每一个下级，以及分配的数量、使用的数量
	public String total(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = beaconcountService.total(send_id);
				
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		
	}
	
	public String flag(){
		return "success";
	}
}
