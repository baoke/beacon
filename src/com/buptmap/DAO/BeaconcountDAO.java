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

import com.buptmap.model.Beaconcount;
import com.buptmap.model.Staff;


@Component("beaconcountDao")
public class BeaconcountDAO {

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
	
	public boolean add(String jsonstr){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		jsonArray = JSONArray.fromObject(jsonstr);
		jsonObject = jsonArray.getJSONObject(0);
		Beaconcount beaconcount = new Beaconcount();
		beaconcount.setAmount(Integer.valueOf(jsonObject.getString("amount")));
		beaconcount.setReceive_id(jsonObject.getString("receive_id"));
		beaconcount.setSend_id(jsonObject.getString("send_id"));
		beaconcount.setTime(date);
		beaconcount.setType_id(jsonObject.getString("type_id"));
		hibernateTemplate.save(beaconcount);
		return true;
	}
	
	public JSONArray find(String receive_id, String  send_id){
		List<Beaconcount> result = new ArrayList<Beaconcount>();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		
		result = hibernateTemplate.find("from Beaconcount where receive_id='" +receive_id+ "' and send_id='" +send_id+ "'");
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				Beaconcount beaconcount = result.get(i);
				jsonObject.put("type", beaconcount.getType_id());
				jsonObject.put("amount", beaconcount.getAmount());
				jsonObject.put("time", beaconcount.getTime());
				jsonArray.add(jsonObject);
				
			}
		}
		return jsonArray;
	}

	public JSONArray total(String  send_id){
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> tempList = new ArrayList<Object[]>();
		List<Beaconcount> result1 = new ArrayList<Beaconcount>();
		List<Object[]> result2 = new ArrayList<Object[]>();
		List<Object[]> result3 = new ArrayList<Object[]>();
		List<Staff> staffList = new ArrayList<Staff>();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		Object[] receiveObjects = null;
		try {
			tempList = hibernateTemplate.find("select staff_id,staff_name from Staff where parent_id='" +send_id+ "'");
			if (tempList != null && tempList.size() > 0) {
				
				for (int i = 0; i < tempList.size(); i++) {
					receiveObjects = tempList.get(i);
					String receive_id = receiveObjects[0].toString();
					jsonObject.put("receive_id", receive_id);	
					jsonObject.put("receive_name", receiveObjects[1].toString());
					ArrayList<String> childList = new ArrayList<String>();
					childList.add(receive_id);
					int used = 0;
					int recover = 0;
					for (int j = 0; j < childList.size(); j++) {
						staffList = hibernateTemplate.find("from Staff where parent_id='" +childList.get(j)+ "'");
						if (staffList != null && staffList.size() != 0) {
							for (int l = 0; l < staffList.size(); l++) {
								childList.add(staffList.get(l).getStaff_id());
							}
						}
						result2 = hibernateTemplate.find("from Beacon b where b.status!='回收' and b.create_id = '" + childList.get(j) + "'");
						if (result2 != null && result2.size() != 0) {
							used += result2.size();
						}
						result3 = hibernateTemplate.find("from Beacon b where b.status='回收' and b.create_id = '" + childList.get(j) + "'");
						if (result3 != null && result3.size() != 0) {
							recover += result3.size();
						}
					}
					jsonObject.put("used", used);		
					jsonObject.put("recover", recover);		
					result1 = hibernateTemplate.find("from Beaconcount where receive_id='" +receive_id+ "' and send_id='" +send_id+ "' order by time desc");
					
					if (result1 != null && result1.size() != 0) {
						int account = 0;
						for (int j = 0; j < result1.size(); j++) {
							Beaconcount beaconcount = result1.get(j);
							account += beaconcount.getAmount();
						}
						jsonObject.put("all", account);
						jsonObject.put("time", result1.get(0).getTime());
					}
					else {
						jsonObject.put("all", 0);
						jsonObject.put("time", "");
					}
					jsonArray.add(jsonObject);
					
				}
			}
			return jsonArray;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return jsonArray;
		}
		
	}

}
