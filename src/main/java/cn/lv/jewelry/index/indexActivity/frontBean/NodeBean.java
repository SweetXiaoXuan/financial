package cn.lv.jewelry.index.indexActivity.frontBean;

/**
 * 这个bean在正式数据出来前,会被弃用
 */
public class NodeBean {
	private String path;
	private String description;
	private String id;
	private String status;
	private ActivityBean detail;
	
	public ActivityBean getDetail() {
		return detail;
	}

	public void setDetail(ActivityBean detail) {
		this.detail = detail;
	}
	public final static String pre_path="http://img.qimiaodian.com:8088/image_news/lv/";
	

	public static NodeBean getInstance(String id,String path,String description,String status)
	{
		NodeBean nb=new NodeBean(id,path,description,status);
		return nb;
	}
	public static NodeBeanVideo getInstance(String id,String path,String description,String img,String status)
	{
		return new NodeBeanVideo(id,path,description,img,status);
	}
	public NodeBean(String id,String path,String description,String status)
	{
		this.path=path;
		this.id=id;
		this.description=description;
		this.status=status;
	}
	public NodeBean(){}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
