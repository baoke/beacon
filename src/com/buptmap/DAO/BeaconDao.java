package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import test.test;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.opensymphony.xwork2.interceptor.annotations.After;

import com.buptmap.model.Beacon;
import com.buptmap.model.Patrol;
import com.buptmap.model.Testbeacon;
import com.buptmap.model.Spot;
import com.buptmap.model.Staff;
import com.buptmap.model.Company;
import com.buptmap.model.Map;

@Component("beaconDAO")
public class BeaconDao {
	private HibernateTemplate hibernateTemplate = null;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	public final static HashMap coverageMap = new HashMap() {{    
	    put("Mi_05", 5);    
	    put("Mi_08", 8);    
	    put("Mi_10", 10);  
	    put("Mi_15", 15);  
	    put("Mi_22", 22);  
	    put("Mi_30", 30);  
	    put("Mi_30BIG", 31);  
	    put("Mi_00UNKNOW", 3); 
	}};
	//1已配置未部署，2已部署, 3测试，4回收，5预部署
	public final static HashMap statusMap = new HashMap() {{    
	    put("1", "已配置");    
	    put("2", "已部署");    
	    put("3", "测试");  
	    put("4", "回收");  
	    put("5", "预部署");  
	  
	}};
	
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
	
	//db数据下载
	public boolean download(String unitid,String company_id) {
		try {
			
			Class.forName("org.sqlite.JDBC");
	        Connection connTest = DriverManager.getConnection("jdbc:sqlite://d:/apache-tomcat-7.0.56-windows-x64/apache-tomcat-7.0.56/webapps/testMap/NEWBEACONDEPOY902.db");
	        //"jdbc:sqlite://c:/Users/baoke/Desktop/IBeacon/testMap/test.db"
	        //"jdbc:sqlite://d:/apache-tomcat-7.0.56-windows-x64/apache-tomcat-7.0.56/webapps/testMap/NEWBEACONDEPOY902.db"
	        Statement stat = connTest.createStatement();
	        stat.executeUpdate("delete from beacon");
	        PreparedStatement prep = connTest.prepareStatement("insert into beacon values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);");
			
	        //stat.executeUpdate("create table beacon_table(id varchar(20) primary key, uuid varchar(20),);");
	       // stat.executeUpdate("create table beacon_table (Name, ItemCount);");
			String[] id = unitid.split("\\*");
			for (int i = 0; i < id.length; i++) {
				List<Beacon> result = new ArrayList<Beacon>();
				result = hibernateTemplate.find("from Beacon where building = '" + id[i] + "'");//hibernateTemplate.find("from Beacon b where b.building = '" + id[i] + "' and b.company_id = '" + company_id + "'");
				if (result.size() > 0) {
					for (int j = 0; j < result.size(); j++) {
						Beacon tempbBeacon = result.get(j);
						
						prep.setString(1, tempbBeacon.getMac_id());
						prep.setString(2, tempbBeacon.getUuid());
						prep.setString(3, tempbBeacon.getMajor());
						prep.setString(4, tempbBeacon.getMinor());
						prep.setString(5, tempbBeacon.getBuilding());
						prep.setString(6, tempbBeacon.getFloor());
						prep.setString(7, tempbBeacon.getCoord_x());
						prep.setString(8, tempbBeacon.getCoord_y());
						prep.setString(9, tempbBeacon.getCoverage());
						prep.setString(10, tempbBeacon.getPower());
						prep.setString(11, tempbBeacon.getFrequency());
						prep.setString(12, tempbBeacon.getTemperaturefrequency());
						prep.setString(13, tempbBeacon.getLightfrequency());
						prep.setString(14, tempbBeacon.getType());
						prep.setString(15, tempbBeacon.getFirm());
						prep.setString(16, tempbBeacon.getCreate_id());
						prep.setString(17, tempbBeacon.getCompany_id());
						prep.setString(18, tempbBeacon.getAccelerate());
						prep.setString(19, tempbBeacon.getCreate_time());
						prep.setString(20, tempbBeacon.getLast_modify_id());
						prep.setString(21, tempbBeacon.getLast_modify_time());
						prep.setString(22, tempbBeacon.getAddress());
						prep.setString(23, tempbBeacon.getAddress_type());
						prep.setString(24, tempbBeacon.getStatus());
						
						
						
						//	prep.setString(16, tempbBeacon.getUpdate_time());
				        prep.addBatch();
					}
				}
				
			}
			
			connTest.setAutoCommit(false);
	        prep.executeBatch();
	        connTest.setAutoCommit(true);
	        connTest.close();
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
			// TODO: handle exception
		}finally{
			
		}
		
	}
	
	//根据mac_id查找beacon
	public JSONArray findOne(String mac_id){
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		List<Map> tempresult = new ArrayList<Map>();
		List<Staff> tempStaffs = new ArrayList<Staff>(); 
		List<Beacon> result = new ArrayList<Beacon>();
		result = hibernateTemplate.find("from Beacon b where b.mac_id = '" + mac_id + "'");
		if (result.size() == 1) {
			Beacon findbBeacon = result.get(0);
			jsonObject.put("mac_id", findbBeacon.getMac_id());
			jsonObject.put("uuid", findbBeacon.getUuid());
			jsonObject.put("major", findbBeacon.getMajor());
			jsonObject.put("minor", findbBeacon.getMinor());
			jsonObject.put("building", findbBeacon.getBuilding());
			jsonObject.put("floor", findbBeacon.getFloor());
			jsonObject.put("coverage", coverageMap.get(findbBeacon.getCoverage()));
			jsonObject.put("power", findbBeacon.getPower());
			jsonObject.put("firm", findbBeacon.getFirm());
			jsonObject.put("type", findbBeacon.getType());
			//jsonObject.put("uuid", findbBeacon.getUuid());
			jsonObject.put("frequency", findbBeacon.getFrequency());
			jsonObject.put("coord_x", findbBeacon.getCoord_x());
			jsonObject.put("coord_y", findbBeacon.getCoord_y());
			jsonObject.put("address_type", findbBeacon.getAddress_type());
			jsonObject.put("frame", findbBeacon.getFrame());
			jsonObject.put("address", findbBeacon.getAddress());
			jsonObject.put("status", findbBeacon.getStatus());
			jsonObject.put("create_time", findbBeacon.getCreate_time());
			jsonObject.put("last_modify_time", findbBeacon.getLast_modify_time());
			tempStaffs = hibernateTemplate.find("from Staff s where s.staff_id= '" + findbBeacon.getCreate_id() + "'");
			if (tempStaffs != null && tempStaffs.size() != 0) {
				Staff tempStaff = tempStaffs.get(0);
				jsonObject.put("create", tempStaff.getStaff_name());
			}
			tempStaffs = hibernateTemplate.find("from Staff s where s.staff_id= '" + findbBeacon.getLast_modify_id() + "'");
			if (tempStaffs != null && tempStaffs.size() != 0) {
				Staff tempStaff = tempStaffs.get(0);
				jsonObject.put("last", tempStaff.getStaff_name());
			}
			tempresult =  hibernateTemplate.find("from Map m where m.unit_id = '" + findbBeacon.getBuilding() + "' and m.floor_id='" + findbBeacon.getFloor() + "' and m.map_style_id=4");
			if (tempresult != null && tempresult.size() != 0) {
				Map tempMap = tempresult.get(0);
				jsonObject.put("scale", tempMap.getScale());
			}
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}
	
	//查找该公司已部署的building列表
	public JSONArray getbuilding(String company_id){
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		List<Object[]> result3 = new ArrayList<Object[]>();
		Object[] findbuilding = null;
		result = hibernateTemplate.find("select distinct building,company_id from Beacon b where b.company_id = '" + company_id + "'");
		if (result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				findbuilding = result.get(i);
				System.out.println(findbuilding[0]);
				result3 = hibernateTemplate.find("select company_id from Beacon b where b.company_id = '" + company_id + "' and b.building='"+ findbuilding[0] +"'");
				
				result2 = hibernateTemplate.find("select name, city_name, unit_id from Place s where s.unit_id = '" + findbuilding[0] + "'");
				if (result2.size() == 1) {
					
					Object[] spot = result2.get(0);
					jsonObject.put("name", spot[0]);
					jsonObject.put("city_name", spot[1]);
					jsonObject.put("unit_id", spot[2]);
					jsonObject.put("size", result3.size());
				
					jsonArray.add(jsonObject);
				}
				
			}
			
		}

		return jsonArray;
	}
	
	//查找所有建筑
	
	//生成六边形的六个顶点
	public void updateCoverage(Beacon tempBeacon){
	
		double x = Double.parseDouble(tempBeacon.getCoord_x());
		double y = Double.parseDouble(tempBeacon.getCoord_y());
		int r = (Integer) coverageMap.get(tempBeacon.getCoverage());
		double high = Math.sqrt(3) / 2;
		String coverage = String.valueOf(x-r) + "," + String.valueOf(y) + ";";
		coverage = coverage + String.valueOf(x-r/2) + "," + String.valueOf(y+high*r) +";";
		coverage = coverage + String.valueOf(x+r/2) + "," + String.valueOf(y+high*r) +";";
		coverage = coverage + String.valueOf(x+r) + "," + String.valueOf(y) +";";
		coverage = coverage + String.valueOf(x+r/2) + "," + String.valueOf(y-high*r) +";";
		coverage = coverage + String.valueOf(x-r/2) + "," + String.valueOf(y-high*r) +";";
		tempBeacon.setFrame(coverage);
		hibernateTemplate.update(tempBeacon);	
	}
	
	//上传db，beacon部署的情况
	public boolean update(List<Testbeacon> testList){
		
		List<Beacon> result = new ArrayList<Beacon>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		try {
			for (int i = 0; i < testList.size(); i++) {
				Testbeacon testbeacon = testList.get(i);
				System.out.println(testbeacon.getMac_id());
				result = hibernateTemplate.find("from Beacon where mac_id = '" + testbeacon.getMac_id() + "'");		
				//result2 = hibernateTemplate.find("select from Beacon where mac_id = '" + testbeacon.getMac_id() + "'");
				
				if (result.size() == 0) {
					Beacon tempBeacon = new Beacon();
					if (!testbeacon.getUuid().equalsIgnoreCase("***")) {
						tempBeacon.setAccelerate(testbeacon.getAccelerate() == null ? "" : testbeacon.getAccelerate());
						tempBeacon.setBuilding(testbeacon.getBuilding() == null ? "" : testbeacon.getBuilding());
						tempBeacon.setCoverage(testbeacon.getCoverage() == null ? "" : testbeacon.getCoverage());
						tempBeacon.setCoord_x(testbeacon.getCoord_x() == null ? "" : testbeacon.getCoord_x());
						tempBeacon.setCoord_y(testbeacon.getCoord_y() == null ? "" : testbeacon.getCoord_y());
						tempBeacon.setFirm(testbeacon.getFirm() == null ? "" : testbeacon.getFirm());
						tempBeacon.setFloor(testbeacon.getFloor() == null ? "" : testbeacon.getFloor());
						tempBeacon.setFrequency(testbeacon.getFrequency() == null ? "" : testbeacon.getFrequency());
						tempBeacon.setLightfrequency(testbeacon.getLightfrequency() == null ? "" : testbeacon.getLightfrequency());
						tempBeacon.setMac_id(testbeacon.getMac_id() == null ? "" : testbeacon.getMac_id());
						tempBeacon.setMajor(testbeacon.getMajor() == null ? "" : testbeacon.getMajor());
						tempBeacon.setMinor(testbeacon.getMinor() == null ? "" : testbeacon.getMinor());
						tempBeacon.setPower(testbeacon.getPower() == null ? "" : testbeacon.getPower());
						tempBeacon.setTemperaturefrequency(testbeacon.getTemperaturefrequency() == null ? "" : testbeacon.getTemperaturefrequency());
						tempBeacon.setUuid(testbeacon.getUuid() == null ? "" : testbeacon.getUuid());
						tempBeacon.setType(testbeacon.getType() == null ? "" : testbeacon.getType());
						tempBeacon.setAddress(testbeacon.getAddress() == null ? "" : testbeacon.getAddress());
						tempBeacon.setCreate_id(testbeacon.getLast_modify_id() == null ? "" : testbeacon.getLast_modify_id());
						tempBeacon.setCreate_time(testbeacon.getLast_modify_time() == null ? "" : testbeacon.getLast_modify_time());
						tempBeacon.setLast_modify_id(testbeacon.getLast_modify_id() == null ? "" : testbeacon.getLast_modify_id());;
						tempBeacon.setLast_modify_time(testbeacon.getLast_modify_time() == null ? "" : testbeacon.getLast_modify_time());
						tempBeacon.setStatus(testbeacon.getStatus() == null ? "" : statusMap.get(testbeacon.getStatus()).toString());
						tempBeacon.setAddress_type(testbeacon.getAddress_type() == null ? "" : testbeacon.getAddress_type());
						tempBeacon.setExperience(testbeacon.getMajor()+"_"+testbeacon.getMinor()+"_"+testbeacon.getStatus()+"_"+testbeacon.getLast_modify_time().substring(0,10)+";");
						
						//company_id
						if (testbeacon.getLast_modify_id() != null) {
							List<Staff> result3 = hibernateTemplate.find("from Staff where staff_id = '" + testbeacon.getLast_modify_id() + "'");
							if (result3.size() == 1) {
								tempBeacon.setCompany_id(result3.get(0).getCompany_id());
							}
						}
						//处理frame
						
						hibernateTemplate.save(tempBeacon);
						updateCoverage(tempBeacon);
					}
					
				}
				else {
					Beacon tempBeacon = result.get(0);
					
					if (testbeacon.getUuid().equalsIgnoreCase("***")){
						hibernateTemplate.delete(tempBeacon);
					}
					else {
						tempBeacon.setAccelerate(testbeacon.getAccelerate() == null ? "" : testbeacon.getAccelerate());
						tempBeacon.setBuilding(testbeacon.getBuilding() == null ? "" : testbeacon.getBuilding());
						tempBeacon.setCoverage(testbeacon.getCoverage() == null ? "" : testbeacon.getCoverage());
						tempBeacon.setCoord_x(testbeacon.getCoord_x() == null ? "" : testbeacon.getCoord_x());
						tempBeacon.setCoord_y(testbeacon.getCoord_y() == null ? "" : testbeacon.getCoord_y());
						tempBeacon.setFirm(testbeacon.getFirm() == null ? "" : testbeacon.getFirm());
						tempBeacon.setFloor(testbeacon.getFloor() == null ? "" : testbeacon.getFloor());
						tempBeacon.setFrequency(testbeacon.getFrequency() == null ? "" : testbeacon.getFrequency());
						tempBeacon.setLightfrequency(testbeacon.getLightfrequency() == null ? "" : testbeacon.getLightfrequency());
						tempBeacon.setMajor(testbeacon.getMajor() == null ? "" : testbeacon.getMajor());
						tempBeacon.setMinor(testbeacon.getMinor() == null ? "" : testbeacon.getMinor());
						tempBeacon.setPower(testbeacon.getPower() == null ? "" : testbeacon.getPower());
						tempBeacon.setTemperaturefrequency(testbeacon.getTemperaturefrequency() == null ? "" : testbeacon.getTemperaturefrequency());
						tempBeacon.setUuid(testbeacon.getUuid() == null ? "" : testbeacon.getUuid());
						tempBeacon.setType(testbeacon.getType() == null ? "" : testbeacon.getType());
						tempBeacon.setAddress(testbeacon.getAddress() == null ? "" : testbeacon.getAddress());
						tempBeacon.setStatus(testbeacon.getStatus() == null ? "" : statusMap.get(testbeacon.getStatus()).toString());
						tempBeacon.setAddress_type(testbeacon.getAddress_type() == null ? "" : testbeacon.getAddress_type());
						if (!tempBeacon.getLast_modify_time().equals(testbeacon.getLast_modify_time())) {
							tempBeacon.setLast_modify_id(testbeacon.getLast_modify_id());;
							tempBeacon.setLast_modify_time(testbeacon.getLast_modify_time());
							
						}
						String experience = tempBeacon.getExperience()+testbeacon.getMajor()+"_"+testbeacon.getMinor()+"_"+testbeacon.getStatus()+":"+testbeacon.getLast_modify_time().substring(0, 10)+";";
						if (experience.length()> 4000) {
							experience = testbeacon.getMajor()+"_"+testbeacon.getMinor()+"_"+testbeacon.getStatus()+":"+testbeacon.getLast_modify_time().substring(0, 10)+";";
							
						}
						tempBeacon.setExperience(experience);
						
						hibernateTemplate.update(tempBeacon);
						updateCoverage(tempBeacon);
					}
					
				}
				
			}
			System.out.println("update" + testList.size());
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;// TODO: handle exception
		}
	
	}
	
	//根据building_id查找beacon
	public JSONArray findbybuilding(String building_id, String company_id){
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		List<Beacon> result = new ArrayList<Beacon>();
		List<Map> tempresult = new ArrayList<Map>();
		result = hibernateTemplate.find("from Beacon b where b.building = '" + building_id + "'");// and b.company_id='" + company_id + "'");
		if (result != null && result.size() != 0) {
			for (int i = 0; i < result.size(); i++) {
				Beacon findbBeacon = result.get(i);
				jsonObject.put("mac_id", findbBeacon.getMac_id());
				jsonObject.put("uuid", findbBeacon.getUuid());
				jsonObject.put("major", findbBeacon.getMajor());
				jsonObject.put("minor", findbBeacon.getMinor());
				jsonObject.put("building", findbBeacon.getBuilding());
				jsonObject.put("floor", findbBeacon.getFloor());
				jsonObject.put("coverage", coverageMap.get(findbBeacon.getCoverage()));
				jsonObject.put("power", findbBeacon.getPower());
				jsonObject.put("firm", findbBeacon.getFirm());
				jsonObject.put("type", findbBeacon.getType());
				//jsonObject.put("uuid", findbBeacon.getUuid());
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
	
	public JSONArray findSession(String user_id){
		JSONObject sessionsObject = new JSONObject();
		JSONArray sessionsArray = new JSONArray();
		JSONObject majorObject = new JSONObject();
		JSONArray majorArray = new JSONArray();
		JSONObject resultObject = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		List<Staff> staffList = new ArrayList<Staff>();
		Staff tempStaff;
		List<Beacon> beaconList = new ArrayList<Beacon>();
		String company_id;
		Beacon tempBeacon;
		String[] sessions;
		String[] session;
		
		
		staffList = hibernateTemplate.find("from Staff s where s.staff_id = '" + user_id + "'");
		tempStaff = staffList.get(0);
		company_id = tempStaff.getCompany_id();
		beaconList = hibernateTemplate.find("from Beacon b where b.company_id='"+ company_id + "'");
		if (beaconList != null && beaconList.size() != 0) {
			for (int i = 0; i < beaconList.size(); i++) {
				//清空一下
				majorArray.clear();
				sessionsArray.clear();
				tempBeacon = beaconList.get(0);
				if (tempBeacon.getSession() == null || tempBeacon.getSession().equals("")) {
					sessionsObject.put("value0", "0");
					sessionsObject.put("value1", "0");
					sessionsArray.add(sessionsObject);
				}
				else {
					sessions = tempBeacon.getSession().split(";");
					for (int j = 0; j < sessions.length; j++) {
						session = sessions[j].split(",");
						sessionsObject.put("value0", session[0]);
						sessionsObject.put("value1", session[1]);
						sessionsArray.add(sessionsObject);
					}
					
				}
				majorObject.put("value", tempBeacon.getMajor());
				majorObject.put("sections", sessionsArray);
				majorArray.add(majorObject);
				resultObject.put("value", tempBeacon.getUuid());
				resultObject.put("majors", majorArray);
				resultArray.add(resultObject);
			}
		}
		return resultArray;
	}
	
	public String userSession(String user_id){
		String staffsession;
		JSONArray resultArray = new JSONArray();
		List<Staff> staffList = new ArrayList<Staff>();
		Staff tempStaff;
		
		
		staffList = hibernateTemplate.find("from Staff s where s.staff_id = '" + user_id + "'");
		tempStaff = staffList.get(0);
		
		staffsession = tempStaff.getSessions();
		return staffsession;
	}
	
	public JSONArray findbystaff(String user_id){
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		List<Beacon> result = new ArrayList<Beacon>();
		List<Map> tempresult = new ArrayList<Map>();
		result = hibernateTemplate.find("from Beacon b where b.create_id = '" + user_id + "' order by last_modify_time desc");// Lynn add order by
		if (result != null && result.size() != 0) {
			if (result.size()<=100) {
				for (int i = 0; i < result.size(); i++) {
					Beacon findbBeacon = result.get(i);
					jsonObject.put("mac_id", findbBeacon.getMac_id());
					jsonObject.put("uuid", findbBeacon.getUuid());
					jsonObject.put("major", findbBeacon.getMajor());
					jsonObject.put("minor", findbBeacon.getMinor());
					jsonObject.put("building", findbBeacon.getBuilding());
					jsonObject.put("floor", findbBeacon.getFloor());
					jsonObject.put("coverage", coverageMap.get(findbBeacon.getCoverage()));
					jsonObject.put("power", findbBeacon.getPower());
					jsonObject.put("firm", findbBeacon.getFirm());
					jsonObject.put("type", findbBeacon.getType());
					//jsonObject.put("uuid", findbBeacon.getUuid());
					jsonObject.put("frequency", findbBeacon.getFrequency());
					jsonObject.put("coord_x", findbBeacon.getCoord_x());
					jsonObject.put("coord_y", findbBeacon.getCoord_y());
					jsonObject.put("frame", findbBeacon.getFrame());
					jsonObject.put("address", findbBeacon.getAddress());
					jsonObject.put("status", findbBeacon.getStatus());
					jsonObject.put("create_time", findbBeacon.getCreate_time());
					jsonObject.put("last_modify_time", findbBeacon.getLast_modify_time());
					jsonObject.put("address_type", findbBeacon.getAddress_type());
					
					tempresult =  hibernateTemplate.find("from Map m where m.unit_id = '" + findbBeacon.getBuilding() + "' and m.floor_id='" + findbBeacon.getFloor() + "' and m.map_style_id=4");
					if (tempresult != null && tempresult.size() != 0) {
						Map tempMap = tempresult.get(0);
						jsonObject.put("scale", tempMap.getScale());
					}
					jsonArray.add(jsonObject);
				}
				
			} else {
				for (int i = 0; i < 100; i++) {
					Beacon findbBeacon = result.get(i);
					jsonObject.put("mac_id", findbBeacon.getMac_id());
					jsonObject.put("uuid", findbBeacon.getUuid());
					jsonObject.put("major", findbBeacon.getMajor());
					jsonObject.put("minor", findbBeacon.getMinor());
					jsonObject.put("building", findbBeacon.getBuilding());
					jsonObject.put("floor", findbBeacon.getFloor());
					jsonObject.put("coverage", coverageMap.get(findbBeacon.getCoverage()));
					jsonObject.put("power", findbBeacon.getPower());
					jsonObject.put("firm", findbBeacon.getFirm());
					jsonObject.put("type", findbBeacon.getType());
					//jsonObject.put("uuid", findbBeacon.getUuid());
					jsonObject.put("frequency", findbBeacon.getFrequency());
					jsonObject.put("coord_x", findbBeacon.getCoord_x());
					jsonObject.put("coord_y", findbBeacon.getCoord_y());
					jsonObject.put("frame", findbBeacon.getFrame());
					jsonObject.put("address", findbBeacon.getAddress());
					jsonObject.put("status", findbBeacon.getStatus());
					jsonObject.put("create_time", findbBeacon.getCreate_time());
					jsonObject.put("last_modify_time", findbBeacon.getLast_modify_time());
					jsonObject.put("address_type", findbBeacon.getAddress_type());
					
					tempresult =  hibernateTemplate.find("from Map m where m.unit_id = '" + findbBeacon.getBuilding() + "' and m.floor_id='" + findbBeacon.getFloor() + "' and m.map_style_id=4");
					if (tempresult != null && tempresult.size() != 0) {
						Map tempMap = tempresult.get(0);
						jsonObject.put("scale", tempMap.getScale());
					}
					jsonArray.add(jsonObject);
				}
				JSONObject newObject = new JSONObject();
				newObject.put("total", String.valueOf(result.size()));
				jsonArray.add(newObject);
				
			}	
		}

		return jsonArray;
	}
	
	//Lynn-15-07-09 按照jsonstr条件搜索Beacon信息
	public JSONArray findBeacon(String jsonstr) {
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		List<Beacon> result = new ArrayList<Beacon>();
		List<Map> tempresult = new ArrayList<Map>();
		JSONObject jObject=JSONObject.fromObject(jsonstr);
		JSONObject tempObj = new JSONObject();
		
		if (jObject.has("user_id")&&jObject.getString("user_id")!=null&&!jObject.getString("user_id").equals("")) {
			tempObj.put("create_id", jObject.getString("user_id"));//添加数据库对应的字段
		}
		if (jObject.has("uuid")&&jObject.getString("uuid")!=null&&!jObject.getString("uuid").equals("")) {
			tempObj.put("uuid", jObject.getString("uuid"));
		}
		if (jObject.has("major")&&jObject.getString("major")!=null&&!jObject.getString("major").equals("")) {
			tempObj.put("major", jObject.getString("major"));
		}
		if (jObject.has("minor")&&jObject.getString("minor")!=null&&!jObject.getString("minor").equals("")) {
			tempObj.put("minor", jObject.getString("minor"));
		}
		if (jObject.has("start")&&jObject.getString("start")!=null&&!jObject.getString("start").equals("")) {
			tempObj.put("start", jObject.getString("start"));
		}
		if (jObject.has("end")&&jObject.getString("end")!=null&&!jObject.getString("end").equals("")) {
			tempObj.put("end", jObject.getString("end"));
		}
		if (jObject.has("status")&&jObject.getString("status")!=null&&!jObject.getString("status").equals("")) {
			System.out.println(statusMap.get(jObject.getString("status")));
			tempObj.put("status", statusMap.get(jObject.getString("status")).toString());
		}
		
		String sqlcon="from Beacon b where ";
		System.out.println(tempObj.size());
		Iterator<String>keys=tempObj.keys();
		String key ,value;
		for (int i = 0; i < tempObj.size(); i++) {
			key=keys.next();
			value=tempObj.getString(key);
			System.out.println(key+"-->"+value);
			if (i==tempObj.size()-1) {
				if (key=="start") {
					sqlcon += " b.last_modify_time" +" >= '"+ value +"'";
					
				} else if (key=="end") {
					sqlcon += " b.last_modify_time" +" <= '"+ value +"'";
				}
				else {
					sqlcon += " b." + key +" = '"+ value +"'";
				}
				
			} 
			else {
				if (key=="start") {
					sqlcon += " b.last_modify_time" +" >= '"+ value +"' and ";
				} else if (key=="end") {
					sqlcon += " b.last_modify_time" +" <= '"+ value +"' and ";
				}
				else {
					sqlcon += " b." + key +" = '"+ value +"' and ";
				}
			}		
		}
		System.out.println(sqlcon);
		
		result = hibernateTemplate.find(sqlcon);
		
		if (result != null && result.size() != 0) {
			for (int i = 0; i < result.size(); i++) {
				Beacon findbBeacon = result.get(i);
				jsonObject.put("mac_id", findbBeacon.getMac_id());
				jsonObject.put("uuid", findbBeacon.getUuid());
				jsonObject.put("major", findbBeacon.getMajor());
				jsonObject.put("minor", findbBeacon.getMinor());
				jsonObject.put("building", findbBeacon.getBuilding());
				jsonObject.put("floor", findbBeacon.getFloor());
				jsonObject.put("coverage", coverageMap.get(findbBeacon.getCoverage()));
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
				jsonObject.put("address_type", findbBeacon.getAddress_type());
				
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
	
	//新的版本部署工具上传，盼神做的。
 	public boolean re_deploy(JSONArray testArray, String user_id)
	{
		List<Beacon> result = new ArrayList<Beacon>();
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			for (int i = 0; i < testArray.size(); i++) {
				JSONObject testObject = testArray.getJSONObject(i);
				
				result = hibernateTemplate.find("from Beacon b where b.mac_id = '" + testObject.getString("mac") + "'");
				if(result == null || result.size() == 0){
					Beacon tempBeacon = new Beacon();
					tempBeacon.setMac_id(testObject.getString("mac") == null ? "" : testObject.getString("mac"));
					tempBeacon.setMajor(testObject.getString("major") == null ? "" : testObject.getString("major"));
					tempBeacon.setMinor(testObject.getString("minor") == null ? "" : testObject.getString("minor"));
					tempBeacon.setLast_modify_time(date);
					tempBeacon.setUuid(testObject.getString("uuid") == null ? "" : testObject.getString("uuid"));
					tempBeacon.setBuilding(testObject.getString("building") == null ? "" : testObject.getString("building"));
					tempBeacon.setFloor(testObject.getString("floor") == null ? "" : testObject.getString("floor"));
					tempBeacon.setCoord_x(testObject.getString("coord_x") == null ? "" : testObject.getString("coord_x"));
					tempBeacon.setCoord_y(testObject.getString("coord_y") == null ? "" : testObject.getString("coord_y"));
					tempBeacon.setAddress(testObject.getString("address") == null ? "" : testObject.getString("address"));
					tempBeacon.setStatus(testObject.getString("status") == null ?  "" : statusMap.get(testObject.getString("status")).toString());
					tempBeacon.setLast_modify_id(user_id);
					tempBeacon.setAddress_type(testObject.getString("address_type") == null ? "" : testObject.getString("address_type"));
					hibernateTemplate.save(tempBeacon);
				}
				else {
					Beacon tempBeacon = result.get(0);
					tempBeacon.setMajor(testObject.getString("major") == null ? "" : testObject.getString("major"));
					tempBeacon.setMinor(testObject.getString("minor") == null ? "" : testObject.getString("minor"));
					tempBeacon.setLast_modify_time(date);
					tempBeacon.setUuid(testObject.getString("uuid") == null ? "" : testObject.getString("uuid"));
					tempBeacon.setBuilding(testObject.getString("building") == null ? "" : testObject.getString("building"));
					tempBeacon.setFloor(testObject.getString("floor") == null ? "" : testObject.getString("floor"));
					tempBeacon.setCoord_x(testObject.getString("coord_x") == null ? "" : testObject.getString("coord_x"));
					tempBeacon.setCoord_y(testObject.getString("coord_y") == null ? "" : testObject.getString("coord_y"));
					tempBeacon.setAddress(testObject.getString("address") == null ? "" : testObject.getString("address"));
					tempBeacon.setStatus(testObject.getString("status") == null ?  "" : statusMap.get(testObject.getString("status")).toString() );
					tempBeacon.setAddress_type(testObject.getString("address_type") == null ? "" : testObject.getString("address_type"));
					tempBeacon.setLast_modify_id(user_id);
					hibernateTemplate.update(tempBeacon);
				}
				
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        System.gc();
    }

}
