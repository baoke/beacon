package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import com.buptmap.DAO.BeaconcountDAO;

@Component("beaconcountService")
public class BeaconcountService {

	private BeaconcountDAO beaconcountDAO;

	public BeaconcountDAO getBeaconcountDAO() {
		return beaconcountDAO;
	}
	@Resource(name="beaconcountDao")
	public void setBeaconcountDAO(BeaconcountDAO beaconcountDAO) {
		this.beaconcountDAO = beaconcountDAO;
	}
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public boolean add(String jsonstr){
		lock.writeLock().lock();
		try{
			return beaconcountDAO.add(jsonstr);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray find(String receive_id, String send_id){
		lock.writeLock().lock();
		try{
			return beaconcountDAO.find(receive_id, send_id);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	
	public JSONArray total(String send_id){
		lock.writeLock().lock();
		try{
			return beaconcountDAO.total(send_id);
		}finally{
			lock.writeLock().unlock();
		}
		
	}
	
}
