package com.buptmap.util;

import net.sf.json.JSONArray;

public class AddSession implements Runnable{
	private String url;
	private String user;
	private String pwd;
	private JSONArray resultArray;
	
	public AddSession(String url,String user, String pwd, JSONArray resultArray){
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.resultArray = resultArray;
	}
	
	@Override
	public void run(){/*
		Connection conn = null;
		PreparedStatement pst = null;
		PreparedStatement updatepst = null;
		Statement st = null;
		ResultSet rsResultSet = null;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			conn.setAutoCommit(false);
		
			pst = conn.prepareStatement("insert into vdev_staff_bind(uuid,major,minor,status,staff_id,time) value(?,?,?,?,?,?)");
			st = conn.createStatement();
			System.out.println("insert into vdev_staff_bind(uuid,major,minor,status,staff_id,time) value(?,?,?,?,?,?)");
			for (int l = 0; l <= resultArray.size(); l++) {
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
	        conn.commit();
		
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
		
	*/}
}
