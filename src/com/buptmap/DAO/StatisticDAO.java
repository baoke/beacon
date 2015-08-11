package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import com.buptmap.model.Mes_page_statistic;
import com.buptmap.model.Project;
import com.buptmap.model.Staff;
import com.buptmap.model.Staff_dev_statistic;
import com.buptmap.model.Vdev_mes_bind;


@Component("statisticDao")

public class StatisticDAO implements Serializable{
	
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
	
	public JSONArray findStatisticMinor(String jsonString) {
		System.out.println("******findStatisticMinor*******");
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		
		JSONObject jObject=JSONObject.fromObject(jsonString);
		JSONObject tempObj = new JSONObject();
		JSONObject tempObj2 = new JSONObject();
		String staffId =jObject.getString("staff_id") ;

		try {
			System.out.println(jsonString);
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
				tempObj2.put("start", jObject.getString("start"));
			}
			if (jObject.has("end")&&jObject.getString("end")!=null&&!jObject.getString("end").equals("")) {
				tempObj2.put("end", jObject.getString("end"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String sqlStringm = "from Staff_dev_statistic s where s.staff_id='"+staffId+"' and s.uuid='"+jObject.getString("uuid")+"' and s.major='"+jObject.getString("major")+"' and s.minor='"+jObject.getString("minor")+"'";
		if (tempObj2.size()>0) {
			Iterator<String>keys2=tempObj2.keys();
			String key2 ,value2,t_value2;
			for (int j = 0; j < tempObj2.size(); j++) {
				key2=keys2.next();
				value2=tempObj2.getString(key2);
				if(j==tempObj2.size()-1)
				{
					if (key2=="start") {
						t_value2=value2+" 00:00:00";
						sqlStringm += " and s.ftime" +" >= '"+ t_value2 +"'";
						
					} else if (key2=="end") {
						t_value2=value2+" 23:59:59";
						sqlStringm += " and s.ftime" +" <= '"+ t_value2 +"'";
					}
				}
				else {
					if (key2=="start") {
						t_value2=value2+" 00:00:00";
						sqlStringm += " and s.ftime" +" >= '"+ t_value2 +"'";
					} else if (key2=="end") {
						t_value2=value2+" 23:59:59";
						sqlStringm += " and s.ftime" +" <= '"+ t_value2 +"'";
					}
				}
			}
			
		}
		
		System.out.println("-->"+sqlStringm);
		System.out.println("-->"+"select uuid,major,minor,staff_id,shake_uv,shake_pv,click_uv,click_pv,ftime "+sqlStringm);
		List<Object[]>objects =new ArrayList<Object[]>();
		objects=hibernateTemplate.find("select uuid,major,minor,staff_id,shake_uv,shake_pv,click_uv,click_pv,ftime "+sqlStringm);
		if (objects!=null && objects.size()>0) {
			for (int i = 0; i < objects.size(); i++) {
				Object[] findobject =objects.get(i);
				jsonObject.put("uuid",findobject[0].toString() );
				jsonObject.put("major", findobject[1].toString());
				jsonObject.put("minor", findobject[2].toString());
				jsonObject.put("staff_id", findobject[3].toString());
				jsonObject.put("shake_uv", findobject[4].toString());
				jsonObject.put("shake_pv", findobject[5].toString());
				jsonObject.put("click_uv", findobject[6].toString());
				jsonObject.put("click_pv", findobject[7].toString());
				jsonObject.put("ftime", findobject[8].toString());	
				jsonArray.add(jsonObject);		
				
				
			}
		}
		
		return jsonArray;
	}	
	
	public JSONArray findStatisticUrl(String jsonString) {
		System.out.println("******findStatisticUrl*******");
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		JSONObject jObject=JSONObject.fromObject(jsonString);
		
		/*时间筛选*/
		JSONObject tempObj2 = new JSONObject();
		String sqltime="";
		String staffId = jObject.getString("staff_id");
		try {
			if (jObject.has("start")&&jObject.getString("start")!=null&&!jObject.getString("start").equals("")) {
				tempObj2.put("start", jObject.getString("start"));
			}
			if (jObject.has("end")&&jObject.getString("end")!=null&&!jObject.getString("end").equals("")) {
				tempObj2.put("end", jObject.getString("end"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (tempObj2.size()>0) {
			Iterator<String>keys2=tempObj2.keys();
			String key2 ,value2,t_value2;
			
			for (int j = 0; j < tempObj2.size(); j++) {
				key2=keys2.next();
				value2=tempObj2.getString(key2);
				//System.out.println(key2+"-->"+t_value2.toString());
				if (key2=="start") {
					t_value2=value2+" 00:00:00";
					sqltime += " and m.ftime" +" >= '"+ t_value2 +"'";
				} else if (key2=="end") {
					t_value2=value2+" 23:59:59";
					sqltime += " and m.ftime" +" <= '"+ t_value2 +"'";

				}
				
			}
		}
		System.out.println("sqltime--->"+sqltime);
		/*时间筛选*/
		
		
		String value2=jObject.getString("title");
		String sqlString="select distinct page_id, mes_title,mes_name,mes_content,staff_id,shake_uv,shake_pv,click_uv,click_pv,ftime from Mes_page_statistic m where m.staff_id='"+staffId+"' and ( m.mes_title like '%"+value2+"%' or m.mes_name like '%"+value2+"%' ) and m.ftime is not null ";
		if (sqltime!=null&&!sqltime.equals("")) {
			sqlString+=sqltime;
		}
		System.out.println(sqlString);
		List<Object[]>objects=new ArrayList<Object[]>();
		objects= hibernateTemplate.find(sqlString);
		if (objects!=null && objects.size()>0) {
			for (int i = 0; i < objects.size(); i++) {
				Object[] findMpStatistic= objects.get(i);
				jsonObject.put("page_id",findMpStatistic[0].toString() );
				jsonObject.put("title",findMpStatistic[1].toString() );
				jsonObject.put("name", findMpStatistic[2].toString());
				jsonObject.put("content",findMpStatistic[3].toString());
				jsonObject.put("staff_id", findMpStatistic[4].toString());
				jsonObject.put("shake_uv", findMpStatistic[5].toString());
				jsonObject.put("shake_pv", findMpStatistic[6].toString());
				jsonObject.put("click_uv", findMpStatistic[7].toString());
				jsonObject.put("click_pv", findMpStatistic[8].toString());
				jsonObject.put("ftime", findMpStatistic[9].toString());	
				jsonArray.add(jsonObject);		
				
			}
		}
		
		return jsonArray;
	}
	public JSONArray findStatisticProject(String jsonString) {
		System.out.println("******findStatisticProject*******");
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		JSONObject jObject=JSONObject.fromObject(jsonString);
		//String findStyle=jObject.getString("style");
		
		
		
		/*时间筛选*/
		JSONObject tempObj2 = new JSONObject();
		String sqltime="";
		try {
			if (jObject.has("start")&&jObject.getString("start")!=null&&!jObject.getString("start").equals("")) {
				tempObj2.put("start", jObject.getString("start"));
			}
			if (jObject.has("end")&&jObject.getString("end")!=null&&!jObject.getString("end").equals("")) {
				tempObj2.put("end", jObject.getString("end"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (tempObj2.size()>0) {
			Iterator<String>keys2=tempObj2.keys();
			String key2 ,value2,t_value2;
			
			for (int j = 0; j < tempObj2.size(); j++) {
				key2=keys2.next();
				value2=tempObj2.getString(key2);
				//System.out.println(key2+"-->"+t_value2.toString());
				if(j==tempObj2.size()-1)
				{
					if (key2=="start") {
						t_value2=value2+" 00:00:00";

							sqltime += " and ftime" +" >= '"+ t_value2 +"'";

						
					} else if (key2=="end") {
						t_value2=value2+" 23:59:59";

							sqltime += " and ftime" +" <= '"+ t_value2 +"'";

					}
				}
				else {
					if (key2=="start") {
						t_value2=value2+" 00:00:00";

							sqltime += " and ftime" +" >= '"+ t_value2 +"'";
						
					} else if (key2=="end") {
						t_value2=value2+" 23:59:59";

							sqltime += " and ftime" +" <= '"+ t_value2 +"'";
						
					}
				}
			}
		}
		System.out.println("sqltime--->"+sqltime);
		/*时间筛选*/
		
		
		String value3=jObject.getString("name");
		String sqlString="SELECT DISTINCT page_id,project_id,pro_title,shake_pv,shake_uv,click_pv,click_uv,ftime FROM  Mes_page_statistic where staff_id='"+jObject.getString("staff_id")+"' and pro_title like '%"+value3+"%' ";
		if (sqltime!=null && !sqltime.equals("")) {
			sqlString+=sqltime;
		}
		sqlString+=" order by project_id, ftime";
		System.out.println("sqltime--->"+sqlString);
		int sum_sp=0,sum_su=0,sum_cp=0,sum_cu=0;
		String pro_id_old="",pro_id_new="",pro_title="",f_time_old="",f_time_new="";
		
		List<Object[]>jsonObjects = new ArrayList<Object[]>();
		jsonObjects = hibernateTemplate.find(sqlString);
		if (jsonObjects!=null && jsonObjects.size()>0) {
			for (int i = 0; i < jsonObjects.size(); i++) {
				Object[] findObject = jsonObjects.get(i);
				
				if (i==0) {
					pro_id_old=findObject[1].toString();
					pro_title=findObject[2].toString();
					sum_sp=Integer.valueOf(findObject[3].toString());
					sum_su=Integer.valueOf(findObject[4].toString());
					sum_cp=Integer.valueOf(findObject[5].toString());
					sum_cu=Integer.valueOf(findObject[6].toString());
					f_time_old=findObject[7].toString();
					continue;
				}
				pro_id_new=findObject[1].toString();
				f_time_new=findObject[7].toString();
				if (pro_id_new.equals(pro_id_old)) {
					if (f_time_new.equals(f_time_old)) {
						sum_sp+=Integer.valueOf(findObject[3].toString());
						sum_su+=Integer.valueOf(findObject[4].toString());
						sum_cp+=Integer.valueOf(findObject[5].toString());
						sum_cu+=Integer.valueOf(findObject[6].toString());
					}
					else {
						//jsonObject.put("page_id", pro_id_old );
						jsonObject.put("project_id", pro_id_old );
						jsonObject.put("title",pro_title );
						jsonObject.put("shake_pv", sum_sp );
						jsonObject.put("shake_uv", sum_su );
						jsonObject.put("click_pv", sum_cp );
						jsonObject.put("click_uv", sum_cu  );
						jsonObject.put("ftime", f_time_old );
						jsonArray.add(jsonObject);	
						
						sum_sp=Integer.valueOf(findObject[3].toString());
						sum_su=Integer.valueOf(findObject[4].toString());
						sum_cp=Integer.valueOf(findObject[5].toString());
						sum_cu=Integer.valueOf(findObject[6].toString());
						f_time_old=f_time_new;
						
					}
					
					
				}
				else {
					
					jsonObject.put("project_id", pro_id_old );
					jsonObject.put("title",pro_title );
					jsonObject.put("shake_pv", sum_sp );
					jsonObject.put("shake_uv", sum_su );
					jsonObject.put("click_pv", sum_cp );
					jsonObject.put("click_uv", sum_cu  );
					jsonObject.put("ftime", f_time_old );
					jsonArray.add(jsonObject);	
					
					pro_id_old=pro_id_new;
					pro_title=findObject[2].toString();
					sum_sp=Integer.valueOf(findObject[3].toString());
					sum_su=Integer.valueOf(findObject[4].toString());
					sum_cp=Integer.valueOf(findObject[5].toString());
					sum_cu=Integer.valueOf(findObject[6].toString());
					f_time_old=findObject[7].toString();
				}
				
			}
			jsonObject.put("project_id", pro_id_old );
			jsonObject.put("title",pro_title );
			jsonObject.put("shake_pv", sum_sp );
			jsonObject.put("shake_uv", sum_su );
			jsonObject.put("click_pv", sum_cp );
			jsonObject.put("click_uv", sum_cu  );
			jsonObject.put("ftime", f_time_old );
			jsonArray.add(jsonObject);	
				
			
		}
		
		
		return jsonArray;
	}

	public boolean InStaff(String jsonString) {
		boolean returnValue=false;
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
//		List<Vdevice> vdevices=new ArrayList<Vdevice>();
//		List<Message>messages = new ArrayList<Message>();
//		List<Vdev_mes_bind>vdev_mes_binds = new ArrayList<Vdev_mes_bind>();
//		List<Vdev_staff_bind>vdev_staff_binds = new ArrayList<Vdev_staff_bind>();
		List<Staff_dev_statistic>staff_dev_statistics = new ArrayList<Staff_dev_statistic>();
		List<Mes_page_statistic>mes_page_statistics = new ArrayList<Mes_page_statistic>();
		List<Project>projects = new ArrayList<Project>();
		//List<Map> tempresult = new ArrayList<Map>();
		JSONObject jObject=JSONObject.fromObject(jsonString);
		JSONObject tempObj = new JSONObject();//minor
		JSONObject tempObj2 = new JSONObject();//url
		JSONObject tempObj3 = new JSONObject();//project
		
		String StaffId=jObject.getString("staff_id");
		if(StaffId.equalsIgnoreCase("super"))
		{
			return true;
		}
		
		try {
			
			if (jObject.has("uuid")&&jObject.getString("uuid")!=null&&!jObject.getString("uuid").equals("")) {
				tempObj.put("uuid", jObject.getString("uuid"));
			}
			if (jObject.has("major")&&jObject.getString("major")!=null&&!jObject.getString("major").equals("")) {
				tempObj.put("major", jObject.getString("major"));
			}
			if (jObject.has("minor")&&jObject.getString("minor")!=null&&!jObject.getString("minor").equals("")) {
				tempObj.put("minor", jObject.getString("minor"));
			}
			if (jObject.has("title")&&jObject.getString("title")!=null&&!jObject.getString("title").equals("")) {
				tempObj2.put("title", jObject.getString("title"));
			}
			if (jObject.has("name")&&jObject.getString("name")!=null&&!jObject.getString("name").equals("")) {
				tempObj3.put("name", jObject.getString("name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("0:"+tempObj.size()+",1:"+tempObj2.size()+",2:"+tempObj3.size());
		if(tempObj.size()>0)
		{
			System.out.println("from Staff_dev_statistic s where s.uuid='"+tempObj.getString("uuid")+"' and s.major='"+tempObj.getString("major")+"' and s.minor='"+tempObj.getString("minor")+"'");
			staff_dev_statistics=hibernateTemplate.find("from Staff_dev_statistic s where s.uuid='"+tempObj.getString("uuid")+"' and s.major='"+tempObj.getString("major")+"' and s.minor='"+tempObj.getString("minor")+"'");
			if (staff_dev_statistics!=null && staff_dev_statistics.size()>0) {
				for (int i = 0; i < staff_dev_statistics.size(); i++) {
					Staff_dev_statistic finds_d_statistic = staff_dev_statistics.get(i);
					String tempStaffId = finds_d_statistic.getStaff_id();
					if (AreYouMyChildren(tempStaffId,StaffId)) 
					{
						return true;							
					}						
				}				
			}
			
			/*
			String sqlString="from Vdevice v where ";
			Iterator<String>keys=tempObj.keys();
			String key ,value;
			for (int i = 0; i < tempObj.size(); i++) {
				key=keys.next();
				value=tempObj.getString(key);
				System.out.println(key+"-->"+value);
				if(i==tempObj.size()-1)
				{
					sqlString+="v."+key+"='"+value+"' ";
				}
				else {
					sqlString+="v."+key+"='"+value+"' and ";
				}
			}
			System.out.println(sqlString);
			
			vdevices=hibernateTemplate.find(sqlString);
			if(vdevices!=null&&vdevices.size()!=0)
			{
				for(int i=0;i<vdevices.size();i++)
				{
					Vdevice vVdevice = vdevices.get(i);
					String tempVdeviceId = vVdevice.getVdevice_id();
					 vdev_staff_binds=new ArrayList<Vdev_staff_bind>();
					System.out.println("from Vdev_staff_bind sb where sb.vdevice_id = '"+tempVdeviceId+"'");
					vdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind sb where sb.vdevice_id = '"+tempVdeviceId+"'");
					if (vdev_staff_binds!=null&&vdev_staff_binds.size()!=0)
					{
						for (int j = 0; j < vdev_staff_binds.size(); j++) 
						{
							Vdev_staff_bind sbVdev_staff_bind=vdev_staff_binds.get(j);
							String tempStaffId=sbVdev_staff_bind.getStaff_id();
							//if(tempStaffId.equals(StaffId))//字符串比较
							if (AreYouMyChildren(tempStaffId,StaffId)) 
							{
								returnValue=true;		
								return returnValue;							
							}						
						}					
					}
				}
			}	
			*/		
		}
		else if (tempObj2.size()>0) {
			
			String value2=tempObj2.getString("title");
			System.out.println("");
			mes_page_statistics=hibernateTemplate.find("from Mes_page_statistics m where m.title like '%"+value2+"%' or m.name like '%"+value2+"%' ");
			if (mes_page_statistics!=null && mes_page_statistics.size()>0) {
				for (int i = 0; i < mes_page_statistics.size(); i++) {
					Mes_page_statistic findM_p_statistic = mes_page_statistics.get(i);
					String tempStaffId=findM_p_statistic.getStaff_id();
					if (AreYouMyChildren(tempStaffId,StaffId)) 
					{
						
						return true;							
					}	
					
				}
				
			}
			/*
			String sqlString="from Message m where m.title like '%"+value2+"%' or m.name like '%"+value2+"%' ";
			System.out.println(sqlString);
			messages=hibernateTemplate.find(sqlString);
			if (messages!=null&&messages.size()>0) {
				for (int i = 0; i < messages.size(); i++) {
					Message mmMessage =messages.get(i);
					int mMessageId=mmMessage.getId();
					String sqlString2="from Vdev_mes_bind V where V.message_id ='"+String.valueOf(mMessageId)+"'";
					System.out.println(sqlString2);
					vdev_mes_binds = hibernateTemplate.find(sqlString2);
					if(vdev_mes_binds!=null&&vdev_mes_binds.size()>0)
					{
						for (int j = 0; j < vdev_mes_binds.size(); j++) {
							Vdev_mes_bind vvVdev_mes_bind =vdev_mes_binds.get(j);
							String dDeviceId =vvVdev_mes_bind.getVdevice_id();
							String sqlString3="from Vdev_staff_bind Vd where Vd.vdevice_id='"+dDeviceId+"'";
							System.out.println(sqlString3);
							vdev_staff_binds = hibernateTemplate.find(sqlString3);
							if (vdev_staff_binds!=null&&vdev_staff_binds.size()>0) {
								for (int k = 0; k < vdev_staff_binds.size(); k++) {
									Vdev_staff_bind vvVdev_staff_bind =vdev_staff_binds.get(k);
									//if (vvVdev_staff_bind.getStaff_id().equals(StaffId)) 
									if (AreYouMyChildren(vvVdev_staff_bind.getStaff_id(),StaffId)) 
									{
										returnValue=true;
										return returnValue; 
									}									
								}								
							}							
						}
					}					
				}				
			}	*/		
		}
		else if (tempObj3.size()>0) {
			String value3=tempObj3.getString("name");
			String sqlString="from Project p where p.title like '%"+value3+"%'";
			System.out.println(sqlString);
			projects=hibernateTemplate.find(sqlString);
			if (projects!=null&&projects.size()>0) {
				for (int i = 0; i < projects.size(); i++) {
					Project pproject=projects.get(i);
					//if (pproject.getStaff_id().equals(StaffId)) 
					if (AreYouMyChildren(pproject.getStaff_id(),StaffId)) 
					{
						returnValue=true;
						return returnValue; 
					}
				}				
			}
		}
		return returnValue;
	}
	
	//message/device的staff_id是否是StaffId或子代理
	public boolean AreYouMyChildren(String staff_id,String StaffId)
	{
		boolean YouAreMyChildren = false;
		if (StaffId.equalsIgnoreCase("super")) {
			return true;
		}

		if (staff_id.equals(StaffId)) {
			return true;			
		}
		else {
			List<Staff>staffs = new ArrayList<Staff>();
			staffs = hibernateTemplate.find("from Staff s where parent_id ='"+StaffId+"'");
			if (staffs!=null&&staffs.size()>0) {
				for (int i = 0; i < staffs.size(); i++) {
					Staff sStaff = staffs.get(i);
					String sStaff_id=sStaff.getStaff_id();
					YouAreMyChildren=AreYouMyChildren(staff_id, sStaff_id);					
				}				
			}
			return YouAreMyChildren;
		}
		
		
		
	}

	public JSONArray findStatisticAll(String staff_id,String style) {
		System.out.println("******findStatisticAll*******");
		/*
		 * style=0,1,2    
		 * 设备，页面，项目
		 * */
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Date d=new Date(System.currentTimeMillis()-1000*60*60*24);
		 SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
		String ZUOTIAN=sp.format(d);//获取昨天日期
		Date d2=new Date(System.currentTimeMillis()-1000*60*60*24*2);//2
		String QIANTIAN=sp.format(d2);//获取前天日期
		System.out.println("昨天"+ZUOTIAN);
		//System.out.println("and ftime >='"+ZUOTIAN+" 00:00:00' and ftime <='"+ZUOTIAN+" 23:59:59'");
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();

		List<Staff_dev_statistic>staff_dev_statistics = new ArrayList<Staff_dev_statistic>();
		List<Mes_page_statistic>mes_page_statistics = new ArrayList<Mes_page_statistic>();
		
		if(style.equals("0"))
		{
			System.out.println("0，设备！");
			//设备默认按照项目返回
			
				System.out.println("from Staff_dev_statistic s where s.staff_id='"+ staff_id +"' and s.ftime >='"+QIANTIAN+" 00:00:00' and s.ftime <='"+ZUOTIAN+" 23:59:59'");
				
				List<Object[]>objects=new ArrayList<Object[]>();
				objects = hibernateTemplate.find("select distinct minor,major,uuid,shake_uv,shake_pv,click_uv,click_pv,ftime from Staff_dev_statistic s where s.staff_id='"+ staff_id +"' and s.ftime >='"+QIANTIAN+" 00:00:00' and s.ftime <='"+ZUOTIAN+" 23:59:59'");
				//staff_dev_statistics= hibernateTemplate.find("from Staff_dev_statistic s where s.staff_id='"+ staff_id +"' and s.ftime >='"+QIANTIAN+" 00:00:00' and s.ftime <='"+ZUOTIAN+" 23:59:59'");// 
				if (objects!=null && objects.size()>0) {
					for (int i = 0; i < objects.size(); i++) {
						//Staff_dev_statistic finds_d_statistic = staff_dev_statistics.get(i);
						Object[] findObject = objects.get(i);
						jsonObject.put("uuid", findObject[2].toString());
						jsonObject.put("major", findObject[1].toString() );
						jsonObject.put("minor", findObject[0].toString() );
						jsonObject.put("shake_uv", findObject[3].toString());
						jsonObject.put("shake_pv", findObject[4].toString());
						jsonObject.put("click_uv", findObject[5].toString());
						jsonObject.put("click_pv", findObject[6].toString());
						jsonObject.put("ftime", findObject[7].toString());	
						jsonArray.add(jsonObject);		
					}
					
				}

		}
		else if (style.equals("1")) {
			System.out.println("1，页面！");
			//页面，项目按页面返回
			
				System.out.println("from Mes_page_statistic m where m.staff_id='"+ staff_id +"' and m.ftime >='"+QIANTIAN+" 00:00:00' and m.ftime <='"+ZUOTIAN+" 23:59:59' group by page_id");
				List<Object[]>objects=new ArrayList<Object[]>();
				objects= hibernateTemplate.find("select distinct page_id, mes_title,mes_name,mes_content,staff_id,shake_uv,shake_pv,click_uv,click_pv,ftime from Mes_page_statistic m where m.staff_id='"+ staff_id +"' and m.ftime >='"+QIANTIAN+" 00:00:00' and m.ftime <='"+ZUOTIAN+" 23:59:59' ");
				if (objects!=null && objects.size()>0) {
					for (int i = 0; i < objects.size(); i++) {
						Object[] findMpStatistic= objects.get(i);
						//Mes_page_statistic findMpStatistic = mes_page_statistics.get(i);
						jsonObject.put("page_id",findMpStatistic[0].toString() );
						jsonObject.put("title",findMpStatistic[1].toString() );
						jsonObject.put("name", findMpStatistic[2].toString());
						jsonObject.put("content",findMpStatistic[3].toString());
						jsonObject.put("staff_id", findMpStatistic[4].toString());
						jsonObject.put("shake_uv", findMpStatistic[5].toString());
						jsonObject.put("shake_pv", findMpStatistic[6].toString());
						jsonObject.put("click_uv", findMpStatistic[7].toString());
						jsonObject.put("click_pv", findMpStatistic[8].toString());
						jsonObject.put("ftime", findMpStatistic[9].toString());	
						jsonArray.add(jsonObject);		
						
					}
				}

			
			
			
			
		}
		else if (style.equals("2")) {
			System.out.println("2，项目！");
			//String sqlString="SELECT tmpM.project_id,tmpM.pro_title,SUM(tmpM.shake_pv),SUM(tmpM.shake_uv),SUM(tmpM.click_pv),SUM(tmpM.click_uv),tmpM.ftime " +
			//		"from ( SELECT DISTINCT page_id,project_id,shake_pv,shake_uv,click_pv,click_uv,pro_title,ftime FROM  Mes_page_statistic m" +
			//		"where m.staff_id='"+staff_id+"' and m.ftime >='"+QIANTIAN+" 00:00:00' and m.ftime <='"+ZUOTIAN+" 23:59:59' ) AS tmpM" +
			//		" GROUP BY tmpM.project_id,tmpM.ftime";
			String sqlString="SELECT DISTINCT page_id,project_id,pro_title,shake_pv,shake_uv,click_pv,click_uv,ftime FROM  Mes_page_statistic m where m.staff_id='"+staff_id+"' and m.ftime >='"+QIANTIAN+" 00:00:00' and m.ftime <='"+ZUOTIAN+" 23:59:59' order by project_id,ftime";
			System.out.println(sqlString);
			//System.out.println("SELECT project_id,SUM(shake_pv),SUM(shake_uv),SUM(click_pv),SUM(click_uv),pro_title,ftime FROM Mes_page_statistic m where m.staff_id='"+staff_id+"' and m.ftime >='"+QIANTIAN+" 00:00:00' and m.ftime <='"+ZUOTIAN+" 23:59:59' GROUP BY m.ftime,m.project_id,m.staff_id");
			List<Object[]>jsonObjects = new ArrayList<Object[]>();
			jsonObjects = hibernateTemplate.find(sqlString);
			//jsonObjects=hibernateTemplate.find("SELECT project_id,SUM(shake_pv),SUM(shake_uv),SUM(click_pv),SUM(click_uv),pro_title,ftime FROM Mes_page_statistic  where staff_id='"+staff_id+"' and ftime >='"+QIANTIAN+" 00:00:00' and ftime <='"+ZUOTIAN+" 23:59:59' GROUP BY ftime,project_id");
			//System.out.println(jsonObjects.size());
			int sum_sp=0,sum_su=0,sum_cp=0,sum_cu=0;
			String pro_id_old="",pro_id_new="",pro_title="",f_time_old="",f_time_new="";
			
			if (jsonObjects!=null && jsonObjects.size()>0) {
				for (int i = 0; i < jsonObjects.size(); i++) {
					Object[] findObject =jsonObjects.get(i);
					if (i==0) {
						pro_id_old=findObject[1].toString();
						pro_title=findObject[2].toString();
						sum_sp=Integer.valueOf(findObject[3].toString());
						sum_su=Integer.valueOf(findObject[4].toString());
						sum_cp=Integer.valueOf(findObject[5].toString());
						sum_cu=Integer.valueOf(findObject[6].toString());
						f_time_old=findObject[7].toString();
						continue;
					}
					pro_id_new=findObject[1].toString();
					f_time_new=findObject[7].toString();
					if (pro_id_new.equals(pro_id_old)) {
						if (f_time_new.equals(f_time_old)) {
							sum_sp+=Integer.valueOf(findObject[3].toString());
							sum_su+=Integer.valueOf(findObject[4].toString());
							sum_cp+=Integer.valueOf(findObject[5].toString());
							sum_cu+=Integer.valueOf(findObject[6].toString());
						}
						else {
							//jsonObject.put("page_id", pro_id_old );
							jsonObject.put("project_id", pro_id_old );
							jsonObject.put("title",pro_title );
							jsonObject.put("shake_pv", sum_sp );
							jsonObject.put("shake_uv", sum_su );
							jsonObject.put("click_pv", sum_cp );
							jsonObject.put("click_uv", sum_cu  );
							jsonObject.put("ftime", f_time_old );
							jsonArray.add(jsonObject);	
							
							sum_sp=Integer.valueOf(findObject[3].toString());
							sum_su=Integer.valueOf(findObject[4].toString());
							sum_cp=Integer.valueOf(findObject[5].toString());
							sum_cu=Integer.valueOf(findObject[6].toString());
							f_time_old=f_time_new;
							
						}
						
						
					}
					else {
						
						jsonObject.put("project_id", pro_id_old );
						jsonObject.put("title",pro_title );
						jsonObject.put("shake_pv", sum_sp );
						jsonObject.put("shake_uv", sum_su );
						jsonObject.put("click_pv", sum_cp );
						jsonObject.put("click_uv", sum_cu  );
						jsonObject.put("ftime", f_time_old );
						jsonArray.add(jsonObject);	
						
						pro_id_old=pro_id_new;
						pro_title=findObject[2].toString();
						sum_sp=Integer.valueOf(findObject[3].toString());
						sum_su=Integer.valueOf(findObject[4].toString());
						sum_cp=Integer.valueOf(findObject[5].toString());
						sum_cu=Integer.valueOf(findObject[6].toString());
						f_time_old=findObject[7].toString();
					}
					
				}
				jsonObject.put("project_id", pro_id_old );
				jsonObject.put("title",pro_title );
				jsonObject.put("shake_pv", sum_sp );
				jsonObject.put("shake_uv", sum_su );
				jsonObject.put("click_pv", sum_cp );
				jsonObject.put("click_uv", sum_cu  );
				jsonObject.put("ftime", f_time_old );
				jsonArray.add(jsonObject);	
				
			}

			
		}
		
		return jsonArray;
	}
	
//	public boolean uploadExl()
//	{
//		
//		
//		return true;
//	}
	
	public boolean UploadWangfeng(String staff_id)
	{
		//vdev_mes_bind
		if (!staff_id.equalsIgnoreCase("wangfeng")) {
			return false;
		}
		String uuidString="FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
		String majorsString="10001";
		String mes_id="3905";
		int minorStart = 21390;
		int vdeviceStart = 18466;
		int minorSize = 50;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Vdev_mes_bind>vdev_mes_binds = new ArrayList<Vdev_mes_bind>();
		for (int i = 0; i < minorSize; i++) {
			vdev_mes_binds= hibernateTemplate.find("from Vdev_mes_bind where vdevice_id='"+String.valueOf(vdeviceStart+i)+"'");
			if (vdev_mes_binds==null && vdev_mes_binds.size()==0) {
				Vdev_mes_bind tempvBind = new Vdev_mes_bind();
				tempvBind.setMessage_id(mes_id);
				tempvBind.setVdevice_id(String.valueOf(vdeviceStart+i));
				tempvBind.setUuid(uuidString);
				tempvBind.setMajor(majorsString);
				tempvBind.setMinor(String.valueOf(minorStart+i));
				tempvBind.setTime(time);
				hibernateTemplate.save(tempvBind);
			}
			
		}
		
		return true;
	}
	
	/*
	public boolean UploadChengdu(String staff_id)
	{
		if (!staff_id.equalsIgnoreCase("chengdu")) {
			return false;
		}
		String uuidString="FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
		String majorsString="10011";
		int minorStart=23357; //String minorStart="23357";
		int minorSize=10000;
		int deviceStart=962937;
		List<Vdevice>findvDevices = new ArrayList<Vdevice>();
		List<Vdev_staff_bind>findVdev_staff_binds = new ArrayList<Vdev_staff_bind>();
		
		//先捆绑vdevice_info
		for (int i = 0; i < minorSize ; i++) {
			findvDevices = hibernateTemplate.find("from Vdevice v where v.vdevice_id='"+String.valueOf((deviceStart+i))+"'");
			if (findvDevices==null||findvDevices.size()==0) {
				Vdevice tempDevice = new Vdevice();
				//vdev_id--uuid--major--minor--type==time
				tempDevice.setVdevice_id(String.valueOf((deviceStart+i)));
				tempDevice.setUuid(uuidString);
				tempDevice.setMajor(majorsString);
				tempDevice.setMinor(String.valueOf(minorStart+i));
				tempDevice.setType("1");
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				tempDevice.setTime(time);			
				
				hibernateTemplate.save(tempDevice);	
			}	
		}
		
		//再绑定vdev_staff_bind
		
		for ( int j = 0; j < minorSize; j++) {
			//绑定成都，成都也分着写吧
			Vdev_staff_bind vdev_staff_bind1 = new Vdev_staff_bind();
			//绑定子代理
			Vdev_staff_bind vdev_staff_bind2 = new Vdev_staff_bind();
			//vdevice_id--status--staff_id--time
			if (j<4000) {
				//i4 白
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='chengdu'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0) {
					vdev_staff_bind1.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind1.setStatus("1");
					vdev_staff_bind1.setStaff_id("chengdu");
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					vdev_staff_bind1.setTime(time);
					hibernateTemplate.save(vdev_staff_bind1);					
				}
				
				//sdsc
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='sdsc'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("sdsc");
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				
				//wfme
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='wfme'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("wfme");
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				
				//whxnf
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='whxnf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("whxnf");
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
			}
			else if (j<7000) {
				//i3 白 重庆兰鹤  重庆维丰  山东维丰

				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='chengdu'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0) {
					vdev_staff_bind1.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind1.setStatus("1");
					vdev_staff_bind1.setStaff_id("chengdu");
					vdev_staff_bind1.setTime(time);
					hibernateTemplate.save(vdev_staff_bind1);					
				}
				
				//		        cqlh        cqwf     sdwf
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='cqlh'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("cqlh");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
			
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='cqwf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("cqwf");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='sdwf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("sdwf");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
			}
			else if (j<10000) {
				//i3 黑   广元良品    河南维丰  湖南维丰   四川维丰(乐山) 
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='chengdu'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0) {
					vdev_staff_bind1.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind1.setStatus("1");
					vdev_staff_bind1.setStaff_id("chengdu");					
					vdev_staff_bind1.setTime(time);
					hibernateTemplate.save(vdev_staff_bind1);					
				}
				
				//		        gylp        hnwf     hunanwf      lswf
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='gylp'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("gylp");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='hnwf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("hnwf");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='hunanwf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("hunanwf");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}
				
				findVdev_staff_binds=hibernateTemplate.find("from Vdev_staff_bind v where v.vdevice_id='"+String.valueOf(deviceStart+j)+"' and v.staff_id='lswf'");
				if (findVdev_staff_binds==null||findVdev_staff_binds.size()==0)
				{
					vdev_staff_bind2.setVdevice_id(String.valueOf(deviceStart+j));
					vdev_staff_bind2.setStatus("0");
					vdev_staff_bind2.setStaff_id("lswf");
					vdev_staff_bind2.setTime(time);
					hibernateTemplate.save(vdev_staff_bind2);
				}	
				
			}
			else {
				
			}			
		}
		
		
		return true; 
	}

*/
	
	
}
