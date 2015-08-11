package com.buptmap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * By Lynn 
 * */

public class EditDevStaffBind implements Runnable {
	
	private String url;
	private String user;
	private String pwd;
	private String staff_id;
	private String parent_id;
	private JSONArray jsonArray;
	//private String uuid;
	//private String major;
	//private String minor;
	private int start;
	private int end;
	private boolean deladd;  //del-->0;add-->1;
	
	public EditDevStaffBind(String url,String user, String pwd, String parent_id, String staff_id, JSONArray jsonArray,int start,int end, boolean deladd){
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.parent_id = parent_id;
		this.staff_id = staff_id;
		this.jsonArray = jsonArray;
		//this.uuid = uuid;
		//this.major = major;
		//this.minor = minor;
		this.start = start;
		this.end = end;
		this.deladd = deladd;

	}
	
	
	
	
	@Override
	public void run(){
		
		Connection conn = null;
		PreparedStatement pst = null;
		PreparedStatement updatepst = null;
		PreparedStatement deletepst =null;
		Statement st = null;
		ResultSet rsResultSet = null;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("线程号："+Thread.currentThread().getName());
		
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			pst = conn.prepareStatement("insert into vdev_staff_bind(uuid,major,minor,status,staff_id,time) value(?,?,?,?,?,?)");
			updatepst = conn.prepareStatement("update Vdev_staff_bind set status=? where uuid=?  and major=? and minor=? and staff_id = '" + parent_id + "'");
			deletepst = conn.prepareStatement("delete from Vdev_staff_bind where uuid=? and major=? and minor=? and staff_id ='"+ staff_id +"'");
			st = conn.createStatement();
			if (deladd) {
				//edit Add sessions
				for (int i = start; i <=end ; i++) {
					
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String Vuuid = jsonObject.getString("uuid");
					String Vmajor = jsonObject.getString("major");
					String Vminor = jsonObject.getString("minor");
					//System.out.println(Vminor);
					
					rsResultSet = st.executeQuery("select status from Vdev_staff_bind where uuid='" + Vuuid + "'and major='" + Vmajor + "' and minor='" +Vminor+"' and staff_id = '" + parent_id + "'");
					if (rsResultSet.next()) {
						String statusp=String.valueOf(Integer.valueOf( rsResultSet.getString("status")) + 1);
						updatepst.setString(1,statusp);
						updatepst.setString(2,Vuuid);
						updatepst.setString(3,Vmajor);
						updatepst.setString(4,Vminor);
						updatepst.addBatch();
						
						pst.setString(1, Vuuid);
						pst.setString(2, Vmajor);
						pst.setString(3, Vminor);
						pst.setString(4, "0");
						pst.setString(5, staff_id);
						pst.setString(6, time);
						pst.addBatch();
					}
					else {
						//分配给第一个子代理
						pst.setString(1, Vuuid);
						pst.setString(2, Vmajor);
						pst.setString(3, Vminor);
						pst.setString(4, "0");
						pst.setString(5, staff_id);
						pst.setString(6, time);
						pst.addBatch();
						pst.setString(1, Vuuid);
						pst.setString(2, Vmajor);
						pst.setString(3, Vminor);
						pst.setString(4, "1");
						pst.setString(5, parent_id);
						pst.setString(6, time);
						pst.addBatch();
						
					}
					
				}
				updatepst.executeBatch();
				pst.executeBatch();
			} 
			else {
				//edit Del sessions
				for (int i = start; i <=end ; i++) {
					
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String Vuuid = jsonObject.getString("uuid");
					String Vmajor = jsonObject.getString("major");
					String Vminor = jsonObject.getString("minor");
					//System.out.println(Vminor);
					rsResultSet = st.executeQuery("select status from Vdev_staff_bind where uuid='" + Vuuid + "'and major='" + Vmajor + "' and minor='" +Vminor+"' and staff_id = '" + staff_id + "'");
					if (!rsResultSet.next()) {
						continue;
					}
					
					//其实删除并不需要select，数据库中一定会有这条数据的~
					rsResultSet = st.executeQuery("select status from Vdev_staff_bind where uuid='" + Vuuid + "'and major='" + Vmajor + "' and minor='" +Vminor+"' and staff_id = '" + parent_id + "'");
					if (rsResultSet.next()) {
						
						String statusp=String.valueOf(Integer.valueOf( rsResultSet.getString("status")) - 1);
						
						updatepst.setString(1,statusp);
						updatepst.setString(2,Vuuid);
						updatepst.setString(3,Vmajor);
						updatepst.setString(4,Vminor);
						updatepst.addBatch();
						
						deletepst.setString(1, Vuuid);
						deletepst.setString(2, Vmajor);
						deletepst.setString(3, Vminor);
						deletepst.addBatch();
						
					}
					else {
						updatepst.setString(1,"0");
						updatepst.setString(2,Vuuid);
						updatepst.setString(3,Vmajor);
						updatepst.setString(4,Vminor);
						updatepst.addBatch();
						
						deletepst.setString(1, Vuuid);
						deletepst.setString(2, Vmajor);
						deletepst.setString(3, Vminor);
						deletepst.addBatch();
						
					}
					
					
				}
				deletepst.executeBatch();
				updatepst.executeBatch();				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			try{
				if (pst != null) {
					pst.close();
				}
				if (updatepst != null) {
					updatepst.close();
				}
				if (deletepst!=null) {
					deletepst.close();
				}
				if (rsResultSet != null) {
					rsResultSet.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			}catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		
		
	}

}
