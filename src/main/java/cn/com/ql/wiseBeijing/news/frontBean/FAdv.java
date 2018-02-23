package cn.com.ql.wiseBeijing.news.frontBean;

import javax.ws.rs.FormParam;

public class FAdv {
	private int id;
	@FormParam("title")
	private String title;
	@FormParam("largeimage")
	private String largeimage;
	@FormParam("type")
	private String type;
	@FormParam("content")
	private String content;
	@FormParam("beginTime")
	private String beginTime;
	@FormParam("endTime")
	private String endTime;
	private String createTime;
	private String status;
	@FormParam("sort")
	private String sort;
	@FormParam("target")
	private String target;
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
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
	public String getLargeimage() {
		return largeimage;
	}
	public void setLargeimage(String largeimage) {
		this.largeimage = largeimage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
