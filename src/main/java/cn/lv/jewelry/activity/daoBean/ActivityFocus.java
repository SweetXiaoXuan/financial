package cn.lv.jewelry.activity.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/8/21 0021.
 */

/**
 * 活动关注表
 */
@Table(name = "activity_focus")
@Entity
public class ActivityFocus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 用户权限id
    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private ActivityPrivilege privilegeId;
    @ManyToOne
    @JoinColumn(name = "aid")
    private Activity aid;
    @Column(name = "a_status")
    private Integer aStatus;
    @Column(name = "create_time")
    private String createTime;
    private String extend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Activity getAid() {
        return aid;
    }

    public void setAid(Activity aid) {
        this.aid = aid;
    }

    public Integer getaStatus() {
        return aStatus;
    }

    public void setaStatus(Integer aStatus) {
        this.aStatus = aStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }
}
