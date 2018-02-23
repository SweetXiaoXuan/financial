package cn.com.ql.wiseBeijing.dec.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.com.ql.wiseBeijing.dec.util.DecNewsType;
import cn.com.ql.wiseBeijing.dec.util.PrimaryKeyGenerator;

@Table(name="decmainnews")
@Entity
public class DecMainNews 
{
	@Id
	@GenericGenerator(name="news_key",strategy=PrimaryKeyGenerator.NEWS_PRIMARY_KEY)
	@GeneratedValue(generator="news_key")
	private String nid;
	private String title;
	@Column(name="abstract")
	private String description;
	private String keywords;
	private String createtime;
	private String pubdate;
	private String content;
	private int pubstatus;
	private String editor;
	private String reporter;
	private String creator;
	private String type=DecNewsType.REGULAR.get();
	private String listimage;
	private String smallimage;
	private String largeimage;
	private int share_flag;
	private int comment_flag;
	private String status;
	private String sources;
	private String urls;
	private int sort_flag;
	private String template;
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPubstatus() {
		return pubstatus;
	}
	public void setPubstatus(int pubstatus) {
		this.pubstatus = pubstatus;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getListimage() {
		return listimage;
	}
	public void setListimage(String listimage) {
		this.listimage = listimage;
	}
	public String getSmallimage() {
		return smallimage;
	}
	public void setSmallimage(String smallimage) {
		this.smallimage = smallimage;
	}
	public String getLargeimage() {
		return largeimage;
	}
	public void setLargeimage(String largeimage) {
		this.largeimage = largeimage;
	}
	public int getShare_flag() {
		return share_flag;
	}
	public void setShare_flag(int share_flag) {
		this.share_flag = share_flag;
	}
	public int getComment_flag() {
		return comment_flag;
	}
	public void setComment_flag(int comment_flag) {
		this.comment_flag = comment_flag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	public int getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(int sort_flag) {
		this.sort_flag = sort_flag;
	}
	

}
