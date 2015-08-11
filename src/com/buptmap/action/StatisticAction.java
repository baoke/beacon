package com.buptmap.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.DAO.StaffDao;
import com.buptmap.Service.StaffService;
import com.buptmap.Service.StatisticService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Lynn-15-07-14
 *
 */
@SuppressWarnings("serial")
@Component
@Scope("prototype")

public class StatisticAction extends ActionSupport{
	
	private String staff_id;
	private String jsonstr;
	private String style;
	
	private Map<String,Object> resultObj;
	private StatisticService statisticService;
	private StaffService staffService;

	
	
	
	//test
	public StaffService getStaffService() {
		return staffService;
	}
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	//test
	
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getJsonstr() {
		return jsonstr;
	}
	public void setJsonstr(String jsonstr) {

		try {
			this.jsonstr =new String( jsonstr.getBytes("ISO-8859-1"),"UTF-8"); 			
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
	
	public StatisticService getStatisticService() {
		return statisticService;
	}
	public void setStatisticService(StatisticService statisticService) {
		this.statisticService = statisticService;
	}
	
	
	public String findbyminor() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
			if(!statisticService.Instaff(jsonstr))
			{
				map.put("success", false);
				map.put("message", "无权限做相关查询");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONArray resultArray = statisticService.findStatisticMinor(jsonstr);			
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("statistic", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "没有查到相关的统计信息");
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
	
	public String findbyurl() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
			if(!statisticService.Instaff(jsonstr))
			{
				map.put("success", false);
				map.put("message", "无权限做相关查询");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			JSONArray resultArray = statisticService.findStatisticUrl(jsonstr);			
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("statistic", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "没有查到相关的统计信息");
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

	public String findbyproject()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(jsonstr);
			if(!statisticService.Instaff(jsonstr))
			{
				map.put("success", false);
				map.put("message", "无权限做相关查询");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONArray resultArray = statisticService.findStatisticProject(jsonstr);			
			if (resultArray != null && resultArray.size() > 0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("statistic", resultArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				map.put("message", "没有查到相关的统计信息");
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
	
	public String findall() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.print(staff_id);
			System.out.println("   +"+style);
			JSONArray resultArray = statisticService.findStasticAll(staff_id, style);
			if (resultArray!=null && resultArray.size()>0) {
				map.put("success", true);
				map.put("total", resultArray.size());
				map.put("statistic", resultArray);
				resultObj = JSONObject.fromObject(map);				
			} else {
				map.put("success", false);
				map.put("message", "没有查到相关的统计信息");
				resultObj = JSONObject.fromObject(map);
			}
			
			return SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "Error:"+e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		} 
		
	}
	
	public String unusedev() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray resultArray = staffService.unused_dev(staff_id);
			
			if (resultArray!=null && resultArray.size()>0) {
				map.put("success", true);
				map.put("session", resultArray);
				resultObj = JSONObject.fromObject(map);				
			} else {
				map.put("success", false);
				map.put("message", "您的权限内没有未绑定的设备！");
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "Error:"+e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		
		
	}
	/*
	public String uploadwangfeng() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (statisticService.UploadWangfeng(staff_id)) {
				map.put("success", true);
				map.put("message", "汪峰演唱会会绑定数据导入成功！");
				resultObj = JSONObject.fromObject(map);
				
				return SUCCESS;
				
			} else {
				map.put("success", false);
				map.put("message", "汪峰演唱会会绑定数据导入未成功！");
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
	/*
	public String uploadchengdu()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println("StatisticAction.uploadchengdu()");
			System.out.println("staff_id:"+staff_idd);
			if (statisticService.UploadChengdu(staff_idd)) {
				map.put("success", true);
				map.put("message", "成都数据导入成功！");
				resultObj = JSONObject.fromObject(map);
				
			} else {
				map.put("success", false);
				map.put("message", "成都数据导入未成功！");
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
	*/
	

}
