package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.StaffDao;

@Component("staffService")
public class StaffService {
	private StaffDao staffDao;
	public StaffDao getStaffDao() {
		return staffDao;
	}
	@Resource(name="staffDao")
	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONObject login(String id, String pwd){
		lock.writeLock().lock();
		try{
			return staffDao.login(id, pwd);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public JSONObject log_in(String id, String pwd){
		lock.writeLock().lock();
		try{
			return staffDao.log_in(id, pwd);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public boolean verify(String id, String key)
	{
		lock.writeLock().lock();
		try{
			return staffDao.verify(id, key);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray findall(String user_id){
		lock.writeLock().lock();
		try{
			return staffDao.findall(user_id);
		}finally{
			lock.writeLock().unlock();
		}		
	}
	
	public JSONArray findone(String id){
		lock.writeLock().lock();
		try{
			return staffDao.findone(id);
		}finally{
			lock.writeLock().unlock();
		}	
		
	}

	public boolean add(String jsonstr){
		lock.writeLock().lock();
		try{
			return staffDao.add(jsonstr);
		}finally{
			lock.writeLock().unlock();
		}	
		
	}
	
	public boolean edit(String jsonstr){
		lock.writeLock().lock();
		try{
			return staffDao.edit(jsonstr);
		}finally{
			lock.writeLock().unlock();
		}	
	}
	
	public boolean delete(String id){
		lock.writeLock().lock();
		try{
			return staffDao.delete(id);
		}finally{
			lock.writeLock().unlock();
		}		
	}
	
	public boolean verify_session(String jsonstr){
		lock.writeLock().lock();
		try{
			return staffDao.verify_session(jsonstr);
		}finally{
			lock.writeLock().unlock();
		}	
	}
	
	public JSONObject add_session(String sessionString){
		lock.writeLock().lock();
		try{
			return staffDao.addSession(sessionString);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	
	public JSONArray find_vacant(String staff_id){
		lock.writeLock().lock();
		try{
			return staffDao.find_vacant(staff_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public JSONArray unused_dev(String staff_id) {
		lock.writeLock().lock();
		try{
			return staffDao.unused_dev(staff_id);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	/*Lynn-test
	public JSONArray objectToJsonArray(List<Object[]> objects){
		lock.writeLock().lock();
		try{
			return staffDao.objectToJsonArray(objects);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	*/
	/*public boolean upload_xls(String staff_id){
		lock.writeLock().lock();
		try{
			return staffDao.upload_xls(staff_id);
		}finally{
			lock.writeLock().unlock();
		}
	}*/
	
	public boolean test(){
		lock.writeLock().lock();
		try{
			return staffDao.test();
		}finally{
			lock.writeLock().unlock();
		}		
	}
	
	public JSONArray find_differ_session(JSONArray uuidArray1, JSONArray uuidArray2){
		lock.writeLock().lock();
		try{
			return staffDao.find_differ_session(uuidArray1, uuidArray2);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
}
