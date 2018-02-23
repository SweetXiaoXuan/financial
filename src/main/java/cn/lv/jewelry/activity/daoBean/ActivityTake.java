package cn.lv.jewelry.activity.daoBean;

import cn.lv.jewelry.index.indexActivity.frontBean.TakeActivityBean;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/19.
 * 活动举办
 */
@Table(name = "activity_take")
@Entity
public class ActivityTake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int aid;
    // 用户权限id
    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private ActivityPrivilege privilegeId;
    @Column(name = "insert_time")
    private long insertTime;
    private int status;
    private Integer level;

    public ActivityTake(){}
    public ActivityTake(TakeActivityBean takeActivityBean){
        this.aid = takeActivityBean.getAid();
        Integer eid = takeActivityBean.getEid();
        ActivityPrivilege privilege = new ActivityPrivilege();
        privilege.setId(Long.parseLong(eid.toString()));
        this.privilegeId = privilege;
        this.insertTime = takeActivityBean.getTime();
        this.status = takeActivityBean.getStatus();
        this.level = takeActivityBean.getLevel();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(long insertTime) {
        this.insertTime = insertTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }
}
