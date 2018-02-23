package cn.lv.jewelry.product.daoBean;

import javax.persistence.*;

/**
 * 商品 -> 相关活动
 * @author hy
 *
 */
@Table(name = "PRODUCT_ACTIVITY")
@Entity
public class ProductActivity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long pid;
	private long aid;
	private int status;
	private String extend;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}
}
