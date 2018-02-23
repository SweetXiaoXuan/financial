package cn.lv.jewelry.fashion.daoBean;

import javax.persistence.*;

@Table(name = "FASHION_MAN")
@Entity
public class Fashion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nickname;
	private String headpic;
	private String level;
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
