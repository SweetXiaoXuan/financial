package cn.com.ql.wiseBeijing.dec.frontBean;

import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

import cn.xxtui.support.util.DateUtil;

public class FDecNews {
	private String nid;
	@FormParam(value = "title")
	private String title="";
	@FormParam(value = "description")
	private String description="";
	@FormParam(value = "keywords")
	private String keywords="";
	private String createtime=DateUtil.getCurrentDateForSql();
	@FormParam(value = "pubdate")
	private String pubdate=createtime;
	@FormParam(value="content")
	private String content="";
	@FormParam(value="pubstatus")
	private int pubstatus=0;
	@FormParam(value="editor")
	private String editor="";
	@FormParam(value="reporter")
	private String reporter="";
	private String creator="";
	@FormParam(value = "type")
	private String type="";
	@FormParam(value = "listimage")
	private String listimage="";
	@FormParam(value = "smallimage")
	private String smallimage="";
	@FormParam(value = "largeimage")
	private String largeimage="";
	private int share_flag=0;
	private int comment_flag=0;
	private String status="0";
	@FormParam(value = "source")
	private String sources="";
	@FormParam(value = "url")
	private String urls="";
	@FormParam(value="sort_flag")
	private int sort_flag=0;
	@FormParam(value="template")
	private int template=0;
	
	@FormParam(value="adv")
	private String adv;//逗号分开
	@FormParam(value="recNews")
	private String recNews;//逗号分开
	private List additionalList;
	private Map<String,Object> addtionalMap;
	
	
	
	public String getRecNews() {
		return recNews;
	}
	public void setRecNews(String recNews) {
		this.recNews = recNews;
	}
	public String getAdv() {
		return adv;
	}
	public void setAdv(String adv) {
		this.adv = adv;
	}
	public Map<String, Object> getAddtionalMap() {
		return addtionalMap;
	}
	public void setAddtionalMap(Map<String, Object> addtionalMap) {
		this.addtionalMap = addtionalMap;
	}
	public int getTemplate() {
		return template;
	}
	public void setTemplate(int template) {
		this.template = template;
	}
	public List getAdditionalList() {
		return additionalList;
	}
	public void setAdditionalList(List additionalList) {
		this.additionalList = additionalList;
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
