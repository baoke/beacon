package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Beacon;
import com.buptmap.model.Map;
import com.buptmap.model.Patrol;


@Component("patrolDao")
public class PatrolDao {
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
	
	//巡检记录实时更新接口
	public boolean update(JSONArray testArray, String user_id)
	{
		try {
			for (int i = 0; i < testArray.size(); i++) {
				JSONObject testObject = testArray.getJSONObject(i);
				Patrol tempPatrol = new Patrol();
				//testObject.g
				tempPatrol.setMac(testObject.getString("mac") == null ? "" : testObject.getString("mac"));
				tempPatrol.setMajor(testObject.getString("major") == null ? "" : testObject.getString("major"));
				tempPatrol.setMinor(testObject.getString("minor") == null ? "" : testObject.getString("minor"));
				tempPatrol.setRssi(testObject.getString("rssi") == null ? "" : testObject.getString("rssi"));
				tempPatrol.setSerial_id(testObject.getString("id") == null ? "" : testObject.getString("id"));
				tempPatrol.setTime(testObject.getString("time") == null ? "" : testObject.getString("time"));
				tempPatrol.setUuid(testObject.getString("uuid") == null ? "" : testObject.getString("uuid"));
				tempPatrol.setBuilding(testObject.getString("building") == null ? "" : testObject.getString("building"));
				tempPatrol.setFloor(testObject.getString("floor") == null ? "" : testObject.getString("floor"));
				tempPatrol.setCoord_x(testObject.getString("coord_x") == null ? "" : testObject.getString("coord_x"));
				tempPatrol.setCoord_y(testObject.getString("coord_y") == null ? "" : testObject.getString("coord_y"));
				tempPatrol.setLatitude(testObject.getString("latitude") == null ? "" : testObject.getString("latitude"));
				tempPatrol.setType(testObject.getString("type") == null ? "" : testObject.getString("type"));
				tempPatrol.setLongitude(testObject.getString("longitude") == null ? "" : testObject.getString("longitude"));
				tempPatrol.setAddress(testObject.getString("address") == null ? "" : testObject.getString("address"));
				tempPatrol.setStatus(testObject.getString("status") == null ?  "0" : testObject.getString("status") );
				tempPatrol.setStaff_id(user_id);
				if (testObject.getString("mac") != null) {
					List<Beacon> beaconList = hibernateTemplate.find("from Beacon b where b.mac_id = '" + testObject.getString("mac") + "'");
					if (beaconList.size() == 1) {
						Beacon tempBeacon = beaconList.get(0);
						//tempBeacon.setStatus(testObject.getString("status"));
						//hibernateTemplate.save(tempBeacon);
						tempPatrol.setCompany_id(tempBeacon.getCompany_id());
					}
				
				}
				
				hibernateTemplate.save(tempPatrol);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	//巡检记录db上载
	public boolean upload(JSONArray testArray){
		try {
			for (int i = 0; i < testArray.size(); i++) {
				JSONObject testObject = testArray.getJSONObject(i);
				Patrol tempPatrol = new Patrol();
				//testObject.g
				tempPatrol.setMac(testObject.getString("mac"));
				tempPatrol.setMajor(testObject.getString("major"));
				tempPatrol.setMinor(testObject.getString("minor"));
				tempPatrol.setRssi(testObject.getString("rssi"));
				tempPatrol.setSerial_id(testObject.getString("serial_id"));
				tempPatrol.setStaff_id(testObject.getString("staff_id") );
				tempPatrol.setTime(testObject.getString("time"));
				tempPatrol.setUuid(testObject.getString("uuid"));
				hibernateTemplate.save(tempPatrol);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	//寻找某一座建筑内的巡检人员上传的beacon巡检记录
	public JSONArray findbybuilding(String building_id){
		//String type = "1";
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();	
		List<Patrol> result = new ArrayList<Patrol>();
		List<Object[]> result1 = new ArrayList<Object[]>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		Object[] findpatrol = null;
		result1 = hibernateTemplate.find("select distinct mac,uuid from Patrol p where p.building = '" + building_id + "'order by time desc");
		if (result1 != null && result1.size() != 0) {			
			for(int i= 0; i < result1.size(); i ++){
				findpatrol = result1.get(i);
				result = hibernateTemplate.find("from Patrol p where p.mac = '" + findpatrol[0] + "'order by time desc");
				
				Patrol tempPatrol = result.get(0);
				jsonObject.put("count", result.size());
				if (tempPatrol.getBuilding().equals("")){
					jsonObject.put("building", tempPatrol.getBuilding());
				}
				else {
					result2 = hibernateTemplate.find("select name, city_name, unit_id from Place s where s.unit_id = '" + tempPatrol.getBuilding() + "'");
					if (result2.size() == 1) {
						Object[] spot = result2.get(0);
						jsonObject.put("building", spot[0]);
					}
					else {
						jsonObject.put("building","");
					}
					
				}
				jsonObject.put("company_id", tempPatrol.getCompany_id());
				jsonObject.put("coord_x", tempPatrol.getCoord_x());
				jsonObject.put("coord_y", tempPatrol.getCoord_y());
				jsonObject.put("floor", tempPatrol.getFloor());
				jsonObject.put("id", tempPatrol.getId());
				jsonObject.put("latitude", tempPatrol.getLatitude());
				jsonObject.put("longitude", tempPatrol.getLongitude());
				jsonObject.put("major", tempPatrol.getMajor());
				jsonObject.put("mac", tempPatrol.getMac());
				jsonObject.put("minor", tempPatrol.getMinor());
				jsonObject.put("rssi", tempPatrol.getRssi());
				jsonObject.put("serial_id", tempPatrol.getSerial_id());
				jsonObject.put("staff_id", tempPatrol.getStaff_id());
				jsonObject.put("time", tempPatrol.getTime());
				jsonObject.put("uuid", tempPatrol.getUuid());
				jsonObject.put("address", tempPatrol.getAddress());
				if (tempPatrol.getStatus().equals("2")) {
					jsonObject.put("status", "正常");
				}
				else if (tempPatrol.getStatus().equals("3")) {
					jsonObject.put("status", "位置变动");
				}
				else {
					jsonObject.put("status", "未检到");
				}
				
				
				jsonArray.add(jsonObject);
			}
		
		}
		return jsonArray;
	}
	
	//寻找某一座建筑内的巡检人员上传的beacon巡检记录
	public JSONArray findbymac(String mac_id){
		String type = "1";
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();	
		List<Patrol> result = new ArrayList<Patrol>();
		
		result = hibernateTemplate.find("from Patrol p where p.mac = '" + mac_id + "' and p.type='" + type + "' order by time desc");
		if (result != null && result.size() != 0) {
			
			
			for(int i= 0; i < result.size(); i ++){
				Patrol tempPatrol = result.get(0);
				jsonObject.put("building", tempPatrol.getBuilding());
				jsonObject.put("company_id", tempPatrol.getCompany_id());
				jsonObject.put("coord_x", tempPatrol.getCoord_x());
				jsonObject.put("coord_y", tempPatrol.getCoord_y());
				jsonObject.put("floor", tempPatrol.getFloor());
				jsonObject.put("id", tempPatrol.getId());
				jsonObject.put("latitude", tempPatrol.getLatitude());
				jsonObject.put("longitude", tempPatrol.getLongitude());
				jsonObject.put("major", tempPatrol.getMajor());
				jsonObject.put("mac", tempPatrol.getMac());
				jsonObject.put("minor", tempPatrol.getMinor());
				jsonObject.put("rssi", tempPatrol.getRssi());
				jsonObject.put("serial_id", tempPatrol.getSerial_id());
				jsonObject.put("staff_id", tempPatrol.getStaff_id());
				jsonObject.put("time", tempPatrol.getTime());
				jsonObject.put("uuid", tempPatrol.getUuid());
				
				jsonArray.add(jsonObject);
			}
		
		}
		return jsonArray;
	}
	
	//寻找公司的beancon记录
	public JSONArray findall(HashMap conditions){
		//String type = "1";
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();	
		List<Patrol> result = new ArrayList<Patrol>();
		List<Beacon> beaconList = new ArrayList<Beacon>();
		List<Object[]> result1 = new ArrayList<Object[]>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		Object[] findpatrol = null;
		String sql = "select distinct mac,uuid from Patrol p where";
		Set set = conditions.keySet();
		int flag = 0;
		for(Iterator iter = set.iterator(); iter.hasNext();)
		{
		   flag ++;
		   
		   String key = (String)iter.next();
		   String value = (String)conditions.get(key);
		   
		   
		  if(flag != set.size()){
			  if(key=="starttime"){
				   sql = sql +" time>='" + value + "' and";
			   }
			   else if(key=="endtime"){
				   sql = sql +" time <='" + value + "'and";
			   }
			  
			   else sql = sql +" " + key + "='" + value + "'and";
		   }
		   else{
			   if(key=="starttime"){
				   sql = sql +" time >='" + value + "'";
			   }
			   else if(key=="endtime"){
				   sql = sql +" time<='" + value + "'";
			   }
			   else sql = sql +" " + key + "='" + value + "'";
		   }
		  
		  }
		
		System.out.println(sql);
		result1 = hibernateTemplate.find(sql);
		if (result1 != null && result1.size() != 0) {			
			for(int i= 0; i < result1.size(); i ++){
				findpatrol = result1.get(i);
				result = hibernateTemplate.find("from Patrol p where p.mac = '" + findpatrol[0] + "'order by time desc");
				beaconList = hibernateTemplate.find("from Beacon b where b.mac_id = '" + findpatrol[0] + "'");
				Patrol tempPatrol = result.get(0);
				jsonObject.put("count", result.size());
				if (tempPatrol.getBuilding().equals("")){
					jsonObject.put("building", tempPatrol.getBuilding());
				}
				else {
					result2 = hibernateTemplate.find("select name, city_name, unit_id from Place s where s.unit_id = '" + tempPatrol.getBuilding() + "'");
					if (result2.size() == 1) {
						Object[] spot = result2.get(0);
						jsonObject.put("building", spot[0]);
					}
					else {
						jsonObject.put("building","");
					}
					
				}
				if(beaconList != null && beaconList.size() != 0){
					Beacon tempBeacon = beaconList.get(0);
					jsonObject.put("coord_x", tempBeacon.getCoord_x());
					jsonObject.put("coord_y", tempBeacon.getCoord_y());
					jsonObject.put("floor", tempBeacon.getFloor());
				}
				else{
					jsonObject.put("coord_x", tempPatrol.getCoord_x());
					jsonObject.put("coord_y", tempPatrol.getCoord_y());
					jsonObject.put("floor", tempPatrol.getFloor());
				}
				jsonObject.put("company_id", tempPatrol.getCompany_id());
				
				jsonObject.put("id", tempPatrol.getId());
				jsonObject.put("latitude", tempPatrol.getLatitude());
				jsonObject.put("longitude", tempPatrol.getLongitude());
				jsonObject.put("major", tempPatrol.getMajor());
				jsonObject.put("mac", tempPatrol.getMac());
				jsonObject.put("minor", tempPatrol.getMinor());
				jsonObject.put("rssi", tempPatrol.getRssi());
				jsonObject.put("serial_id", tempPatrol.getSerial_id());
				jsonObject.put("staff_id", tempPatrol.getStaff_id());
				jsonObject.put("time", tempPatrol.getTime());
				jsonObject.put("uuid", tempPatrol.getUuid());
				jsonObject.put("address", tempPatrol.getAddress());
				if (tempPatrol.getStatus().equals("2")) {
					jsonObject.put("status", "正常");
				}
				else if (tempPatrol.getStatus().equals("3")) {
					jsonObject.put("status", "位置变动");
				}
				else {
					jsonObject.put("status", "未检到");
				}
				
				
				jsonArray.add(jsonObject);
			}
		
		}
		return jsonArray;
	}
	
	public JSONArray findone(String mac_id){
		//String type = "1";
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();	
		List<Patrol> result = new ArrayList<Patrol>();
		List<Beacon> beaconList = new ArrayList<Beacon>();
		List<Object[]> result1 = new ArrayList<Object[]>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		Object[] findpatrol = null;
		result1 = hibernateTemplate.find("select distinct mac,uuid from Patrol p where p.mac = '" + mac_id + "'order by time desc");
		if (result1 != null && result1.size() != 0) {			
			for(int i= 0; i < result1.size(); i ++){
				findpatrol = result1.get(i);
				result = hibernateTemplate.find("from Patrol p where p.mac = '" + findpatrol[0] + "'order by time desc");
				
				Patrol tempPatrol = result.get(0);
				jsonObject.put("count", result.size());
				if (tempPatrol.getBuilding().equals("")){
					jsonObject.put("building", tempPatrol.getBuilding());
				}
				else {
					result2 = hibernateTemplate.find("select name, city_name, unit_id from Place s where s.unit_id = '" + tempPatrol.getBuilding() + "'");
					if (result2.size() == 1) {
						Object[] spot = result2.get(0);
						jsonObject.put("building", spot[0]);
					}
					else {
						jsonObject.put("building","");
					}
					
				}
				beaconList = hibernateTemplate.find("from Beacon b where b.mac_id = '" + findpatrol[0] + "'");
				if(beaconList != null && beaconList.size() != 0){
					Beacon tempBeacon = beaconList.get(0);
					jsonObject.put("coord_x", tempBeacon.getCoord_x());
					jsonObject.put("coord_y", tempBeacon.getCoord_y());
					jsonObject.put("floor", tempBeacon.getFloor());
				}
				else{
					jsonObject.put("coord_x", tempPatrol.getCoord_x());
					jsonObject.put("coord_y", tempPatrol.getCoord_y());
					jsonObject.put("floor", tempPatrol.getFloor());
				}
				jsonObject.put("company_id", tempPatrol.getCompany_id());
				jsonObject.put("id", tempPatrol.getId());
				jsonObject.put("latitude", tempPatrol.getLatitude());
				jsonObject.put("longitude", tempPatrol.getLongitude());
				jsonObject.put("major", tempPatrol.getMajor());
				jsonObject.put("mac", tempPatrol.getMac());
				jsonObject.put("minor", tempPatrol.getMinor());
				jsonObject.put("rssi", tempPatrol.getRssi());
				jsonObject.put("serial_id", tempPatrol.getSerial_id());
				jsonObject.put("staff_id", tempPatrol.getStaff_id());
				jsonObject.put("time", tempPatrol.getTime());
				jsonObject.put("uuid", tempPatrol.getUuid());
				jsonObject.put("address", tempPatrol.getAddress());
				if (tempPatrol.getStatus().equals("2")) {
					jsonObject.put("status", "正常");
				}
				else if (tempPatrol.getStatus().equals("3")) {
					jsonObject.put("status", "位置变动");
				}
				else {
					jsonObject.put("status", "未检到");
				}
				
				
				jsonArray.add(jsonObject);
			}
		
		}
		return jsonArray;
	}
	
	public JSONArray getbuildingid(String mac_id){
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();	
		List<Beacon> result1 ;
		List<Map> result2 ;
		
		result1 = hibernateTemplate.find("from Beacon b where b.mac_id = '" + mac_id + "'");
		if (result1.size() == 1) {
			Beacon tempBeacon = result1.get(0);
			jsonObject.put("buildingId", tempBeacon.getBuilding());
		
		   if(tempBeacon.getBuilding()!=""){
		    result2 = hibernateTemplate.find("from Map m where m.unit_id = '" + tempBeacon.getBuilding() + "' and m.floor_id='" + tempBeacon.getFloor() + "'");
		    
		     Map tempMap = result2.get(0);
		     jsonObject.put("scaleId", tempMap.getScale());
		     
		     
		   }
		   jsonArray.add(jsonObject);
		}
		

		return jsonArray;
	}
	
  

public JSONArray weijiance(){
	//String type = "1";
	jsonArray = new JSONArray();
	jsonObject = new JSONObject();	
	List<Patrol> result = new ArrayList<Patrol>();
	List<Object[]> result1 = new ArrayList<Object[]>();
	List<Object[]> result2 = new ArrayList<Object[]>();
	Object[] findpatrol = null;
	result1 = hibernateTemplate.find("select distinct mac,uuid from Patrol p where p.status = '3' or p.status='1' order by time desc");
	if (result1 != null && result1.size() != 0) {			
		for(int i= 0; i < result1.size(); i ++){
			findpatrol = result1.get(i);
			result = hibernateTemplate.find("from Patrol p where p.mac = '" + findpatrol[0] + "'order by time desc");
			
			Patrol tempPatrol = result.get(0);
			jsonObject.put("count", result.size());
			if (tempPatrol.getBuilding().equals("")){
				jsonObject.put("building", tempPatrol.getBuilding());
			}
			else {
				result2 = hibernateTemplate.find("select name, city_name, unit_id from Place s where s.unit_id = '" + tempPatrol.getBuilding() + "'");
				if (result2.size() == 1) {
					Object[] spot = result2.get(0);
					jsonObject.put("building", spot[0]);
				}
				else {
					jsonObject.put("building","");
				}
				
			}
			jsonObject.put("company_id", tempPatrol.getCompany_id());
			jsonObject.put("coord_x", tempPatrol.getCoord_x());
			jsonObject.put("coord_y", tempPatrol.getCoord_y());
			jsonObject.put("floor", tempPatrol.getFloor());
			jsonObject.put("id", tempPatrol.getId());
			jsonObject.put("latitude", tempPatrol.getLatitude());
			jsonObject.put("longitude", tempPatrol.getLongitude());
			jsonObject.put("major", tempPatrol.getMajor());
			jsonObject.put("mac", tempPatrol.getMac());
			jsonObject.put("minor", tempPatrol.getMinor());
			jsonObject.put("rssi", tempPatrol.getRssi());
			jsonObject.put("serial_id", tempPatrol.getSerial_id());
			jsonObject.put("staff_id", tempPatrol.getStaff_id());
			jsonObject.put("time", tempPatrol.getTime());
			jsonObject.put("uuid", tempPatrol.getUuid());
			jsonObject.put("address", tempPatrol.getAddress());
			if (tempPatrol.getStatus().equals("2")) {
				jsonObject.put("status", "正常");
			}
			else if (tempPatrol.getStatus().equals("3")) {
				jsonObject.put("status", "位置变动");
			}
			else {
				jsonObject.put("status", "未检到");
			}
			
			
			jsonArray.add(jsonObject);
		}
	
	}
	return jsonArray;
}

public JSONArray floorbeacon(String building_id, String floor_id){
	jsonArray = new JSONArray();
	jsonObject = new JSONObject();
	List<Beacon> result = new ArrayList<Beacon>();
	List<Map> tempresult = new ArrayList<Map>();
	result = hibernateTemplate.find("from Beacon b where b.building = '" + building_id + "' and b.floor='" + floor_id + "'");
	if (result != null && result.size() != 0) {
		for (int i = 0; i < result.size(); i++) {
			Beacon findbBeacon = result.get(i);
			jsonObject.put("mac_id", findbBeacon.getMac_id());
			jsonObject.put("uuid", findbBeacon.getUuid());
			jsonObject.put("major", findbBeacon.getMajor());
			jsonObject.put("minor", findbBeacon.getMinor());
			jsonObject.put("building", findbBeacon.getBuilding());
			jsonObject.put("floor", findbBeacon.getFloor());
			
			jsonObject.put("power", findbBeacon.getPower());
			jsonObject.put("firm", findbBeacon.getFirm());
			jsonObject.put("type", findbBeacon.getType());
			
			jsonObject.put("frequency", findbBeacon.getFrequency());
			jsonObject.put("coord_x", findbBeacon.getCoord_x());
			jsonObject.put("coord_y", findbBeacon.getCoord_y());
			jsonObject.put("frame", findbBeacon.getFrame());
			jsonObject.put("address", findbBeacon.getAddress());
			jsonObject.put("status", findbBeacon.getStatus());
			jsonObject.put("create_time", findbBeacon.getCreate_time());
			jsonObject.put("last_modify_time", findbBeacon.getLast_modify_time());
			
			
			tempresult =  hibernateTemplate.find("from Map m where m.unit_id = '" + findbBeacon.getBuilding() + "' and m.floor_id='" + findbBeacon.getFloor() + "' and m.map_style_id=4");
			if (tempresult != null && tempresult.size() != 0) {
				Map tempMap = tempresult.get(0);
				jsonObject.put("scale", tempMap.getScale());
			}
			jsonArray.add(jsonObject);
		}
		
	}

	return jsonArray;
}

}
