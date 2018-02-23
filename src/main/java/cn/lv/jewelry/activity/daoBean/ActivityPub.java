package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by liumengwei on 2017/8/19 0019.
 * 活动发布
 */
@Table(name = "activity_pub")
@Entity
public class ActivityPub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "aid")
    private Activity aid;
    @ManyToOne
    @JoinColumn(name = "eid")
    private ActivityPrivilege eid;
    @Column(name="insert_time")
    private BigInteger insertTime;
    private Integer status;
    private String extend;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(BigInteger insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public ActivityPrivilege getEid() {
        return eid;
    }

    public void setEid(ActivityPrivilege eid) {
        this.eid = eid;
    }

    public Activity getAid() {
        return aid;
    }

    public void setAid(Activity aid) {
        this.aid = aid;
    }
}
