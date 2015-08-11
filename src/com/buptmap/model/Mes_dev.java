package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="mes_dev_info")

public class Mes_dev {
	
	private int id;
	private String vdevice_id;
	private String message_id;
	private String uuid;
	private String major;
	private String minor;
	private String time;
	private String mes_status;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVdevice_id() {
		return vdevice_id;
	}
	public void setVdevice_id(String vdevice_id) {
		this.vdevice_id = vdevice_id;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getMinor() {
		return minor;
	}
	public void setMinor(String minor) {
		this.minor = minor;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMes_status() {
		return mes_status;
	}
	public void setMes_status(String mes_status) {
		this.mes_status = mes_status;
	}
	
	

}
