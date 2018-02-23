package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Table(name = "activity_comment_pic")
@Entity
public class ActivityCommentPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "cid")
    private ActivityComment cid;
    @Column(name = "comment_content")
    private String commentContent;
    @Column(name = "a_status")
    private Integer aStatus;
    @Column(name = "comment_status")
    private Integer commentStatus;
    @Column(name = "comment_time")
    private Long commentTime;
    private String extend;

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

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getaStatus() {
        return aStatus;
    }

    public void setaStatus(Integer aStatus) {
        this.aStatus = aStatus;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public ActivityComment getCid() {
        return cid;
    }

    public void setCid(ActivityComment cid) {
        this.cid = cid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}