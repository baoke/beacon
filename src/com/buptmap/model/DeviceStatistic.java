package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Date;
import java.sql.Timestamp;
/*
 * @author Lynn
 
 */


@Entity
@Table(name="device_statistic")

public class DeviceStatistic {
	private int id;
	private int device_id;
	private int shake_uv;
	private int shake_pv;
	private int click_uv;
	private int click_pv;
	private Date ftime;
	private Timestamp create_time;
	
	
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public int getShake_uv() {
		return shake_uv;
	}
	public void setShake_uv(int shake_uv) {
		this.shake_uv = shake_uv;
	}
	public int getShake_pv() {
		return shake_pv;
	}
	public void setShake_pv(int shake_pv) {
		this.shake_pv = shake_pv;
	}
	public int getClick_uv() {
		return click_uv;
	}
	public void setClick_uv(int click_uv) {
		this.click_uv = click_uv;
	}
	public int getClick_pv() {
		return click_pv;
	}
	public void setClick_pv(int click_pv) {
		this.click_pv = click_pv;
	}
	public Date getFtime() {
		return ftime;
	}
	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	
	
	
	

}
