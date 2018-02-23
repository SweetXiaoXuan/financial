package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

public class ActivityContentString extends ActivityContent<String>{
	private Long id;
	@FormParam("aid")
	private Long aid;
	@FormParam("type")
	public Integer type;
	@FormParam("content")
	public String content;
	@FormParam("activityStatus")
	private Integer activityStatus;
	@FormParam("status")
	private Integer status;

	public Map<String, Object> getMap() {
		return new HashMap<String, Object>() {
			{
				put("type", getType());
				put("content", getContent());
				put("status", getStatus());
			}
		};
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getAid() {
		return aid;
	}

	@Override
	public void setAid(Long aid) {
		this.aid = aid;
	}

	@Override
	public Integer getType() {
		return type;
	}

	@Override
	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
