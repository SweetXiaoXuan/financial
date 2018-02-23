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
 * Created by lixing on 16/3/19.
 * 活动参与表
 */
@Table(name = "activity_attence")
@Entity
public class ActivityAttence {
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
    private long status;
    @Column(name = "attence_time",nullable = false)
    private Long attenceTime;
    // 自我介绍
    @Column(name = "self_introduce")
    private String selfIntroduce;
    private String extend;

    public Activity getAid() {
        return aid;
    }

    public void setAid(Activity aid) {
        this.aid = aid;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Long getAttenceTime() {
        return attenceTime;
    }

    public void setAttenceTime(Long attenceTime) {
        this.attenceTime = attenceTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getSelfIntroduce() {
        return selfIntroduce;
    }

    public void setSelfIntroduce(String selfIntroduce) {
        this.selfIntroduce = selfIntroduce;
    }

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }
}
