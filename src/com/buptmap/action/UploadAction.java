package com.buptmap.action;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.util.HashMap;
import java.util.Map;  
  
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;  
  
import com.opensymphony.xwork2.ActionContext;  
import com.opensymphony.xwork2.ActionSupport;  
  
  
public class UploadAction extends ActionSupport {  
  
	private JSONObject resultObj;
    private File upload;  
    private String uploadFileName;    
    private String uploadContentType;  
      
    private long maximumSize;  
    private String allowedTypes;  
      
  
    
    public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	public File getUpload() {  
        return upload;  
    }  
    public void setUpload(File upload) {  
        this.upload = upload;  
    }  
    public String getUploadFileName() {  
        return uploadFileName;  
    }  
    public void setUploadFileName(String uploadFileName) {  
        this.uploadFileName = uploadFileName;  
    }  
  
    public String getUploadContentType() {  
        return uploadContentType;  
    }  
    public void setUploadContentType(String uploadContentType) {  
        this.uploadContentType = uploadContentType;  
    }  
    public long getMaximumSize() {  
        return maximumSize;  
    }  
    public void setMaximumSize(long maximumSize) {  
        this.maximumSize = maximumSize;  
    }  
    public String getAllowedTypes() {  
        return allowedTypes;  
    }  
    public void setAllowedTypes(String allowedTypes) {  
        this.allowedTypes = allowedTypes;  
    }  
    @Override  
    public String execute() throws Exception {  
          
    	Map<String,Object> map = new HashMap<String,Object>();
        File uploadFile = new File("c:/upload");  
        if(!uploadFile.exists()) {  
            uploadFile.mkdir();  
        }  
          
        /*
        //验证文件大小及格式  
        if (maximumSize < upload.length()) {  
            return "error";  
        }  
          
        boolean flag =false;  
        String[]  allowedTypesStr = allowedTypes.split(",");  
        for (int i = 0; i < allowedTypesStr.length; i++) {  
            if (uploadContentType.equals(allowedTypesStr[i])) {  
                flag = true;  
            }  
        }  
        if (flag == false) {  
            Map request = (Map) ActionContext.getContext().get("request");  
            request.put("errorMassage", "文件类型不合法！");  
              
            System.out.println(request.toString());  
            return "error";  
        }  
          */
   
        System.out.println(uploadFileName);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(upload)));  
        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(uploadFile+"\\"+ uploadFileName)));  
        try {  
            char [] c =new char[1024];  
            int i = 0;  
            while ((i = bReader.read(c)) > 0) {  
                bWriter.write(c, 0, i);  
            }  
           
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            bReader.close();  
            bWriter.close();              
            //删除临时文件  
            upload.delete();  
        }  
          
          
        map.put("success", true);	
		resultObj = JSONObject.fromObject(map);
        return "success";  
    }     
      
}  