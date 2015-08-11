package com.buptmap.action;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.BeaconService;
import com.buptmap.Service.MessageService;
import com.opensymphony.xwork2.ActionSupport;

import com.buptmap.model.Voronoi;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class MessageAction extends ActionSupport{
	private Map<String,Object> resultObj;
	private String jsonstr;
	private MessageService messageService;
	private BeaconService beaconService;
	private String mac_id;
	private String message_id;
	private String points;
	private String building;
	private String url;
	private String minor;
	
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
	public MessageService getMessageService() {
		return messageService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	
	public BeaconService getBeaconService() {
		return beaconService;
	}
	public void setBeaconService(BeaconService beaconService) {
		this.beaconService = beaconService;
	}
	public String getMac_id() {
		return mac_id;
	}
	public void setMac_id(String mac_id) {
		try {
			this.mac_id = new String(mac_id.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMinor() {
		return minor;
	}
	public void setMinor(String minor) {
		this.minor = minor;
	}
	
	
	
	
	//消息列表
	public String message_list(){

		System.out.println("message");
		JSONArray messageArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			messageArray = messageService.message_list();
			
			if (messageArray != null && messageArray.size() != 0) {
				map.put("success", true);
				map.put("total", messageArray.size());
				map.put("message", messageArray);
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
			if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}

	//添加beacon与message的对应关系（多对多）
	public String add_message(){

		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(mac_id);
		System.out.println(message_id);
		
		try {
			String[] id = message_id.split("\\*");
			for (int i = 0; i < id.length; i++) {
				System.out.println(id[i]);
				messageService.add_message(mac_id, id[i]);
			}
				map.put("success", true);			
				map.put("message", "OK");
				resultObj = JSONObject.fromObject(map);
			
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
			//if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}

	
	//给定区域，选择
	public String area_message(){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(points);
		System.out.println(building);
		JSONArray result = new JSONArray();
		
		System.out.println(message_id);
		
		try {
			if (points == null || points.equals("") || building.equals("") || building == null || message_id == null || message_id.equals("")) {
				map.put("success", false);
				map.put("message", "数据不全");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			String[] id = message_id.split("\\*");
			JSONArray testArray = JSONArray.fromObject(points);
			JSONObject point = testArray.getJSONObject(0);
			String floor_id = point.get("floorIndex").toString();//point.get("x")&&point.get("y")
			List<Point2D.Double> polygon = new ArrayList<Point2D.Double>();
			for (int i = 0; i < testArray.size() - 1; i++) {
				JSONObject temppoint = testArray.getJSONObject(i);
				Point2D.Double temp = new Point2D.Double();
				temp.setLocation(Double.valueOf(temppoint.get("x").toString()), Double.valueOf(temppoint.get("y").toString()));
				polygon.add(temp);
				
			}
			result = messageService.area_message(id, polygon, building, floor_id);

			
			if (result != null && result.size() > 0) {
				JSONObject tempObject = result.getJSONObject(result.size() - 1);
				result.remove(result.size()-1);
				System.out.println("cehnggong");
				map.put("success", true);
				map.put("plogn", tempObject);
				map.put("points", result);
				map.put("total", result.size());
				resultObj = JSONObject.fromObject(map);
			}
			else{
				map.put("success", false);
				map.put("message", "目标区域无法被完全覆盖");
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
			//if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}
	
	
	public String voronoi(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONArray result = new JSONArray();
		JSONArray area = new JSONArray();
		JSONArray voronois = new JSONArray();
		Polygon areaPolygon = new Polygon();
		ArrayList<Voronoi> voronoiArrayList = new ArrayList<Voronoi>();
		
		
		try {
			System.out.println(jsonstr);
			result = JSONArray.fromObject(jsonstr);
			area = JSONArray.fromObject(result.getJSONObject(0).get("area"));
			voronois = JSONArray.fromObject(result.getJSONObject(0).get("voronoi"));
			System.out.print(voronois);
			for (int i = 0; i < area.size()-1; i++) {
				String coord_x = JSONObject.fromObject(area.get(i)).getString("x");
				String coord_y = JSONObject.fromObject(area.get(i)).getString("y");
				int m;
				for (m = 0; m < coord_x.length(); m++) {
					if (coord_x.charAt(m) == '.') {
						break;
					}
				}
				coord_x = coord_x.substring(0, m);
				for (m = 0; m < coord_y.length(); m++) {
					if (coord_y.charAt(m) == '.') {
						break;
					}
				}
				coord_y = coord_y.substring(0, m);
				int x = Integer.parseInt(coord_x);
				int y = Integer.parseInt(coord_y);
				areaPolygon.addPoint(x, y);
			}
			
			for (int i = 0; i < voronois.size(); i++) {
				Voronoi tempVoronoi = new Voronoi();
				tempVoronoi.setMac_id(JSONObject.fromObject(voronois.get(i)).getString("mac_id"));
				String plogns = JSONObject.fromObject(voronois.get(i)).getString("plogn");
				String[] points = plogns.split(";");
				for (int j = 0; j < points.length; j++) {
					String[] coord = points[j].split(",");
					int m;
					for (m = 0; m < coord[0].length(); m++) {
						if (coord[0].charAt(m) == '.') {
							break;
						}
					}
					coord[0] = coord[0].substring(0, m);
					for (m = 0; m < coord[1].length(); m++) {
						if (coord[1].charAt(m) == '.') {
							break;
						}
					}
					coord[1] = coord[1].substring(0, m);
					//int coord_x = tempVoronoi.voronoi.
					for (int k = 0; k < coord.length; k++) {
						
					}
					tempVoronoi.voronoi.addPoint(Integer.parseInt(coord[0]),Integer.parseInt(coord[1]));
					
				}
				voronoiArrayList.add(tempVoronoi);
				int x[] = tempVoronoi.voronoi.xpoints;
				int y[] = tempVoronoi.voronoi.ypoints;
				for (int j = 0; j < y.length; j++) {
					System.out.println("第"+i+"个voroi"+x+","+y);
				}
			}
			
			map.put("success", true);
			map.put("message", "11111");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
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
			//if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}
	
	
	
	public String del_record(){

		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(mac_id);
		System.out.println(message_id);
		
		try {
			if (messageService.delete_message(mac_id, message_id)) {
				map.put("success", true);			
				map.put("message", "OK");
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);			
				map.put("message", "OK");
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
			//if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}

	//查找beacon对应的message
	public String beacon_message(){

		System.out.println("message");
		JSONArray messageArray = new JSONArray();
		JSONArray beaconArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			messageArray = messageService.beacon_message(mac_id);
			beaconArray = beaconService.findOne(mac_id);
			if (messageArray != null && messageArray.size() != 0) {
				map.put("success", true);
				map.put("total", messageArray.size());
				map.put("beacon", beaconArray);
				map.put("message", messageArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", true);
				map.put("total", 0);
				map.put("beacon", beaconArray);
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
			if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}

	//excel导入
	public String insert_message(){

		System.out.println("message");
		JSONArray messageArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			messageService.insert_message();
			
            //取数字的时候强转一下,否则默认只取出小数点后3位  
           
			map.put("success", true);
				
			resultObj = JSONObject.fromObject(map);
			
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
			if(messageArray != null) { messageArray.clear(); messageArray = null; }
		}
	
	}

	public String minor_mes(){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(jsonstr);
		
		try {
			JSONArray testArray = JSONArray.fromObject(jsonstr);
			System.out.print(testArray);
			if (messageService.minor_mes(testArray)) {
				map.put("success", true);
				map.put("message", "t");
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
		return SUCCESS;
	
	}

	

}
