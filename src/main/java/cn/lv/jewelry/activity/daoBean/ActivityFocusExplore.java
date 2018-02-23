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
 * 探索活动关注表
 * @author liumengwei
 * @Date 2017/10/18
 */
@Table(name = "activity_focus_explore")
@Entity
public class ActivityFocusExplore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 用户权限id
    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private ActivityPrivilege privilegeId;
    @ManyToOne
    @JoinColumn(name = "privilege_puid")
    private ActivityPrivilege privilegePuid;
    @Column(name = "create_time")
    private String createTime;
    private String extend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ActivityPrivilege getPrivilegePuid() {
        return privilegePuid;
    }

    public void setPrivilegePuid(ActivityPrivilege privilegePuid) {
        this.privilegePuid = privilegePuid;
    }
}
