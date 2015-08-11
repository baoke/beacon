package com.buptmap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDevStaffBind implements Runnable {

	/**
	 * @param args
	 */
	private String url;
	private String user;
	private String pwd;
	private String staff_id;
	private String parent_id;
	private String uuid;
	private String major;
	private int start;
	private int end;
	
	public AddDevStaffBind(String url,String user, String pwd, String parent_id, String staff_id, String uuid, String major, int start, int end){
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.parent_id = parent_id;
		this.staff_id = staff_id;
		this.uuid = uuid;
		this.major = major;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void run(){
		Connection conn = null;
		PreparedStatement pst = null;
		PreparedStatement updatepst = null;
		Statement st = null;
		ResultSet rsResultSet = null;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			pst = conn.prepareStatement("insert into vdev_staff_bind(uuid,major,minor,status,staff_id,time) value(?,?,?,?,?,?)");
			updatepst = conn.prepareStatement("update Vdev_staff_bind set status=? where uuid=? and major=? and minor=? and staff_id = '" + parent_id + "'");
			st = conn.createStatement();
			System.out.println("insert into vdev_staff_bind(uuid,major,minor,status,staff_id,time) value(?,?,?,?,?,?)");
			for (int l = start; l <= end; l++) {
				rsResultSet = st.executeQuery("select status from Vdev_staff_bind where uuid='" + uuid + "'and major='" + major + "' and minor='" +String.valueOf(l)+"' and staff_id = '" + parent_id + "'");
				if (rsResultSet.next()) {
					String total = String.valueOf(Integer.valueOf(rsResultSet.getString("status")) + 1);//String total = String.valueOf(Integer.valueOf(rsResultSet.getString("status")) + 1);
					updatepst.setString(1,total);
					updatepst.setString(2,uuid);
					updatepst.setString(3,major);
					updatepst.setString(4,String.valueOf(l));
					updatepst.addBatch();
					
					pst.setString(1, uuid);
					pst.setString(2, major);
					pst.setString(3, String.valueOf(l));
					pst.setString(4, "0");
					pst.setString(5, staff_id);
					pst.setString(6, time);
					pst.addBatch();										
					
				}
				else {
					pst.setString(1, uuid);
					pst.setString(2, major);
					pst.setString(3, String.valueOf(l));
					pst.setString(4, "0");
					pst.setString(5, staff_id);
					pst.setString(6, time);
					pst.addBatch();
					pst.setString(1, uuid);
					pst.setString(2, major);
					pst.setString(3, String.valueOf(l));
					pst.setString(4, "1");
					pst.setString(5, parent_id);
					pst.setString(6, time);
					pst.addBatch();
													
				}
			}
			updatepst.executeBatch();
			pst.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		finally{
			try{
				if (pst != null) {
					pst.close();
				}
				if (updatepst != null) {
					updatepst.close();
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
