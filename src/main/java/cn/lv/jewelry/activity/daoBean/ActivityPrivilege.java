package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;


/**
 * Created by liumengwei on 2017/8/19 0019.
 * 活动权限表
 */
@Table(name = "activity_privilege")
@Entity
public class ActivityPrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 对应user表id，或者Enterprise表id
    @Column(name = "user_id")
    private Long uid;
    @Column(name="user_type")
    private String userType;
    @Column(name="publish_activity_privilege")
    private String publishActivityPrivilege;
    @Column(name="take_acitivity_privilege")
    private String takeAcitivityPrivilege;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPublishActivityPrivilege() {
        return publishActivityPrivilege;
    }

    public void setPublishActivityPrivilege(String publishActivityPrivilege) {
        this.publishActivityPrivilege = publishActivityPrivilege;
    }

    public String getTakeAcitivityPrivilege() {
        return takeAcitivityPrivilege;
    }

    public void setTakeAcitivityPrivilege(String takeAcitivityPrivilege) {
        this.takeAcitivityPrivilege = takeAcitivityPrivilege;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
