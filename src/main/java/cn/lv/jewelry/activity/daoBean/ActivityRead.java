package cn.lv.jewelry.activity.daoBean;

import cn.com.ql.wiseBeijing.user.daoBean.User;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/19.
 */
@Table(name = "activity_read")
@Entity
public class ActivityRead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "uid")
    private User uid;
    @ManyToOne(targetEntity =Activity.class)
    @JoinColumn(name = "aid")
    private Activity aid;
    private int status;
    @Column(name = "read_time", nullable = false)
    private Long readTime;
    private String extend;

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public void setAid(Activity aid) {
        this.aid = aid;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Activity getAid() {
        return aid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }
}