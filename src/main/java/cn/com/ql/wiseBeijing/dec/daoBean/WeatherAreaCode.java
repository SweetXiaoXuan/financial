package cn.com.ql.wiseBeijing.dec.daoBean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.com.ql.wiseBeijing.dec.util.PrimaryKeyGenerator;

@Table(name="weather_area")
@Entity
public class WeatherAreaCode {
	@Id
	@GenericGenerator(name="news_key",strategy=PrimaryKeyGenerator.NEWS_PRIMARY_KEY)
	@GeneratedValue(generator="news_key")
	private String areaid;
	private String nameen;
	private String namecn;
	private String describeen;
	private String describecn;
	private String	proen;
	private String procn;
	private String nationen;
	private String nationcn;
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getNameen() {
		return nameen;
	}
	public void setNameen(String nameen) {
		this.nameen = nameen;
	}
	public String getNamecn() {
		return namecn;
	}
	public void setNamecn(String namecn) {
		this.namecn = namecn;
	}
	public String getDescribeen() {
		return describeen;
	}
	public void setDescribeen(String describeen) {
		this.describeen = describeen;
	}
	public String getDescribecn() {
		return describecn;
	}
	public void setDescribecn(String describecn) {
		this.describecn = describecn;
	}
	public String getProen() {
		return proen;
	}
	public void setProen(String proen) {
		this.proen = proen;
	}
	public String getProcn() {
		return procn;
	}
	public void setProcn(String procn) {
		this.procn = procn;
	}
	public String getNationen() {
		return nationen;
	}
	public void setNationen(String nationen) {
		this.nationen = nationen;
	}
	public String getNationcn() {
		return nationcn;
	}
	public void setNationcn(String nationcn) {
		this.nationcn = nationcn;
	}
	
}
