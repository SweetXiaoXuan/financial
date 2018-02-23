package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.BeanParam;

public class ActivityRelativeProduct {
    @BeanParam
	private String id;
	private String description;
	private String price;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
