package cn.lv.jewelry.product.daoBean;

import cn.lv.jewelry.brand.daoBean.Brand;

import javax.persistence.*;

/**
 * 商品 -> 类别
 * @author hy
 *
 */
@Table(name = "PRODUCT_VIDEO")
@Entity
public class ProductVideo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	@JoinColumn(name = "pid")
	private Brand pid;
	private String img;
	private String video;
	private String extend;
	private int status;


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Brand getPid() {
		return pid;
	}

	public void setPid(Brand pid) {
		this.pid = pid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

}
