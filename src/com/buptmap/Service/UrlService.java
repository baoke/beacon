package com.buptmap.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.MessageDao;
import com.buptmap.DAO.StaffDao;
import com.buptmap.DAO.Staff_devDAO;
import com.buptmap.model.Message;
import com.buptmap.model.Staff;
import com.buptmap.model.Staff_mes;
import com.buptmap.model.Vdev_mes_bind;
import com.buptmap.model.Vdevice;
import com.buptmap.util.WeChatAPI;

@Component
public class UrlService {
	private MessageDao messageDao;
	private Staff_devDAO staff_devDao;
	private StaffDao staffDao;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public Map<String, String> getLogoUrl(String local_logo){
		Map<String,String> map = new HashMap<String,String>();			
		map = WeChatAPI.getLogoUrl(local_logo);
		return map;
	}
	public Map<String,Object> addMessage(String json){
		lock.writeLock().lock();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			//判断major、minor再添加
			System.out.println(json);
			JSONArray jsonArray = JSONArray.fromObject(json);
			JSONObject o = jsonArray.getJSONObject(0);
			String staff_id = o.getString("parent_id");
			boolean flag = staffDao.verify_session(json);
			//Staff_dev sd = this.staff_devDao.checkExist(staff_id, uuid, major, minor);	
			
			if( !flag ){
				// major、minor范围错误
				map.put("success", false);
				map.put("message", "权限不足，无法绑定此URL");
				return map;
			}else{
				JSONArray devices = this.formatDevices(json);
				
				if(devices == null){
					map.put("success", false);
					map.put("message", "该beacon号段存在未注册设备");
					return map;
				}
				
				Message m = new Message();
				String content = o.getString("content");
				String name = o.getString("name");
				String other_info = o.getString("other_info");
				String title = o.getString("title");
				
				// 判断权限并调用微信接口
				Staff staff = staffDao.getone(staff_id);
				if(staff == null){
					map.put("success", false);
					map.put("message", "权限不足，无法绑定此URL");
					return map;
				}else{
					if(o.containsKey("logo")) {
						m.setLogo(o.getString("logo"));
					}
					if(o.containsKey("logo_url")){
						m.setLogo_url(o.getString("logo_url"));
					}
					
					m.setContent(content);
					m.setName(name);
					m.setOther_info(other_info);
					m.setProject_id(o.getString("project_id"));
					m.setStart_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					m.setEnd_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					m.setTitle(title);
					m.setLast_modify_id(staff_id);
					
					//有审核权限
					if(staff.getManage().equals("1") ){
						//上传的是图片地址
						if(o.containsKey("logo")){
							Map<String,String> logoResult = WeChatAPI.getLogoUrl(o.getString("logo"));
							//logo接口成功
							if(logoResult.containsKey("url")){
								m.setLogo_url(logoResult.get("url"));			
								Map<String,String> pageResult = WeChatAPI.getPageId(devices, title, name, content, other_info,logoResult.get("url"));
								//page接口成功
								if(pageResult.containsKey("page_id")){
									m.setStatus("2");
									m.setPage_id(pageResult.get("page_id"));
								}else{
									m.setStatus("3");
									map.put("pageError", pageResult.get("addPageError"));
								}
							}else{
								m.setStatus("3");
								map.put("logoError", logoResult.get("addLogoError"));
							}
						}
						
						//上传的是服务器logo
						if(o.containsKey("logo_url")){
							Map<String,String> pageResult = WeChatAPI.getPageId(devices, title, name, content, other_info,o.getString("logo_url"));
							//page接口成功
							if(pageResult.containsKey("page_id")){
								m.setStatus("2");
								m.setPage_id(pageResult.get("page_id"));
							}else{
								m.setStatus("3");
								map.put("pageError", pageResult.get("addPageError"));
							}
						}
							
					//无审核权限	
					}else if(staff.getManage().equals("0")){
						m.setStatus("1");
					}
					
					// 若保存message时出错?
					messageDao.save(m);

					Vdev_mes_bind  dev_mes = new Vdev_mes_bind();

					for(int n = 0; n < devices.size();n++){
						dev_mes.setMessage_id(m.getId()+"");
						dev_mes.setUuid(devices.getJSONObject(n).getString("uuid"));
						dev_mes.setMinor(devices.getJSONObject(n).getString("minor"));
						dev_mes.setMajor(devices.getJSONObject(n).getString("major"));
						dev_mes.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						staff_devDao.saveDev_mes(dev_mes);
					}	
					
					map.put("success", true);
					return map;
				}
			}
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	
	public Map<String,Object> editMessage(String json){
		lock.writeLock().lock();
		try{
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray jsonArray = JSONArray.fromObject(json);
		JSONObject o = jsonArray.getJSONObject(0);
		Message m = this.messageDao.message_one(Integer.parseInt(o.getString("url_id")));
		
		String content = o.getString("content");
		String name = o.getString("name");
		String other_info = o.getString("other_info");
		String title = o.getString("title");
		String staff_id = o.getString("staff_id");
		
		// 判断权限并调用微信接口
		
		Staff staff = staffDao.getone(staff_id);
		if(staff == null){
			map.put("success", false);
			map.put("message", "权限不足，无法绑定此URL");
			return map;
		}else{
			//有审核权限
			if(staff.getManage().equals("1") ){
				Map<String,String> pageResult = new HashMap<String,String>();
				//上传的是图片
				if(o.containsKey("logo")){
					Map<String,String> logoResult = WeChatAPI.getLogoUrl(o.getString("logo"));
					//logo接口成功
					if(logoResult.containsKey("url")){
						m.setLogo_url(logoResult.get("url"));			
						//判断page_id是否为空，进行新增或是修改
						if(m.getPage_id() == null || m.getPage_id().equals("")){
							List<Vdev_mes_bind> temp= staff_devDao.selectByMessage(m.getId()+"");
							if(temp != null){ 
								JSONArray devices = new JSONArray();
								JSONObject device = new JSONObject();
								for(int i = 0; i < temp.size();i++){
									device.put("uuid", temp.get(i).getUuid());
									device.put("major", temp.get(i).getMajor());
									device.put("minor", temp.get(i).getMinor());
									devices.add(device);
								}
								pageResult = WeChatAPI.getPageId(devices,title,name, content, other_info, logoResult.get("url"));
								//page添加接口成功
								if(pageResult.containsKey("page_id")){
									m.setStatus("2");
									m.setPage_id(pageResult.get("page_id"));
								}else{
									m.setStatus("3");
									map.put("pageError", pageResult.get("addPageError"));
								}
							}
						}else{
							pageResult = WeChatAPI.editPage(m.getPage_id(), title, name, content, other_info, logoResult.get("url"));
							//page修改接口成功
							if(pageResult.containsKey("page_id")){
								m.setStatus("2");
							}else{
								m.setStatus("3");
								map.put("pageError", pageResult.get("editPageError"));
							}
						}
					}else{
						m.setStatus("3");
						map.put("logoError", logoResult.get("addLogoError"));
					}				
				}
				
				//上传的是服务器logo
				if(o.containsKey("logo_url")){
					//判断page_id是否为空，进行新增或是修改
					if(m.getPage_id() == null || m.getPage_id().equals("")){
						List<Vdev_mes_bind> temp= staff_devDao.selectByMessage(m.getId()+"");
						if(temp != null){ 
							JSONArray devices = new JSONArray();
							JSONObject device = new JSONObject();
							for(int i = 0; i < temp.size();i++){
								device.put("uuid", temp.get(i).getUuid());
								device.put("major", temp.get(i).getMajor());
								device.put("minor", temp.get(i).getMinor());
								devices.add(device);
							}
							pageResult = WeChatAPI.getPageId(devices,title,name, content, other_info, o.getString("logo_url"));
							//page添加接口成功
							if(pageResult.containsKey("page_id")){
								m.setStatus("2");
								m.setPage_id(pageResult.get("page_id"));
							}else{
								m.setStatus("3");
								map.put("pageError", pageResult.get("addPageError"));
							}
						}
					}else{
						pageResult = WeChatAPI.editPage(m.getPage_id(), title, name, content, other_info, o.getString("logo_url"));
						//page修改接口成功
						if(pageResult.containsKey("page_id")){
							m.setStatus("2");
						}else{
							m.setStatus("3");
							map.put("pageError", pageResult.get("editPageError"));
						}
					}
				}
				
				//无审核权限
			}else if(staff.getManage().equals("0")){
				m.setStatus("1");
			}
			
		}
		
		map.put("success", true);
		m.setContent(content);
		
		if(o.containsKey("logo")){
			m.setLogo(o.getString("logo"));
		}
		if(o.containsKey("logo_url")){
			m.setLogo_url(o.getString("logo_url"));
		}
		
		m.setName(name);
		m.setOther_info(other_info);
		m.setProject_id(o.getString("project_id"));
		m.setEnd_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		m.setTitle(title);
		m.setLast_modify_id(staff_id);		
		
		 messageDao.update(m);
		 
		 return map;
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray checkDevice(String json){
		JSONArray result = new JSONArray();
		JSONObject deviceObj = new JSONObject();
		List<Vdev_mes_bind> devices = this.getDevices(json);
		if(devices != null && devices.size() > 0){
			for(int d = 0; d < devices.size(); d++){
				deviceObj.put("major", devices.get(d).getMajor());
				deviceObj.put("minor", devices.get(d).getMinor());
				result.add(deviceObj);
			}
		}
		return result;
	}
	
	public boolean checkMessage(String json){
		JSONArray jsonArray = JSONArray.fromObject(json);
		JSONObject obj = jsonArray.getJSONObject(0);
		String url = obj.getString("url");
		List<Message> messages = messageDao.checkUrl(url);
		if( messages != null ){
			return messages.size() > 0 ? false:true;
		}else{
			return true;
		}
	}

	public JSONObject delete(int message_id){
		List<Vdev_mes_bind> temp= staff_devDao.selectByMessage(message_id+"");
		JSONObject result = new JSONObject();
		Message m = this.messageDao.message_one(message_id);
		if(temp != null && m != null){
			if(m.getPage_id() != null && !m.getPage_id().equals("")){
				result = WeChatAPI.deletePage(temp,m.getPage_id());
				if(result.getBoolean("page_message") && result.getBoolean("device_message")){
					m.setStatus("4");
					messageDao.update(m);
				}
			}else{
				m.setStatus("4");
				messageDao.update(m);
				result.put("page_message", true);
				result.put("device_message", true);
				result.put("description", "Success");
			}
			return result;
		}else{
			return null;
		}
		
	}
	
	public JSONArray showList(String staff_id) {
		lock.writeLock().lock();
		try{
			JSONArray result = messageDao.message_list(staff_id);
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();			
			JSONObject majorObject = new JSONObject();
    			if (result != null) {
    				for (int i = 0; i < result.size(); i++) {
    					JSONArray majorsArray = new JSONArray();
    					JSONObject tempMessage = result.getJSONObject(i);
    					for(int j= i+1;j < result.size();j++){
    						if(result.getJSONObject(j).getInt("message_id") ==  tempMessage.getInt("message_id")){
    							majorObject.put("major", result.getJSONObject(j).getString("major"));
    							majorObject.put("minor", result.getJSONObject(j).getString("minor"));
    							majorsArray.add(majorObject);
    							result.remove(j);
    							j--;
    						}
    					}
    					
    					majorObject.put("major", tempMessage.getString("major"));
    					majorObject.put("minor", tempMessage.getString("minor"));
						majorsArray.add(majorObject);
    							
						jsonObject.put("message_id", tempMessage.getInt("message_id"));
    					jsonObject.put("title", tempMessage.getString("title"));
    					jsonObject.put("name", tempMessage.getString("name"));
    					jsonObject.put("content",tempMessage.getString("content"));
    					jsonObject.put("project_title",tempMessage.getString("project_title"));
    					jsonObject.put("end_time",tempMessage.getString("end_time"));
    					jsonObject.put("status", tempMessage.getString("status"));
    					jsonObject.put("logo_url", tempMessage.getString("logo_url"));
    					jsonObject.put("page_id", tempMessage.getString("page_id"));
    					jsonObject.put("project_id", tempMessage.getString("project_id"));
    					jsonObject.put("other_info", tempMessage.getString("other_info"));
    					jsonObject.put("majors", majorsArray);
    					jsonArray.add(jsonObject);
    					
    					majorsArray = null;
    					tempMessage = null;
    				}
    				return jsonArray;
    			}else{
    				return null;
    			}
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	public JSONArray showListWithSession(String staff_id) {
		lock.writeLock().lock();
		
		try{
			JSONArray result = messageDao.message_list(staff_id);
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();		
    			if (result != null) {
    				for (int i = 0; i < result.size(); i++) {
    					JSONObject tempObj = result.getJSONObject(i);
    					JSONObject majorResult = new JSONObject();
    					JSONObject majorObject = new JSONObject();
    			
						List<Integer> minors = new ArrayList<Integer>(); 
						minors.add(Integer.parseInt(tempObj.getString("minor")));
						majorObject.put(tempObj.getString("major"), minors);
    					
    					//去除重复的message，还是用sql distinct去重效率更好(类似detailWithSession)?
    					for(int j= i+1;j < result.size();j++){
    						if(result.getJSONObject(j).getInt("message_id") == tempObj.getInt("message_id")){
    							if(majorObject.containsKey(result.getJSONObject(j).getString("major"))){
    								List<Integer> tempMi = (List<Integer>) majorObject.get(result.getJSONObject(j).getString("major"));
    								tempMi.add(Integer.parseInt(result.getJSONObject(j).getString("minor")));
    							}else{
    								List<Integer> tempMi2 = new ArrayList<Integer>(); 
    								tempMi2.add(Integer.parseInt(result.getJSONObject(j).getString("minor")));
    								majorObject.put(result.getJSONObject(j).getString("major"), tempMi2);
    							}
    							result.remove(j);
    							j--;
    						}
    					}					
    					
    					
    					for(Object key : majorObject.keySet()){
    						JSONObject minorObject = new JSONObject();
        					JSONArray minorArray = new JSONArray();
        					
        					//前提是数据库中取出的minor排序正确，否则需要对minorT重新排序
    						List<Integer> minorT =(List<Integer>) majorObject.get(key);
    						if(minorT != null){
        						if(minorT.size() == 1){
        							minorObject.put("value0", minorT.get(0));
        							minorObject.put("value1", minorT.get(0));
        							minorArray.add(minorObject);
        						}else{
        							int m = 0; int n = 1;
        							for( ; n < minorT.size(); n++){
        								if(minorT.get(n) - minorT.get(n-1) > 1){
        									minorObject.put("value0", minorT.get(m));
        									minorObject.put("value1", minorT.get(n-1));
        									m = n;
        									minorArray.add(minorObject);
        								}
        							}
        							minorObject.put("value0", minorT.get(m));
        							minorObject.put("value1", minorT.get(n-1));
        							minorArray.add(minorObject);
        						}
        					}

    						majorResult.put(key, minorArray);
    						
    						minorObject = null;
    						minorArray = null;
						}
						
    					jsonObject.put("message_id", tempObj.getInt("message_id"));
    					jsonObject.put("title", tempObj.getString("title"));
    					jsonObject.put("name", tempObj.getString("name"));
    					jsonObject.put("content",tempObj.getString("content"));
    					jsonObject.put("project_title",tempObj.getString("project_title"));
    					jsonObject.put("end_time",tempObj.getString("end_time"));
    					jsonObject.put("status", tempObj.getString("status"));
    					jsonObject.put("logo_url", tempObj.getString("logo_url"));
    					jsonObject.put("page_id", tempObj.getString("page_id"));
    					jsonObject.put("project_id", tempObj.getString("project_id"));
    					jsonObject.put("other_info", tempObj.getString("other_info"));
    					jsonObject.put("sessions",majorResult);
    					jsonArray.add(jsonObject);
    					
    					majorResult = null;
    					minors = null;
    					majorObject = null;
    				}
    				return jsonArray;
    			}else{
    				return null;
    			}
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	public JSONArray showDeList(int message_id,String staff_id) {
		lock.writeLock().lock();
		try{
			List<Staff_mes> result = messageDao.message_deList(message_id,staff_id);
			JSONArray jsonArray = new JSONArray();
			JSONArray sessionArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();			
			JSONObject sessionObject = new JSONObject();
			Staff_mes tempMessage = new Staff_mes();
			if(result != null  ){
				for(int i = 0; i < result.size(); i++){
					tempMessage = result.get(i);
					sessionObject.put("uuid", tempMessage.getUuid()==null?"":tempMessage.getUuid());
					sessionObject.put("major", tempMessage.getMajor()==null?"":tempMessage.getMajor());
					sessionObject.put("minor", tempMessage.getMinor()==null?"":tempMessage.getMinor());
					sessionArray.add(sessionObject);
				}
				
				tempMessage = result.get(0);
				jsonObject.put("message_id", tempMessage.getMessage_id());
				jsonObject.put("title", tempMessage.getTitle()==null?"":tempMessage.getTitle());
				jsonObject.put("name", tempMessage.getName()==null?"":tempMessage.getName());
				jsonObject.put("content", tempMessage.getContent()==null?"":tempMessage.getContent());
				jsonObject.put("start_time", tempMessage.getStart_time()==null?"":tempMessage.getStart_time());
				jsonObject.put("logo", tempMessage.getLogo()==null?"":tempMessage.getLogo());	
				jsonObject.put("status", tempMessage.getStatus()==null?"":tempMessage.getStatus());
				jsonObject.put("other_info", tempMessage.getOther_info()==null?"":tempMessage.getOther_info());
				jsonObject.put("page_id", tempMessage.getPage_id()==null?"":tempMessage.getPage_id());
				jsonObject.put("project_id", tempMessage.getProject_id()==null?"":tempMessage.getProject_id());
				jsonObject.put("last_modify_id", tempMessage.getLast_modify_id()==null?"":tempMessage.getLast_modify_id());
				jsonObject.put("project_title", tempMessage.getPr_title()==null?"":tempMessage.getPr_title());
				jsonObject.put("end_time", tempMessage.getEnd_time()==null?"":tempMessage.getEnd_time());
				jsonObject.put("logo_url", tempMessage.getLogo_url()==null?"":tempMessage.getLogo_url());
				jsonObject.put("staff_id", tempMessage.getStaff_id()==null?"":tempMessage.getStaff_id());				
				jsonObject.put("pr_begin_time", tempMessage.getPr_begin_time()==null?"":tempMessage.getPr_begin_time());
				jsonObject.put("pr_end_time", tempMessage.getPr_end_time()==null?"":tempMessage.getPr_end_time());
				jsonObject.put("sessions", sessionArray);
				jsonArray.add(jsonObject);
				return jsonArray;
			}
			return null;
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	public JSONArray showDetailWithSession(int message_id,String staff_id) {
		lock.writeLock().lock();
		try{
			List<Staff_mes> result = messageDao.message_deList(message_id,staff_id);
			JSONArray jsonArray = new JSONArray();
			
			JSONObject jsonObject = new JSONObject();			
			Staff_mes tempMessage = new Staff_mes();
			
			if(result != null) {	
				
			tempMessage = result.get(0);
			jsonObject.put("message_id", tempMessage.getMessage_id());
			jsonObject.put("title", tempMessage.getTitle()==null?"":tempMessage.getTitle());
			jsonObject.put("name", tempMessage.getName()==null?"":tempMessage.getName());
			jsonObject.put("content", tempMessage.getContent()==null?"":tempMessage.getContent());
			jsonObject.put("start_time", tempMessage.getStart_time()==null?"":tempMessage.getStart_time());
			jsonObject.put("logo", tempMessage.getLogo()==null?"":tempMessage.getLogo());	
			jsonObject.put("status", tempMessage.getStatus()==null?"":tempMessage.getStatus());
			jsonObject.put("other_info", tempMessage.getOther_info()==null?"":tempMessage.getOther_info());
			jsonObject.put("page_id", tempMessage.getPage_id()==null?"":tempMessage.getPage_id());
			jsonObject.put("project_id", tempMessage.getProject_id()==null?"":tempMessage.getProject_id());
			jsonObject.put("last_modify_id", tempMessage.getLast_modify_id()==null?"":tempMessage.getLast_modify_id());
			jsonObject.put("project_title", tempMessage.getPr_title()==null?"":tempMessage.getPr_title());
			jsonObject.put("end_time", tempMessage.getEnd_time()==null?"":tempMessage.getEnd_time());
			jsonObject.put("logo_url", tempMessage.getLogo_url()==null?"":tempMessage.getLogo_url());
			jsonObject.put("staff_id", tempMessage.getStaff_id()==null?"":tempMessage.getStaff_id());				
			jsonObject.put("pr_begin_time", tempMessage.getPr_begin_time()==null?"":tempMessage.getPr_begin_time());
			jsonObject.put("pr_end_time", tempMessage.getPr_end_time()==null?"":tempMessage.getPr_end_time());
			jsonObject.put("sessions", this.getUUIDs(message_id, staff_id));
			jsonArray.add(jsonObject);
			return jsonArray;
			}
			
			return null;
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	public JSONArray getUUIDs(int message_id,String staff_id){
		JSONArray sessionArray = new JSONArray();
		JSONObject sessionObject = new JSONObject();
		
		List<String> uuids = staff_devDao.distinctUUID(message_id, staff_id);
		if(uuids != null){
			for(int i = 0; i < uuids.size(); i++){
				sessionObject.put("uuid", uuids.get(i));
				sessionObject.put("majors", getMajors(message_id,staff_id,uuids.get(i)));
				sessionArray.add(sessionObject);
			}
		}
		
		return sessionArray;
	}
	
	public JSONArray getMajors(int message_id,String staff_id,String uuid){
		JSONArray majorArray = new JSONArray();
		JSONObject majorObject = new JSONObject();
		
		List<String> majors = staff_devDao.distinctMajor(message_id, staff_id, uuid);
		if(majors != null){
			for(int i = 0; i < majors.size(); i++){
				majorObject.put("major", majors.get(i));
				majorObject.put("minors", this.getMinors(message_id, staff_id, uuid, majors.get(i)));
				majorArray.add(majorObject);
			}
		}
		return majorArray;
	}
	
	public JSONArray getMinors(int message_id,String staff_id,String uuid,String major){
		JSONArray minorArray = new JSONArray();
		JSONObject minorObject = new JSONObject();
		
		int[] minors = staff_devDao.findMinors(message_id, staff_id, uuid, major);
		if(minors != null){
			Arrays.sort(minors);
			if(minors.length == 1){
				minorObject.put("value0", minors[0]);
				minorObject.put("value1", minors[0]);
				minorArray.add(minorObject);
			}else{
				int m = 0; int n = 1;
				for( ; n < minors.length; n++){
					if(minors[n] - minors[n-1] > 1){
						minorObject.put("value0", minors[m]);
						minorObject.put("value1", minors[n-1]);
						m = n;
						minorArray.add(minorObject);
					}
				}
				minorObject.put("value0", minors[m]);
				minorObject.put("value1", minors[n-1]);
				minorArray.add(minorObject);
			}
		}
		
		return minorArray;
	}
	
	
	
	/**
	 * 
	 * @param json
	 * @return 根据传入的session字段取出所有在该范围内的devices
	 */
	private List<Vdev_mes_bind> getDevices(String json){
		List<Vdev_mes_bind> total = new ArrayList<Vdev_mes_bind>();
		List<Vdev_mes_bind> temp = new ArrayList<Vdev_mes_bind>();
		JSONArray jsonArray = JSONArray.fromObject(json);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		JSONArray uuidArray = jsonObject.getJSONArray("session");
		
		String uuid = null;
		String major = null;
		String start = null;
		String end = null;
		for(int i = 0; i<uuidArray.size();i++){
			
			uuid = uuidArray.getJSONObject(i).getString("value");
			JSONArray majorArray = uuidArray.getJSONObject(i).getJSONArray("majors");
			
			for(int j=0; j<majorArray.size();j++){
				major = majorArray.getJSONObject(j).getString("value");
				JSONArray minorArray = majorArray.getJSONObject(j).getJSONArray("sections");
				
				for(int k=0; k<minorArray.size(); k++){
					start = minorArray.getJSONObject(k).getString("value0");
					end = minorArray.getJSONObject(k).getString("value1");
					temp = staff_devDao.checkMinors(uuid, major, start, end);
					if(temp != null){
						total.addAll(temp);
					}
				}
			}	
			
		}
		
		/*String[] device_ids = new String[total.size()];
		for(int l=0;l<total.size();l++){
			device_ids[l] = total.get(l).getVdevice_id();
		}
		return device_ids;*/
		return total;
	}
	
	/**
	 * 
	 * @param json
	 * @return 根据传入的session字段取出所有在该范围内的device,组装成JSONArray
	 */
	private JSONArray formatDevices(String json){
	
		JSONArray jsonArray = JSONArray.fromObject(json);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		JSONArray uuidArray = jsonObject.getJSONArray("session");
		
		String uuid = null;
		String major = null;
		int start = 0;
		int end = 0;
		JSONArray total = new JSONArray();
		JSONObject temp = new JSONObject();
		List<Vdevice> devTemp = new ArrayList<Vdevice>();
		
		for(int i = 0; i<uuidArray.size();i++){
			
			uuid = uuidArray.getJSONObject(i).getString("value");
			JSONArray majorArray = uuidArray.getJSONObject(i).getJSONArray("majors");
			
			for(int j=0; j<majorArray.size();j++){
				major = majorArray.getJSONObject(j).getString("value");
				JSONArray minorArray = majorArray.getJSONObject(j).getJSONArray("sections");
				
				for(int k=0; k<minorArray.size(); k++){
					start = Integer.parseInt(minorArray.getJSONObject(k).getString("value0"));
					end = Integer.parseInt(minorArray.getJSONObject(k).getString("value1"));
					//验证号段有对应的device_id
					devTemp = staff_devDao.checkDeviceId(uuid, major, start+"", end+"");
					if(devTemp != null&&devTemp.size() > 0){
						return null;
					}
					
					while(start <= end){
							temp.put("uuid", uuid);
							temp.put("major", major);
							temp.put("minor", start+"");
							total.add(temp);
							start++;
					}
				}
			}	
			
		}
		return total;
	}
	
	public MessageDao getMessageDao() {
		return messageDao;
	}
	@Resource
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public Staff_devDAO getStaff_devDao() {
		return staff_devDao;
	}
	@Resource
	public void setStaff_devDao(Staff_devDAO staff_devDao) {
		this.staff_devDao = staff_devDao;
	}

	public StaffDao getStaffDao() {
		return staffDao;
	}
	@Resource
	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	
}
