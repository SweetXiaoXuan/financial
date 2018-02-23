package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;

/**
 * 图片的通用bean
 */
public class NodeBeanImage extends NodeBean{
	public String path_small;
	public String path_big;
	public String id;
	//图片的保存路径
	@FormParam("path")
	private String path;
	//图片的描述
	@FormParam("description")
	private String description;
	//图片相关联的对象id
	@FormParam("rid")
	private String rid;
	//图片的类型
	@FormParam("type")
	private String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath_small() {
		return path_small;
	}
	public void setPath_small(String path_small) {
		this.path_small = path_small;
	}
	public String getPath_big() {
		return path_big;
	}
	public void setPath_big(String path_big) {
		this.path_big = path_big;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}
}
