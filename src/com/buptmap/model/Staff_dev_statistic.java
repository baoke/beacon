package com.buptmap.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="staff_dev_statistic_info")

public class Staff_dev_statistic {
	
	private int id;
	private String uuid;
	private String major;
	private String minor;
	private String staff_id;
	private String status;
	private String vdevice_id;
	private int shake_uv;
	private int shake_pv;
	private int click_uv;
	private int click_pv;
	private Date ftime;
	private Timestamp create_time;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVdevice_id() {
		return vdevice_id;
	}
	public void setVdevice_id(String vdevice_id) {
		this.vdevice_id = vdevice_id;
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
