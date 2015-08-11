package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import com.buptmap.DAO.StatisticDAO;

@Component("statisticService")

public class StatisticService {
	
	private StatisticDAO statisticDAO;

	public StatisticDAO getStatisticDAO() {
		return statisticDAO;
	}
	@Resource(name="statisticDao")
	public void setStatisticDAO(StatisticDAO statisticDAO) {
		this.statisticDAO = statisticDAO;
	}
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONArray findStatisticMinor(String jsonString) {
		return statisticDAO.findStatisticMinor(jsonString);
	}
	
	public JSONArray findStatisticUrl(String jsonString) {
		return statisticDAO.findStatisticUrl(jsonString);		
	}
	
	public JSONArray findStatisticProject(String jsonString) {
		 return statisticDAO.findStatisticProject(jsonString);
	}
	
	public boolean Instaff(String jsonString) {
		return statisticDAO.InStaff(jsonString);
		
	}
	
	public JSONArray findStasticAll(String staff_id,String style) {
		lock.writeLock().lock();
		try{
			return statisticDAO.findStatisticAll(staff_id, style);
		}finally{
			lock.writeLock().unlock();
		}
		
		
	}
	
	/*public boolean UploadChengdu(String staff_id ) {
		return statisticDAO.UploadChengdu(staff_id);
	}*/
	
	public boolean UploadWangfeng(String staff_id) {
		lock.writeLock().lock();
		try{
			return statisticDAO.UploadWangfeng(staff_id);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	
	
	
	

}
