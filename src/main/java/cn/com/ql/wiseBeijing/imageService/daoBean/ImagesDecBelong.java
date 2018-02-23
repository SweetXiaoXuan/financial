package cn.com.ql.wiseBeijing.imageService.daoBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;

@Table(name = "imagesbelong")
@Entity
public class ImagesDecBelong {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="newsid")
	private DecMainNews newsid;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="imagesid")
	private Images imagesid;
	private String belong_category="dec";
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public DecMainNews getNewsid() {
		return newsid;
	}

	public void setNewsid(DecMainNews newsid) {
		this.newsid = newsid;
	}

	public Images getImagesid() {
		return imagesid;
	}

	public void setImagesid(Images imagesid) {
		this.imagesid = imagesid;
	}

	public String getBelong_category() {
		return belong_category;
	}

	public void setBelong_category(String belong_category) {
		this.belong_category = belong_category;
	}
}
