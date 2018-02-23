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
 * Created by Administrator on 2017/8/23 0023.
 */
@Table(name = "activity_comment")
@Entity
public class ActivityComment {
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
    @Column(name = "comment_type")
    private Integer commentType;
    @Column(name = "comment_status")
    private Integer commentStatus;
    @Column(name = "comment_time")
    private Long commentTime;
    @Column(name = "a_status")
    private Integer aStatus;
    @Column(name = "is_delete")
    private Integer isDelete;
    private String extend;
    private Integer comment;

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

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getaStatus() {
        return aStatus;
    }

    public void setaStatus(Integer aStatus) {
        this.aStatus = aStatus;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public ActivityPrivilege getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(ActivityPrivilege privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}