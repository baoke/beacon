package com.buptmap.DAO;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import com.buptmap.model.Staff_dev;
import com.buptmap.model.Vdev_mes_bind;
import com.buptmap.model.Vdevice;
/**
 * 
 * @author weiier
 *		methods be used when manage URL 
 */
@Component
public class Staff_devDAO {
	private HibernateTemplate hibernateTemplate;
	private List<String> temp;
	public Staff_dev checkExist( String staff_id , String uuid, String major,String minor) {
		List<Staff_dev> result = new ArrayList<Staff_dev>();		
		Staff_dev temp = new Staff_dev();
		try {
			result = hibernateTemplate.find("from Staff_dev where staff_id='"+staff_id+"' and uuid='"+uuid
					+"' and major='"+major+"' and minor='"+minor+"'");
			if (result != null && result.size() > 0) {		
				temp	 = result.get(0);
				return temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Vdevice> selectByminors(String uuid,String major,String start,String end){
		List<Vdevice> result = new ArrayList<Vdevice>();
		result = hibernateTemplate.find("from Vdevice where uuid='"+uuid+"' and major='"+major+"' and minor between '"+start+"' and '"+end+"'");
		if(result != null && result.size() > 0){
			return result;
		}else{
			return null;
		}
	}
	
	public List<Vdev_mes_bind> checkMinors(String uuid,String major,String start,String end){
		List<Vdev_mes_bind> result = new ArrayList<Vdev_mes_bind>();
		result = hibernateTemplate.find("from Vdev_mes_bind where uuid='"+uuid+"' and major='"+major+"' and minor between '"+start+"' and '"+end+"'");
		if(result != null && result.size() > 0){
			return result;
		}else{
			return null;
		}
	}
	
	public List<Vdevice> checkDeviceId(String uuid,String major,String start,String end){
		List<Vdevice> result = new ArrayList<Vdevice>();
		result = hibernateTemplate.find("from Vdevice where uuid='"+uuid+"' and major='"+major
				+"' and vdevice_id='-1' and minor between '"+start+"' and '"+end+"'");
		if(result != null && result.size() > 0){
			return result;
		}else{
			return null;
		}
	}
	
	public List<Vdev_mes_bind> selectByMessage(String message_id){
		List<Vdev_mes_bind> result = new ArrayList<Vdev_mes_bind>();
		result = hibernateTemplate.find("from Vdev_mes_bind where message_id='"+message_id+"'");
		if(result != null && result.size() > 0){
			return result;
		}else{
			return null;
		}
	}
	
	public Boolean saveDev_mes(Vdev_mes_bind dev_mes){
		try{
			this.hibernateTemplate.save(dev_mes);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<String> distinctUUID(int message_id,String staff_id) {
		List<String> result = new ArrayList<String>();
		try {
			temp = hibernateTemplate.find("select distinct uuid from Staff_mes where message_id=? and staff_id=?",new Object[]{message_id,staff_id});
			if (temp != null && temp.size() > 0) {
				for(int i = 0; i < temp.size(); i++){
					result.add( temp.get(i));
				}
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> distinctMajor(int message_id,String staff_id,String uuid) {
		List<String> result = new ArrayList<String>();
		try {
			temp = hibernateTemplate.find("select distinct major from Staff_mes where message_id=? and staff_id=? and uuid=?",new Object[]{message_id,staff_id,uuid});
			if (temp != null && temp.size() > 0) {
				for(int i = 0; i < temp.size(); i++){
					result.add(temp.get(i));
				}
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int[] findMinors(int message_id,String staff_id,String uuid,String major) {
		try {
			temp = this.hibernateTemplate.find("select minor from Staff_mes where message_id=? and staff_id=? and uuid=? and major=?",
					new Object[]{message_id,staff_id,uuid,major});
			if (temp != null && temp.size() > 0) {
				int[] result = new int[temp.size()];
				for(int i = 0; i < temp.size(); i++){
					result[i] = Integer.parseInt(temp.get(i));
				}
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
