package com.buptmap.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.HashMap;  
import com.buptmap.model.Vdev_mes_bind;
import com.buptmap.util.NetTools;

/**
 * 
 * @author weiier
 *
 */
public class WeChatAPI {
	//{"success":true,"pic_url":"http:\/\/shp.qpic.cn\/wx_shake_bus\/0\/1438158545d03d864b7f43db9ce34df5f720509d0e\/120","description":"success."}
	//logo上传接口
	public static Map<String,String> getLogoUrl(String logo) {
		        Map<String,String> resultMap = new HashMap<String,String>();
				String urlStr = "http://wx.sinofond.com/wx/admin/api/uploadPic.php";            
		        Map<String, String> textMap = new HashMap<String, String>();  		      
		        textMap.put("file", "testname");  
		        Map<String, String> fileMap = new HashMap<String, String>();  
		        fileMap.put("file", logo);  
		        String ret = NetTools.formUpload(urlStr, textMap, fileMap);
		        if(ret != null){
		        		JSONObject result = JSONObject.fromObject(ret);
				        if(result.getBoolean("success")){
				        	resultMap.put("url", result.getString("pic_url"));
				        }else{
				        	System.out.println("addLogoError:"+result.getString("description"));
				        	resultMap.put("addLogoError", result.getString("description"));		        	
				        }
		        }else{
		        	System.out.println("addLogoError:return null");
		        	resultMap.put("addLogoError", "post请求出错");		        	
		        }
		       
		        return resultMap;
	}
	
	//url新增接口
	public static Map<String,String> getPageId(JSONArray devices,String title,String description,String page_url,String comment,String icon_path){
		 Map<String,String> resultMap = new HashMap<String,String>();
		String urlStr = "http://wx.sinofond.com/wx/admin/api/createPage.php";
		String postStr = "{\"devices\":[";
		String uuid = null;
		String major = null;
		String minor = null;
		for(int n = 0; n < devices.size();n++){
			 uuid = devices.getJSONObject(n).getString("uuid");
			 major = devices.getJSONObject(n).getString("major");
			 minor = devices.getJSONObject(n).getString("minor");	
			 if(n != devices.size() -1){
			 postStr += "{\"uuid\":\""+uuid+"\",\"major\":\""+major+"\",\"minor\":\""+minor+"\"},";
			 }else{
				 postStr += "{\"uuid\":\""+uuid+"\",\"major\":\""+major+"\",\"minor\":\""+minor+"\"}],";
			 }
		}
		
		postStr += "\"title\":\""+title+"\",";
		postStr += "\"description\":\""+description+"\",";
		postStr += "\"page_url\":\""+page_url+"\",";
		postStr += "\"comment\":\""+comment+"\",";
		postStr += "\"icon_path\":\""+icon_path+"\"}";
	
		String result = NetTools.doPost(urlStr, postStr);
		JSONObject resulto = JSONObject.fromObject(result);
		if( resulto.getBoolean("success")){
			resultMap.put("page_id", resulto.getString("page_id"));
		}else{
			System.out.println("addPageError:"+resulto.getString("description"));
			resultMap.put("addPageError", resulto.getString("description"));
		}
		return resultMap;
	}
	
	//url编辑接口
	public static Map<String,String> editPage(String page_id,String title,String description,String page_url,String comment,String icon_path){
		 Map<String,String> resultMap = new HashMap<String,String>();
		String editUrl = "http://wx.sinofond.com/wx/admin/api/updatePage.php";
		String editParam = "{\"page_id\":\""+page_id+"\",";
		editParam += "\"title\":\""+title+"\",";
		editParam += "\"description\":\""+description+"\",";
		editParam += "\"page_url\":\""+page_url+"\",";
		editParam += "\"comment\":\""+comment+"\",";
		editParam += "\"icon_path\":\""+icon_path+"\"}";
		
		String edit = NetTools.doPost(editUrl, editParam);
		JSONObject edito = JSONObject.fromObject(edit);
		if( edito.getBoolean("success")){
			resultMap.put("page_id", edito.getString("page_id"));
		}else{
			System.out.println("editPageError:"+edito.getString("description"));
			resultMap.put("editPageError", edito.getString("description"));
		}
		return resultMap;
	}
	
	//url删除接口
	public static JSONObject deletePage(List<Vdev_mes_bind> data,String page_id) {
		String deleteUrl = "http://wx.sinofond.com/wx/admin/api/removePage.php";
		if(data != null && page_id != null){
			String postStr = "{\"devices\":[";
			for(int i = 0 ;i < data.size(); i++){
				if(i != data.size()-1){
					postStr += "{\"uuid\":\""+data.get(i).getUuid()+"\",\"major\":\""+data.get(i).getMajor()+"\",\"minor\":\""+data.get(i).getMinor()+"\"},";
				} else{
					postStr += "{\"uuid\":\""+data.get(i).getUuid()+"\",\"major\":\""+data.get(i).getMajor()+"\",\"minor\":\""+data.get(i).getMinor()+"\"}],";
				}
			}
			postStr += "\"page_id\":\""+page_id+"\"}";	
			JSONObject obj = JSONObject.fromObject(NetTools.doPost(deleteUrl, postStr));
			return obj;
		}
		return null;
	}
	
	public static void main(String[] args) {
		/*// TODO Auto-generated method stub
		NetTools nt = new NetTools();
		String editUrl = "http://wx.sinofond.com/wx/admin/api/updatePage.php";
		String editParam = "{\"page_id\":\"242339\",";
		editParam += "\"title\":\"主标题只有六\",";
		editParam += "\"description\":\"副标题\",";
		editParam += "\"page_url\":\"https://zbs.weixin.qq.com\",";
		editParam += "\"comment\":\"数据示例\",";
		editParam += "\"icon_path\":\"http://p.qpic.cn/ecc_merchant/0/w_pic_1435209919477/0\"}";
		
		System.out.println(editParam);
		nt.doPost(editUrl, editParam);*/
		
		 	//String filepath="http://10.103.242.71:8888/IBeaconSystem/uploadFiles/对的.jpg";  
		/*	String filepath = "C:\\Tomcat7.0\\webapps\\IBeaconSystem\\uploadFiles\\aaa.jpg";
	        
			String urlStr = "http://wx.sinofond.com/wx/admin/api/uploadPic.php";  
	          
	        Map<String, String> textMap = new HashMap<String, String>();  
	      
	        textMap.put("file", "testname");  
	  
	        Map<String, String> fileMap = new HashMap<String, String>();  
	          
	        fileMap.put("file", filepath);  
	          
	        String ret = NetTools.formUpload(urlStr, textMap, fileMap);  
	          
	        System.out.println(ret);  */
		
		/*String deleteUrl = "http://wx.sinofond.com/wx/admin/api/removePage.php";
		String deleParam = "{\"devices\":[";
		deleParam += "{\"uuid\":\"FDA50693-A4E2-4FB1-AFCF-C6EB07647825\",\"major\":\"10009\",\"minor\":\"40587\"},";
		deleParam += "{\"uuid\":\"FDA50693-A4E2-4FB1-AFCF-C6EB07647825\",\"major\":\"10009\",\"minor\":\"40588\"}],";
		deleParam += "\"page_id\": 249541}";
		
		NetTools.doPost(deleteUrl, deleParam);*/
		/*String s = new String("e:\\d\\test");
		String str = new String("{'staff_id':'902t','url_id':'1461','title':'测试device','name':'ssss','content':'https%3A%2F%2Fzbs.weixin.qq.com','other_info':'','project_id':'3','logo':'E:\\programTools\\Tomcat7.0\\webapps\\IBeaconSystem\\uploadFiles\\logoall.jpg'}");
		System.out.println(str);
		str = str.replace("\\", "\\\\"); 
		System.out.println(s);
		JSONObject j = JSONObject.fromObject(str);
		System.out.println(j);*/
		
		/*String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		System.out.println(name);
		String[] names = ("test.java.jsp.jpg").split("\\.");
		System.out.println(names[names.length-1]);
		List<String> s = new ArrayList<String>();
		//s.add(null);
		System.out.println(s.size());
		int[] a = {1,10,2,5,20};
		Arrays.sort(a);
		for( int i : a){
			System.out.print(i);
		}*/
		
		JSONArray minorArray = new JSONArray();
		JSONObject minorObject = new JSONObject();
		
		int[] minors ={1,10,5,4,2,20,50,19,11,9};
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
		System.out.println(minorArray);
	}
}
