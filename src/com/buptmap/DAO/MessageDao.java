package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import java.awt.geom.Point2D;
import java.io.File;  
import jxl.Cell;  
import jxl.Sheet;  
import jxl.Workbook;  
import com.opensymphony.xwork2.interceptor.annotations.After;
import com.buptmap.model.Beacon;
import com.buptmap.model.Mes_Bea;
import com.buptmap.model.Message;
import com.buptmap.model.Minor;
import com.buptmap.model.Staff_mes;
import com.buptmap.util.*;
@Component("messageDao")
public class MessageDao {

	private final static HashMap coverageMap = new HashMap() {{    
	    put("Mi_05", 5);    
	    put("Mi_08", 8);    
	    put("Mi_10", 10);  
	    put("Mi_15", 15);  
	    put("Mi_22", 22);  
	    put("Mi_30", 30);  
	    put("Mi_30BIG", 31);  
	    put("Mi_00UNKNOW", 3); 
	}};
	private HibernateTemplate hibernateTemplate = null;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private Judge testJudge;
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
	
	public Judge getTestJudge() {
		return testJudge;
	}
	public void setTestJudge(Judge testJudge) {
		this.testJudge = testJudge;
	}
	//插入广播某一条消息的多个beacon
	public boolean addRecord(String id, String message_id)
	{
		List<Mes_Bea> result = new ArrayList<Mes_Bea>();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
			
			String[] mac_id = id.split("\\*");
			for (int i = 0; i < mac_id.length; i++) {
				result = hibernateTemplate.find("from Mes_Bea m where m.mac_id = '" + mac_id[i] + "' and m.message_id = '" + message_id +"'");
				if (result.size() == 0) {
					Mes_Bea temprecord = new Mes_Bea();
					temprecord.setMac_id(mac_id[i]);
					temprecord.setMessage_id(message_id);
					temprecord.setTime(date);
					hibernateTemplate.save(temprecord);
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	//删除某个beacon广播的某一条消息
	public boolean delRecord(String mac_id, String message_id)
	{
		List<Mes_Bea> result = new ArrayList<Mes_Bea>();
		try {
			result = hibernateTemplate.find("from Mes_Bea m where m.mac_id = '" +mac_id + "' and m.message_id = '" + message_id +"'");
			if (result.size() == 1) {
				Mes_Bea temp = result.get(0);
				hibernateTemplate.delete(temp);
				return true;
			}
			else if (result.size() == 0) {
				return true;
			}
			else{
				return false;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	//查看一个beancon播放的消息
	public JSONArray beacon_message(String mac_id)
	{
		List<Beacon> result = new ArrayList<Beacon>();
		List<Message> result1 = new ArrayList<Message>();
		List<Mes_Bea> result2 = new ArrayList<Mes_Bea>();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
			result = hibernateTemplate.find("from Beacon b where b.mac_id = '" + mac_id + "'");
			if (result.size() == 1) {
				//Beacon tempBeacon = result.get(0);
				result2 = hibernateTemplate.find("from Mes_Bea m where m.mac_id = '" + mac_id + "'");
				if (result2 != null && result2.size() != 0) {
					for (int i = 0; i < result2.size(); i++) {
						Mes_Bea temp = result2.get(i);
						result1 = hibernateTemplate.find("from Message m where m.id = '" + temp.getMessage_id() + "' and m.end_time >'" + date + "'");
						if (result1.size() == 1) {
							Message tempMessage = result1.get(0);
							jsonObject.put("title", tempMessage.getTitle());
							jsonObject.put("company", tempMessage.getName()); 
							jsonObject.put("content", tempMessage.getContent());
							jsonObject.put("id", tempMessage.getId());
							jsonObject.put("time", tempMessage.getEnd_time());
							jsonArray.add(jsonObject);
						}
						
					}
				}
				return jsonArray;
			}
			else {
				return jsonArray;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			return jsonArray;
		}
	}
	
	public JSONArray message_list()
	{
		List<Message> result = new ArrayList<Message>();		
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
			
			result = hibernateTemplate.find("from Message m where m.end_time >'" + date + "'");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					Message tempMessage = result.get(i);
					jsonObject.put("id", tempMessage.getId());
					jsonObject.put("title", tempMessage.getTitle());
					jsonObject.put("company", tempMessage.getName());
					jsonObject.put("content", tempMessage.getContent());
					jsonObject.put("time", tempMessage.getEnd_time());
					jsonArray.add(jsonObject);
				}
				return jsonArray;
			}
			else {
				return jsonArray;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			return jsonArray;
		}
	}

	public boolean insert_message()
	{
		List<Beacon> result = new ArrayList<Beacon>();
		List<Message> result1 = new ArrayList<Message>();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
            Workbook book = Workbook.getWorkbook(new File("C:\\Users\\baoke\\Desktop\\test.xls"));  
            //获得第一个表的工作对象，“0”表示第一个表  
            Sheet sheet = book.getSheet(0);  
            //得到第一列，第一行的单元格（0，0）  
            for (int i = 1; i < 1290; i++) {
            	Cell cell = sheet.getCell(3,i);  
                String minor = cell.getContents();  
                
                result = hibernateTemplate.find("from Beacon b where b.minor = '" + minor + "'");
                if(result != null && result.size() != 0)
                {
                	String url = sheet.getCell(6,i).getContents();
                	if (url != null && url != "") {
                		String title = sheet.getCell(5,i).getContents();
                		
                		for (int j = 0; j < result.size(); j++) {
							Beacon tempBeacon = result.get(j);
							tempBeacon.setAddress(title);
							hibernateTemplate.update(tempBeacon);
						}
                		
                    	String name = "西南风技术提供";
                    	Message tempMessage = new  Message();
                    	tempMessage.setContent(url);
                    	tempMessage.setEnd_time(date);
                    	tempMessage.setName(name);
                    	tempMessage.setStart_time(date);
                    	tempMessage.setTitle(title != null && title != "" ? title : "西南风");
                    	hibernateTemplate.save(tempMessage);
                    	
						result1 = hibernateTemplate.find("from Message m where m.content='" + url+"'");
						
						if (result1 != null && result1.size() != 0) {
							Message recordMessage = result1.get(0);
							for (int j = 0; j < result.size(); j++) {
								Beacon tempBeacon = result.get(j);
								Mes_Bea temp = new Mes_Bea();
								temp.setMac_id(tempBeacon.getMac_id());
								temp.setMessage_id(Integer.toString(recordMessage.getId()));
								hibernateTemplate.save(temp);
							}
						}
					}
                	
                	
                	
                }
			}

              
            book.close();  
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	public boolean update_message()
	{
		List<Beacon> result = new ArrayList<Beacon>();
		List<Message> result1 = new ArrayList<Message>();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
            Workbook book = Workbook.getWorkbook(new File("C:\\Users\\baoke\\Desktop\\test.xls"));  
            //获得第一个表的工作对象，“0”表示第一个表  
            Sheet sheet = book.getSheet(0);  
            //得到第一列，第一行的单元格（0，0）  
            for (int i = 1; i < 1290; i++) {
            	Cell cell = sheet.getCell(3,i);  
                String minor = cell.getContents();  
                
                result = hibernateTemplate.find("from Beacon b where b.minor = '" + minor + "'");
                if(result != null && result.size() != 0)
                {
                	String url = sheet.getCell(6,i).getContents();
                	if (url == null || url == "") {
                		String title = sheet.getCell(5,i).getContents();
                		
                		for (int j = 0; j < result.size(); j++) {
							Beacon tempBeacon = result.get(j);
							tempBeacon.setAddress(title);
							hibernateTemplate.update(tempBeacon);
						}
                		
                    	
					}
                	
                	
                	
                }
			}
            
              
            //取数字的时候强转一下,否则默认只取出小数点后3位  
           
  
  
              
            book.close();  
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	public boolean update_beacon()
	{
		List<Beacon> result = new ArrayList<Beacon>();
		List<Object[]> result1 = new ArrayList<Object[]>();
		try {
				result = hibernateTemplate.find("from Beacon b where b.frame=''");
           
				System.out.print(result.size());
				if(result != null && result.size() != 0){
					for (int i = 0; i < result.size(); i++) {
						Beacon tempBeacon = result.get(i);
						updateCoverage(tempBeacon);
					}
				}
            
              
            //取数字的时候强转一下,否则默认只取出小数点后3位  
           
  
  
            
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insert_message2()
	{
		List<Beacon> result = new ArrayList<Beacon>();
		List<Message> result1 = new ArrayList<Message>();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
            Workbook book = Workbook.getWorkbook(new File("C:\\Users\\baoke\\Desktop\\test.xls"));  
            //获得第一个表的工作对象，“0”表示第一个表  
            Sheet sheet = book.getSheet(0);  
            //得到第一列，第一行的单元格（0，0）  
            for (int i = 1; i < 1290; i++) {
            	Cell cell = sheet.getCell(6,i);  
                String url = cell.getContents();  
                if (url != null && !url.equals("")) {
                	result1 = hibernateTemplate.find("from Message m where m.content='" + url+"'");
                	
                	String title1 = sheet.getCell(4,i).getContents();
                	String title2 = sheet.getCell(5,i).getContents();
                	String name = "西南风技术提供";
                	if (result1 == null || result1.size() == 0) {
                		Message tempMessage = new  Message();
                    	tempMessage.setContent(url);
                    	tempMessage.setEnd_time(date);
                    	tempMessage.setName(name);
                    	tempMessage.setStart_time(date);
                    	//
                    	if (title1 != null && !title1.equals("")) {
                    		tempMessage.setTitle(title1);
						}
                    	else {
							if (title2 != null && !title2.equals("")) {
								tempMessage.setTitle(title2);
							}
							else {
								tempMessage.setTitle("西南风");
							}
						}
                    	tempMessage.setOther_info("微信合作");
                    	hibernateTemplate.save(tempMessage);
					}
                	

                }
			}
            
              
            //取数字的时候强转一下,否则默认只取出小数点后3位  
           
  
  
              
            book.close();  
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	
	public boolean insert_minor(){
		List<Beacon> result = new ArrayList<Beacon>();
		List<Message> result1 = new ArrayList<Message>();
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
            Workbook book = Workbook.getWorkbook(new File("C:\\Users\\baoke\\Desktop\\test.xls"));  
            //获得第一个表的工作对象，“0”表示第一个表  
            Sheet sheet = book.getSheet(0);  
            //得到第一列，第一行的单元格（0，0）  
            for (int i = 1; i < 1292; i++) {
            	Cell cell = sheet.getCell(6,i);  
                String url = cell.getContents();  
                String minor = sheet.getCell(3, i).getContents();
                String type_id = sheet.getCell(0, i).getContents();
                if (url != null && !url.equals("")) {
                	result1 = hibernateTemplate.find("from Message m where m.content='" + url+"'");
                	
                	if (result1 != null && result1.size() != 0) {
                		Message tempMessage = result1.get(0);
                    	Minor tempMinor = new Minor();
                    	tempMinor.setMessage_id(Integer.toString(tempMessage.getId()));
                    	tempMinor.setMinor(minor);
                    	tempMinor.setType_id(type_id);
                    	tempMinor.setTime(date);
                    	hibernateTemplate.save(tempMinor);
					}
                	else {
                		Minor tempMinor = new Minor();
                    	tempMinor.setMessage_id("");
                    	tempMinor.setMinor(minor);
                    	tempMinor.setType_id(type_id);
                    	tempMinor.setTime(date);
                    	hibernateTemplate.save(tempMinor);
					}
                	

                }
                else {
                	Minor tempMinor = new Minor();
                	tempMinor.setMessage_id("");
                	tempMinor.setMinor(minor);
                	tempMinor.setType_id(type_id);
                	tempMinor.setTime(date);
                	hibernateTemplate.save(tempMinor);
				}
			}
            
              
            //取数字的时候强转一下,否则默认只取出小数点后3位  
           
  
  
              
            book.close();  
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return false;
		}
	}

	
	public boolean minor_mes(JSONArray testArray){
		List<Minor> result = new ArrayList<Minor>();
		List<Message> result1 = new ArrayList<Message>();
		try {
			JSONObject testObject = JSONObject.fromObject(testArray.get(0));
			String tempx = testObject.getString("coord_x");
			String tempy = testObject.getString("coord_y");
			String tempbuild = testObject.getString("building");
			String tempfloor = testObject.getString("floor");
			String tempurl = testObject.getString("gen_url");
			String tempspot = testObject.getString("spot_name");
			String tempminor = testObject.getString("minor");
			String url = tempurl + "x=" + tempx +"&y=" + tempy + "&spotName="+tempspot;
			result1 = hibernateTemplate.find("from Message m where m.content='" + url+"'");
        	
        	if (result1 != null && result1.size() != 0) {
        		Message tempMessage = result1.get(0);
            	Minor tempMinor = new Minor();
            	tempMinor.setMessage_id(Integer.toString(tempMessage.getId()));
            	tempMinor.setMinor(tempminor);
            	hibernateTemplate.save(tempMinor);
			}
        	else {
        		Message tempMessage = new Message();
    			tempMessage.setContent(url);
    			tempMessage.setName("手动添加");
    			tempMessage.setTitle("手动添加");
    			hibernateTemplate.save(tempMessage);
    			
            	Minor tempMinor = new Minor();
            	tempMinor.setMessage_id(Integer.toString(tempMessage.getId()));
            	tempMinor.setMinor(tempminor);
            	hibernateTemplate.save(tempMinor);
    			
    			
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return false;
		}
	}

	/*计算出与目标区域相交的Beacon
	 * @param pointsArray 目标区域点集合
	 * @return Beacon的mac，位置信息以及生成voronoi图的大区域
	 * 
	 */
	public JSONArray area_message(String[] message_id, List<Point2D.Double> pointsArray, String building, String floor){
		List<Beacon> result = new ArrayList<Beacon>();
		result = hibernateTemplate.find("from Beacon b where b.building = '" + building + "' and b.floor ='" + floor +"'");
		myArea area = new myArea();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		boolean flag = true;
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				Beacon tempBeacon = result.get(i);
				Point2D.Double point = new Point2D.Double();
				point.setLocation(Double.valueOf(tempBeacon.getCoord_x()), Double.valueOf(tempBeacon.getCoord_y()));
				double r = Double.valueOf(coverageMap.get(tempBeacon.getCoverage()).toString());
				System.out.println(pointsArray.size());
				if (checkWithJdkGeneralPath(point, pointsArray)) {
					
					jsonObject.put("mac_id", tempBeacon.getMac_id());
					jsonObject.put("coord_x", tempBeacon.getCoord_x());
					jsonObject.put("coord_y", tempBeacon.getCoord_y());
					jsonObject.put("flag", "inner");
					double x = Double.valueOf(tempBeacon.getCoord_x());
					double y = Double.valueOf(tempBeacon.getCoord_y());
					if (flag) {
						area.minX = x;
						area.minY = y;
						flag = false;
					}
					if (x > area.maxX) {
						area.maxX = x;
					}
					if (x < area.minX) {
						area.minX = x;
					}
					if (y > area.maxY) {
						area.maxY = y;
					}
					if (y < area.minY) {
						area.minY = y;
					}
					jsonArray.add(jsonObject);
					System.out.println(tempBeacon.getMac_id());
				}
				else if(checkedOut(point, r, pointsArray)){
					jsonObject.put("mac_id", tempBeacon.getMac_id());
					jsonObject.put("coord_x", tempBeacon.getCoord_x());
					jsonObject.put("coord_y", tempBeacon.getCoord_y());
					jsonObject.put("flag", "outter");
					double x = Double.valueOf(tempBeacon.getCoord_x());
					double y = Double.valueOf(tempBeacon.getCoord_y());
					if (flag) {
						area.minX = x;
						area.minY = y;
						flag = false;
					}
					if (x > area.maxX) {
						area.maxX = x;
					}
					if (x < area.minX) {
						area.minX = x;
					}
					if (y > area.maxY) {
						area.maxY = y;
					}
					if (y < area.minY) {
						area.minY = y;
					}
					jsonArray.add(jsonObject);
					System.out.println(tempBeacon.getMac_id());
				}
			}
		}
		if (jsonArray != null && jsonArray.size() != 0) {
			jsonObject.put("maxX", area.maxX+1);
			jsonObject.put("maxY", area.maxY+1);
			jsonObject.put("minX", area.minX-1);
			jsonObject.put("minY", area.minY-1);
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}
	
	public void updateCoverage(Beacon tempBeacon){
		
		double x = Double.parseDouble(tempBeacon.getCoord_x());
		double y = Double.parseDouble(tempBeacon.getCoord_y());
		int r = 10;
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
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        System.gc();
    }
	
	//判断在区域外的Beacon是否与目标区域相交
	public  boolean checkedOut(Point2D.Double point, Double r, List<Point2D.Double> polygon){
		double x0 = point.getX();
		double y0 = point.getY();
		System.out.println(polygon.size());
		Point2D.Double first = polygon.get(0);
		double x1,y1,x2,y2 = 0;
		 
		for (int i = 0; i < polygon.size(); i++) {
			if (i !=  polygon.size() - 1) {
				x1 = polygon.get(i).getX();
				y1 = polygon.get(i).getY();
				x2 = polygon.get(i+1).getX();
				y2 = polygon.get(i+1).getY();
				if (pointToLine(x1, y1, x2, y2, x0, y0) < r) {
					return true;
				}
			}
			else {
				x1 = polygon.get(i).getX();
				y1 = polygon.get(i).getY();
				x2 = first.getX();
				y2 = first.getY();
				if (pointToLine(x1, y1, x2, y2, x0, y0) < r) {
					return true;
				}
			}
		}
		return false;
	
		
	}

	 //计算(x0,y0)到线段(x1,y1),(x2,y2)的最短距离
	public double pointToLine(double x1, double y1, double x2, double y2, double x0, double y0) {  
 
          double space = 0;  
 
          double a, b, c;  
 
          a = lineSpace(x1, y1, x2, y2);// 线段的长度  
 
          b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离  
 
          c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离  
 
          if (c+b == a) {//点在线段上  
 
             space = 0;  
 
             return space;  
 
          }  
 
          if (a <= 0.000001) {//不是线段，是一个点  
 
             space = b;  
 
             return space;  
 
          }  
 
          if (c * c >= a * a + b * b) { //组成直角三角形或钝角三角形，(x1,y1)为直角或钝角  
 
             space = b;  
 
             return space;  
 
          }  
 
          if (b * b >= a * a + c * c) {//组成直角三角形或钝角三角形，(x2,y2)为直角或钝角  
 
             space = c;  
 
             return space;  
 
          }  
          //组成锐角三角形，则求三角形的高  
          double p = (a + b + c) / 2;// 半周长  
 
          double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积  
 
          space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）  
 
          return space;  
 
      }  
 
     // 计算两点之间的距离  
    public double lineSpace(double x1, double y1, double x2, double y2) {  
 
          double lineLength = 0;  
 
          lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)  
 
                 * (y1 - y2));  
 
          return lineLength;  
 
 
      }  

     /** 
      * 返回一个点是否在一个多边形区域内 
      * @param point 
      * @param polygon 
      * @return 
      */  
    private boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {  
           java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();  
  
           Point2D.Double first = polygon.get(0);  
           p.moveTo(first.x, first.y);  
          
           for (int i = 1; i < polygon.size(); i++) {  
        	   Point2D.Double d = polygon.get(i);
        	   p.lineTo(d.x, d.y);  
           }  
  
           p.lineTo(first.x, first.y);  
  
           p.closePath();  
  
           return p.contains(point);  
  
        }  

    /**
     * 
     * @param entity Message
     * @return 
     * @author weiier
     */
    	public boolean save(Message m){
    		try{
    			hibernateTemplate.save(m);
    		}catch(Exception e){
    			e.printStackTrace();
    			return false;
    		}
    		return true;
    	}
    	
    	public boolean update(Message m){
    		try{
    			this.hibernateTemplate.update(m);
    		}catch(Exception e){
    			e.printStackTrace();
    			return false;
    		}
    		return true;
    	}
    	
    	
      	/**
    	 * @author weiier
    	 * @return All messages
    	 */
    	
    	public JSONArray message_list(String staff_id) {
    		List<Object[]> temp = new ArrayList<Object[]>();		
    		JSONArray result = new JSONArray();
    		JSONObject resultObj = new JSONObject();
    		try {
    			temp = hibernateTemplate.find("select message_id,title,name,content,pr_title,end_time,major,minor,status,logo_url,page_id,project_id,other_info" +
    					" from Staff_mes where staff_id=? order by minor asc",staff_id);
    			if (temp != null && temp.size() > 0) {
    				for(int i = 0; i < temp.size();i++){
    					resultObj.put("message_id", temp.get(i)[0]);
        				resultObj.put("title", temp.get(i)[1]==null?"":temp.get(i)[1]);
        				resultObj.put("name", temp.get(i)[2]==null?"":temp.get(i)[2]);
        				resultObj.put("content", temp.get(i)[3]==null?"":temp.get(i)[3]);
        				resultObj.put("project_title", temp.get(i)[4]==null?"":temp.get(i)[4]);
        				resultObj.put("end_time", temp.get(i)[5]==null?"":temp.get(i)[5]);
        				resultObj.put("major", temp.get(i)[6]==null?"":temp.get(i)[6]);
        				resultObj.put("minor", temp.get(i)[7]==null?"":temp.get(i)[7]);
        				resultObj.put("status", temp.get(i)[8]==null?"":temp.get(i)[8]);
        				resultObj.put("logo_url", temp.get(i)[9]==null?"":temp.get(i)[9]);
        				resultObj.put("page_id", temp.get(i)[10]==null?"":temp.get(i)[10]);
        				resultObj.put("project_id", temp.get(i)[11]==null?"":temp.get(i)[11]);
        				resultObj.put("other_info", temp.get(i)[12]==null?"":temp.get(i)[12]);
        				result.add(resultObj);
    				}
    				return result;
    			}else{
    				return null;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			return null;
    		}finally{
    			if(temp != null){temp.clear(); temp = null;}
    			if(resultObj != null) { resultObj.clear(); resultObj = null; }
    		}
    	}
    	
    	/**
    	 * @author weiier
    	 * @return message
    	 */
    	public Message message_one(int message_id) {
    		List<Message> result = new ArrayList<Message>();		
    		Message tempMessage = new Message();
    		try {
    			result = hibernateTemplate.find("from Message where id="+message_id);
    			if (result != null && result.size() > 0) {		
    				tempMessage	 = result.get(0);
    				return tempMessage;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
    	
    	public List<Message> checkUrl(String url) {
    		List<Message> result = new ArrayList<Message>();		
    		try {
    			result = hibernateTemplate.find("from Message where content='"+url+"'");
    			if (result != null && result.size() > 0) {		
    				return result;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
    	
    	
    	/**
    	 * @author weiier
    	 * @return message detail from Staff_mes
    	 */
    	public List<Staff_mes> message_deList(int message_id,String staff_id) {
    		List<Staff_mes> result = new ArrayList<Staff_mes>();		
    		
    		try {
    			result = hibernateTemplate.find("from Staff_mes where message_id='"+message_id+"' and staff_id='"+staff_id+"'");
    			if (result != null && result.size() > 0) {
    				return result;	
    			}
    			return null;
    		} catch (Exception e) {
    			e.printStackTrace();
    			return null;
    		}
    	}
    	
}

class myArea{
	public double minX;
	public double minY;
	public double maxX;
	public double maxY;
	public double getMinX() {
		return minX;
	}
	public void setMinX(double minX) {
		this.minX = minX;
	}
	public double getMinY() {
		return minY;
	}
	public void setMinY(double minY) {
		this.minY = minY;
	}
	public double getMaxX() {
		return maxX;
	}
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}
	public double getMaxY() {
		return maxY;
	}
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	
}
