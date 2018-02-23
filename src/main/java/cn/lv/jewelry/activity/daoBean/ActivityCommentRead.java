package cn.lv.jewelry.activity.daoBean;

import cn.com.ql.wiseBeijing.dao.BasicDao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
@Table(name="activity_comment_read")
@Entity
public class ActivityCommentRead extends BasicDao<ActivityComment>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 用户权限id
    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private ActivityPrivilege privilegeId;
    @ManyToOne
    @JoinColumn(name = "cid")
    private ActivityComment cid;
    @Column(name = "read_time")
    private String readTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActivityComment getCid() {
        return cid;
    }

    public void setCid(ActivityComment cid) {
        this.cid = cid;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }
}
