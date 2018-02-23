package cn.com.ql.wiseBeijing.imageService.frontBean;

import javax.ws.rs.FormParam;



public class FImages {
	@FormParam("pid")
	private String pid;
	@FormParam("url")
	private String url;
	@FormParam("description")
	private String description;
	private String belong_id;
	private String belong_category;
	private String uploadtime;
	private int status;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBelong_id() {
		return belong_id;
	}
	public void setBelong_id(String belong_id) {
		this.belong_id = belong_id;
	}
	public String getBelong_category() {
		return belong_category;
	}
	public void setBelong_category(String belong_category) {
		this.belong_category = belong_category;
	}
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
