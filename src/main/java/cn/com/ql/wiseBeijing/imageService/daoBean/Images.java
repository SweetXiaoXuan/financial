package cn.com.ql.wiseBeijing.imageService.daoBean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.com.ql.wiseBeijing.news.util.PrimaryKeyGenerator;

@Table(name="images")
@Entity
public class Images {
	@Id
	@GenericGenerator(name="news_key",strategy=PrimaryKeyGenerator.NEWS_PRIMARY_KEY)
	@GeneratedValue(generator="news_key")
	private String pid;
	private String url;
	private String description;
	private String belong_id="0";
	private String belong_category="0";
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
