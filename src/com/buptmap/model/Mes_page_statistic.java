package com.buptmap.model;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mes_page_statistic_info")

public class Mes_page_statistic {
	
	private int id;
	private String uuid;
	private String major;
	private String minor;
	private String staff_id;
	private String mes_title;
	private String mes_name;
	private String mes_content;
	private String page_id;
	private String project_id;
	private String pro_title;
	private int shake_uv;
	private int shake_pv;
	private int click_uv;
	private int click_pv;
	private Date ftime;
	
	
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
	public String getMes_title() {
		return mes_title;
	}
	public void setMes_title(String mes_title) {
		this.mes_title = mes_title;
	}
	public String getMes_name() {
		return mes_name;
	}
	public void setMes_name(String mes_name) {
		this.mes_name = mes_name;
	}
	public String getMes_content() {
		return mes_content;
	}
	public void setMes_content(String mes_content) {
		this.mes_content = mes_content;
	}
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getPro_title() {
		return pro_title;
	}
	public void setPro_title(String pro_title) {
		this.pro_title = pro_title;
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
	
	
	
	

}
