package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.BeaconDao;
import com.buptmap.model.Testbeacon;

@Component("beaconService")
public class BeaconService {
	private BeaconDao beaconDao;

	public BeaconDao getBeaconDao() {
		return beaconDao;
	}

	@Resource(name="beaconDAO")
	public void setBeaconDao(BeaconDao beaconDao) {
		this.beaconDao = beaconDao;
	}
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public boolean download(String unitid, String company_id)
	{
		lock.writeLock().lock();
		try{
			return beaconDao.download(unitid, company_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public JSONArray findOne(String mac_id){
		lock.writeLock().lock();
		try{
			return beaconDao.findOne(mac_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray getbuilding(String company_id){
		lock.writeLock().lock();
		try{
			return beaconDao.getbuilding(company_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	public boolean update(List<Testbeacon> testList){
		System.out.println("service");
		lock.writeLock().lock();
		try{
			return beaconDao.update(testList);
		}finally{
			lock.writeLock().unlock();
		}
			
	
	}
	public boolean re_deploy(JSONArray testArray, String idString){
		lock.writeLock().lock();
		try{
			return beaconDao.re_deploy(testArray, idString);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public JSONArray findbybuilding(String building_id, String company_id){
		lock.writeLock().lock();
		try{
			return beaconDao.findbybuilding(building_id, company_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray findSession(String user_id){
		lock.writeLock().lock();
		try{
			return beaconDao.findSession(user_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public String userSession(String user_id){
		lock.writeLock().lock();
		try{
			return beaconDao.userSession(user_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray findbystaff(String user_id){
		lock.writeLock().lock();
		try{
			return beaconDao.findbystaff(user_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public JSONArray findBeacon(String jsonstr) {
		lock.writeLock().lock();
		try{
			return beaconDao.findBeacon(jsonstr);
		}finally{
			lock.writeLock().unlock();
		}	
		
	}
	
}
