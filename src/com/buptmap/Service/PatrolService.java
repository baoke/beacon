package com.buptmap.Service;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.PatrolDao;

@Component("patrolService")
public class PatrolService {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private PatrolDao patrolDao;
	public PatrolDao getPatrolDao() {
		return patrolDao;
	}

	@Resource(name="patrolDao")
	public void setPatrolDao(PatrolDao patrolDao) {
		this.patrolDao = patrolDao;
	}
	
	public boolean update(JSONArray testArray, String user_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return patrolDao.update(testArray,user_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray selectall(HashMap conditions)
	{
		lock.writeLock().lock();
		try{
		
			System.out.println("service");
			return patrolDao.findall(conditions);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray weijiance()
	{
		lock.writeLock().lock();
		try{
		
			System.out.println("service");
			return patrolDao.weijiance();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray findone(String mac_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return patrolDao.findone(mac_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray getbuildingid(String mac_id)
	{
		
		return patrolDao.getbuildingid(mac_id);	
	}
	
	public JSONArray findbybuilding(String building_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return patrolDao.findbybuilding(building_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray floorbeacon(String building_id, String floor_id){
		lock.writeLock().lock();
		try{
			return patrolDao.floorbeacon(building_id, floor_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean upload(JSONArray testArray){
		System.out.println("service");
		lock.writeLock().lock();
		try{
			return patrolDao.upload(testArray);
		}finally{
			lock.writeLock().unlock();
		}
			
	
	}
	
	

}
