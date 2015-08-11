package com.buptmap.Service;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.MessageDao;

@Component("messageService")
public class MessageService {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private MessageDao messageDao;
	
	public MessageDao getMessageDao() {
		return messageDao;
	}
	@Resource(name="messageDao")
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	public ReadWriteLock getLock() {
		return lock;
	}
	
	public boolean add_message(String mac_id, String message_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.addRecord(mac_id, message_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public boolean delete_message(String mac_id, String message_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.delRecord(mac_id, message_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray message_list()
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.message_list();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray beacon_message(String mac_id)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao. beacon_message(mac_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean insert_message()
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.insert_minor();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray area_message(String[] message_id, List<Point2D.Double> pointsArray, String building, String floor){
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.area_message(message_id, pointsArray, building, floor);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public boolean minor_mes(JSONArray testArray)
	{
		lock.writeLock().lock();
		try{
			System.out.println("service");
			return messageDao.minor_mes(testArray);
		}finally{
			lock.writeLock().unlock();
		}
	}


}
