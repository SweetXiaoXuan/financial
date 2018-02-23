package cn.com.ql.wiseBeijing.auth.frontBean;

import javax.ws.rs.FormParam;

public class FUserApi {
	@FormParam("id")
	private int id;
	@FormParam("title")
	private String title;
	@FormParam("url")
	private String url;
	@FormParam("msg")
	private String msg;
	private String info;
	@FormParam("status")
	private String status;
	@FormParam("through")
	private String through;
	@FormParam("notthrough")
	private String notthrough;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getThrough() {
		return through;
	}
	public void setThrough(String through) {
		this.through = through;
	}
	public String getNotthrough() {
		return notthrough;
	}
	public void setNotthrough(String notthrough) {
		this.notthrough = notthrough;
	}
	
}
