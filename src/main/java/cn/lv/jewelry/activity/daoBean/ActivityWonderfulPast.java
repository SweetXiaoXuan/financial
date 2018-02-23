package cn.lv.jewelry.activity.daoBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "activity_wonderful_past")
@Entity
public class ActivityWonderfulPast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "aid")
    private Activity aid;
    @ManyToOne
    @JoinColumn(name = "cid")
    private ActivityComment cid;
    @Column(name = "insert_time")
    private long insertTime;
    @Column(name = "is_delete")
    private long isDelete;
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

    public ActivityComment getCid() {
        return cid;
    }

    public void setCid(ActivityComment cid) {
        this.cid = cid;
    }

    public long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(long insertTime) {
        this.insertTime = insertTime;
    }

    public long getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(long isDelete) {
        this.isDelete = isDelete;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
