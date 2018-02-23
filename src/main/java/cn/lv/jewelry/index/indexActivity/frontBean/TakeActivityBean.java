package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;

public class TakeActivityBean {
	@FormParam("aid")
	private int aid;
	@FormParam("eid")
	private int eid;
	@FormParam("time")
	private long time;
	private int status=0;
	private Integer level;

	public TakeActivityBean(){}
	public TakeActivityBean(int aid, int eid, long time, Integer level){
		this.aid = aid;
		this.eid = eid;
		this.time = time;
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
