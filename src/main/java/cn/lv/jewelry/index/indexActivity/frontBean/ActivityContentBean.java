package cn.lv.jewelry.index.indexActivity.frontBean;

import java.util.Map;

public class ActivityContentBean {
	private Long aid;
	private String title;
	private String subTitle;
	private Map<String,ActivityContent> contents;
	private ActivityBean activityBean;

	public ActivityBean getActivityBean() {
		return activityBean;
	}

	public void setActivityBean(ActivityBean activityBean) {
		this.activityBean = activityBean;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String,ActivityContent> getContents() {
		return contents;
	}
	public void setContents(Map<String,ActivityContent> contents) {
		this.contents = contents;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}
}
