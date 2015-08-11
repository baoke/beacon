package com.buptmap.action;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;  
import com.buptmap.Service.BeaconService;
import com.buptmap.Service.PatrolService;
import com.buptmap.Service.StaffService;
import com.buptmap.model.Testbeacon;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;  
import com.opensymphony.xwork2.ActionSupport; 

public class AndroidAction extends ActionSupport {
	
	private File image;  
    // 上传文件类型  
    private String imageContentType;  
    // 封装上传文件名  
    private String imageFileName;  
    // 接受依赖注入的属性  
    private String savePath; 
    private JSONObject resultObj;
	private BeaconService beaconService;
	private PatrolService patrolService;
	private StaffService staffService;
	private String id;
	private String pwd;
	private String jsonstr;
	private String user_id;
	private String key;
	
	public String login() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			System.out.println("111");
			resultObj = staffService.login(id, pwd);
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
			
		}
	}
	
	//上传照片
	public String pic_upload() throws Exception {  
    	JSONObject testObject = new JSONObject();
    	JSONArray testArray = new JSONArray();
    	String staff_id = null;
    	Map<String,Object> map = new HashMap<String,Object>();
        HttpServletRequest request=ServletActionContext.getRequest();  
        FileOutputStream fos = null;  
        FileInputStream fis = null;  
        String saveName = null;
        try {  
        	
            System.out.println("获取Android端传过来的普通信息：");  
            System.out.println("文件名："+request.getParameter("fileName"));  
            System.out.println("获取Android端传过来的文件信息：");  
            saveName = request.getParameter("fileName").replace(':', '_');
            
            File firstUsed = new File("c:/upload/" + saveName); 
            if (! firstUsed.exists()) {
            	//不存在时再上传
            	 fos = new FileOutputStream("c:/upload/" + saveName); 
            	 fis = new FileInputStream(getImage());  
                 byte[] buffer = new byte[1024];  
                 int len = 0;  
                 while ((len = fis.read(buffer)) != -1) {  
                     fos.write(buffer, 0, len);  
                 }  
                 close(fos, fis);  
                 System.out.println("文件上传成功");  
			}
        	map.put("success", true);	
         	map.put("message", "上传成功");	
 			resultObj = JSONObject.fromObject(map);
			

        } catch (Exception e) {  
            
            map.put("success", false);	
			resultObj = JSONObject.fromObject(map);
            e.printStackTrace();  
        } finally {  
            close(fos, fis);  
        }  
        return SUCCESS;  
    }  

	//上传巡检记录的db文件
	public String upload() throws Exception {  
	    	JSONObject testObject = new JSONObject();
	    	JSONArray testArray = new JSONArray();
	    	String staff_id = null;
	    	Map<String,Object> map = new HashMap<String,Object>();
	        HttpServletRequest request=ServletActionContext.getRequest();  
	        FileOutputStream fos = null;  
	        FileInputStream fis = null;  
	        String saveName = null;
	        try {  
	        	
	            System.out.println("获取Android端传过来的普通信息：");  
	            //System.out.println("用户名："+request.getParameter("username"));  
	            //System.out.println("密码："+request.getParameter("pwd"));  
	            //System.out.println("年龄："+request.getParameter("age"));  
	            System.out.println("文件名："+request.getParameter("fileName"));  
	            System.out.println("获取Android端传过来的文件信息：");  
	            saveName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +request.getParameter("id");
				
	            fos = new FileOutputStream("c:/upload/" + saveName);  
	            fis = new FileInputStream(getImage());  
	            staff_id = request.getParameter("id");
	            byte[] buffer = new byte[1024];  
	            int len = 0;  
	            while ((len = fis.read(buffer)) != -1) {  
	                fos.write(buffer, 0, len);  
	            }  
	            close(fos, fis);  
	            System.out.println("文件上传成功");  
	            String testString = request.getParameter("fileName");
	            if (testString.endsWith(".db")) {
	            	 Class.forName("org.sqlite.JDBC");
	                 Connection connTest = DriverManager.getConnection("jdbc:sqlite://c:/upload/" + saveName);
	                 Statement stat = connTest.createStatement();

	                 ResultSet rs = stat.executeQuery("select * from patrol_info;");
	                 int i = rs.getRow();
	                 System.out.println(i);
	                 while (rs.next())
	                 {
	                	 testObject.put("mac", rs.getString("mac_id"));
	                	 testObject.put("uuid", rs.getString("uuid"));
	                	 testObject.put("staff_id", rs.getString("staff_id"));
	                	 testObject.put("major", rs.getString("major"));
	                	 testObject.put("minor", rs.getString("minor"));
	                	 testObject.put("time", rs.getString("time"));
	                	 testObject.put("serial_num", rs.getString("serial_num"));
	                	 testObject.put("rssi", rs.getString("rssi"));

	                	testArray.add(testObject);
	                 }
	                 rs.close();
	                 System.out.println("读取成功"); 
	                 connTest.close();
	                 if (patrolService.upload(testArray)) {
	                 	map.put("success", true);	
	                 	map.put("message", "db上传并更新成功");	
	         			resultObj = JSONObject.fromObject(map);
	     			}
	                 else {
	                	 System.out.println("db上传更新失败");  
	                 	map.put("success", false);	
	                 	map.put("message", "db上传更新失败");	
	         			resultObj = JSONObject.fromObject(map);
	     			}
	                 
				}
				

	        } catch (Exception e) {  
	            
	            map.put("success", false);	
				resultObj = JSONObject.fromObject(map);
	            e.printStackTrace();  
	        } finally {  
	            close(fos, fis);  
	        }  
	        return SUCCESS;  
	    }  

    //@Override  
	//上传beacon部署的db文件
    public String uploadtest() throws Exception {  
    	List<Testbeacon> testList = new ArrayList<Testbeacon>();
    	String staff_id = null;
    	Map<String,Object> map = new HashMap<String,Object>();
        HttpServletRequest request=ServletActionContext.getRequest();  
        FileOutputStream fos = null;  
        FileInputStream fis = null;  
        String saveName = null;
        
        if (request.getParameter("id") == null || request.getParameter("tempid") == null) {
    		map.put("success", false);
			map.put("message", "参数为空");
			resultObj = JSONObject.fromObject(map);
        	return SUCCESS;
		}
    	if( !staffService.verify(request.getParameter("id"), request.getParameter("tempid"))){
        	map.put("success", false);
			map.put("message", "tempid_time_out");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
        }
        try {  
        	
        	
        	saveName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "_"+request.getParameter("id") + ".db";
        	System.out.println(saveName);
            fos = new FileOutputStream("c:/upload/" + saveName);  
            fis = new FileInputStream(getImage());  
            staff_id = request.getParameter("id");
            System.out.println(staff_id);
            byte[] buffer = new byte[1024];  
            int len = 0;  
            while ((len = fis.read(buffer)) != -1) {  
                fos.write(buffer, 0, len);  
            }  
            close(fos, fis);  
            System.out.println("文件上传成功");  
            String testString = request.getParameter("fileName");
            if (testString.endsWith(".db")) {
            	 Class.forName("org.sqlite.JDBC");
                 Connection connTest = DriverManager.getConnection("jdbc:sqlite://c:/upload/" + saveName);
                 Statement stat = connTest.createStatement();

                 ResultSet rs = stat.executeQuery("select * from beacon;");
                 int i = rs.getRow();
                 while (rs.next())
                 {
                	Testbeacon testBeacon = new Testbeacon();
                 	testBeacon.setMac_id(rs.getString("mac_id"));
                 	testBeacon.setUuid(rs.getString("uuid"));
                 	testBeacon.setType(rs.getString("type"));
     				testBeacon.setMajor(rs.getString("major"));
     				testBeacon.setMinor(rs.getString("minor"));
     				testBeacon.setBuilding(rs.getString("building"));
     				testBeacon.setFloor(rs.getString("floor"));
     				testBeacon.setCoord_x(rs.getString("coord_x"));
     				testBeacon.setCoord_y(rs.getString("coord_y"));
     				testBeacon.setAddress(rs.getString("address"));
     			    testBeacon.setCoverage(rs.getString("coverage"));
     				testBeacon.setPower(rs.getString("power"));
     				testBeacon.setFrequency(rs.getString("frequency"));
     				testBeacon.setTemperaturefrequency(rs.getString("temperaturefrequency"));
     				testBeacon.setLightfrequency(rs.getString("lightfrequency"));
     				testBeacon.setFirm(rs.getString("firm"));
     				testBeacon.setLast_modify_id(staff_id);
     				testBeacon.setCreate_time(rs.getString("create_time"));
     				testBeacon.setLast_modify_time(rs.getString("last_modify_time"));
     				testBeacon.setAccelerate(rs.getString("accelerate"));
     				testBeacon.setStatus(rs.getString("status"));
     				testBeacon.setCreate_id(rs.getString("create_id"));
     				testBeacon.setAddress_type(rs.getString("address_type"));

     				testList.add(testBeacon);
                 }
                 rs.close();
                 System.out.println("读取成功"); 
                 connTest.close();
                 if (beaconService.update(testList)) {
                 	map.put("success", true);	
                 	map.put("message", "db上传并更新成功");	
         			resultObj = JSONObject.fromObject(map);
     			}
                 else {
                	 
                 	map.put("success", false);	
                 	map.put("message", "db上传更新失败");	
         			resultObj = JSONObject.fromObject(map);
     			}
                 
			}
            else {
            	map.put("success", false);	
             	map.put("message", "请上传.db文件");	
     			resultObj = JSONObject.fromObject(map);
			}
			

            
            return SUCCESS;
        } catch (Exception e) {  
            
            map.put("success", false);	
            map.put("message", "未知错误");
			resultObj = JSONObject.fromObject(map);
            e.printStackTrace();  
            return SUCCESS;
        } finally {  
            close(fos, fis);  
        }  
       
    }  
  
    //实时上传接口
    public String report(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Testbeacon> testList = new ArrayList<Testbeacon>();
    	String staff_id = null;
        HttpServletRequest request=ServletActionContext.getRequest();  
		try {
			System.out.println(jsonstr + "test111111111");
			if (jsonstr == null || jsonstr == "") {
				map.put("success", false);
				map.put("message", "没有数据");
				resultObj = JSONObject.fromObject(map);
				return "success";
			}
			
			JSONObject object = JSONObject.fromObject(jsonstr);
			
			String idString = object.getString("user_id");
			
			if (object.getString("user_id") == null || object.getString("key") == null) {
	    		map.put("success", false);
				map.put("message", "id or key为空");
				resultObj = JSONObject.fromObject(map);
	        	return SUCCESS;
			}
	    	if( !staffService.verify(object.getString("user_id"),  object.getString("key"))){
	        	map.put("success", false);
				map.put("message", "tempid_time_out");
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
	        }
	    	/*需要将testarray数组转化为List<Testbeacon> testList = new ArrayList<Testbeacon>()
	    	 * 调用beaconService.update(testList）接口即可
	    	 * 
	    	 * 
	    	 * */
	    	JSONArray testArray = JSONArray.fromObject(object.getString("data"));

	    	for (int i = 0; i < testArray.size(); i++) {
				JSONObject rs = testArray.getJSONObject(i);
				Testbeacon testBeacon = new Testbeacon();
				
             	testBeacon.setMac_id(rs.getString("mac_id"));
             	testBeacon.setUuid(rs.getString("uuid"));
 				testBeacon.setMajor(rs.getString("major"));
 				testBeacon.setMinor(rs.getString("minor"));
 				testBeacon.setBuilding(rs.getString("building"));
 				testBeacon.setFloor(rs.getString("floor"));
 				testBeacon.setCoord_x(rs.getString("coord_x"));
 				testBeacon.setCoord_y(rs.getString("coord_y"));
 			    testBeacon.setCoverage(rs.getString("coverage"));
 				testBeacon.setPower(rs.getString("power"));
 				testBeacon.setFrequency(rs.getString("frequency"));
 				testBeacon.setTemperaturefrequency(rs.getString("temperaturefrequency"));
 				testBeacon.setLightfrequency(rs.getString("lightfrequency"));
 				testBeacon.setType(rs.getString("type"));
 				testBeacon.setFirm(rs.getString("firm"));		
 				testBeacon.setCreate_time(rs.getString("create_time"));
 				testBeacon.setLast_modify_time(rs.getString("last_modify_time"));
 				testBeacon.setAccelerate(rs.getString("accelerate"));
 				testBeacon.setAddress(rs.getString("address"));
 				
 				testBeacon.setStatus(rs.getString("status"));
 				
 				//testBeacon.setAddress_type(rs.getString("address_type"));
 				testBeacon.setLast_modify_id(idString);

 				testList.add(testBeacon);
			}

	    	if (beaconService.update(testList)) {
              	map.put("success", true);	
              	map.put("message", "上传成功");	
      			resultObj = JSONObject.fromObject(map);
      			return SUCCESS;
  			}
            else {
              	map.put("success", false);	
              	map.put("message", "上传失败");	
      			resultObj = JSONObject.fromObject(map);
      			return SUCCESS;
  			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "数据格式错误");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			
		}
		
	}

    public String test(){
		Map<String, Object> map = new HashMap<String, Object>();
    	JSONArray resultArray = new JSONArray();
		try {	
			if (user_id == null || key == null) {
	    		map.put("success", false);
				map.put("message", "id or key为空");
				resultObj = JSONObject.fromObject(map);
	        	return SUCCESS;
			}
	    	if( !staffService.verify(user_id, key)){
	        	map.put("success", false);
				map.put("message", "tempid_time_out");
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
	        }
	    	
	    	resultArray = beaconService.findSession(user_id);
	    	
	    	if (resultArray != null && resultArray.size() != 0) {
				map.put("success", true);
				map.put("role", resultArray);
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
		}
	}

  
    
    /** 
     * 文件存放目录 
     *  
     * @return 
     */  
    public String getSavePath() throws Exception{  
        return ServletActionContext.getServletContext().getRealPath(savePath);   
    }  
  
    public void setSavePath(String savePath) {  
        this.savePath = savePath;  
    }  
  
    public File getImage() {  
        return image;  
    }  
  
    public void setImage(File image) {  
        this.image = image;  
    }  
  
    public String getImageContentType() {  
        return imageContentType;  
    }  
  
    public void setImageContentType(String imageContentType) {  
        this.imageContentType = imageContentType;  
    }  
  
    public String getImageFileName() {  
        return imageFileName;  
    }  
  
    public void setImageFileName(String imageFileName) {  
        this.imageFileName = imageFileName;  
    }  
  
    public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
    private void close(FileOutputStream fos, FileInputStream fis) {  
        if (fis != null) {  
            try {  
                fis.close();  
                fis=null;  
            } catch (IOException e) {  
                System.out.println("FileInputStream关闭失败");  
                e.printStackTrace();  
            }  
        }  
        if (fos != null) {  
            try {  
                fos.close();  
                fis=null;  
            } catch (IOException e) {  
                System.out.println("FileOutputStream关闭失败");  
                e.printStackTrace();  
            }  
        }  
    }

	
    public BeaconService getBeaconService() {
		return beaconService;
	}

	public void setBeaconService(BeaconService beaconService) {
		this.beaconService = beaconService;
	}


	public PatrolService getPatrolService() {
		return patrolService;
	}


	public void setPatrolService(PatrolService patrolService) {
		this.patrolService = patrolService;
	}
	public StaffService getStaffService() {
		return staffService;
	}
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getJsonstr() {
		return jsonstr;
	}
	public void setJsonstr(String jsonstr) {
		try {
			this.jsonstr = jsonstr;
			//this.jsonstr = new String(jsonstr.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
    

}
