package cn.lv.jewelry.label.daobean;

import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by 24593 on 2018/2/10.
 */
public class UserLabeling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "create_time")
    private String createTime;
    // 权限id
    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private ActivityPrivilege privilegeId;
    // 标签id
    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label labelId;
    // 状态 0正常 1异常
    private Integer status;
    // 是否删除 0否 1是
    @Column(name = "is_delete")
    private Integer isDelete;

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

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Label getLabelId() {
        return labelId;
    }

    public void setLabelId(Label labelId) {
        this.labelId = labelId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
