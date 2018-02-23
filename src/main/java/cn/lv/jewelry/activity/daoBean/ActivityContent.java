package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/23.
 */
@Table(name = "activity_content")
@Entity
public class ActivityContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "aid")
    private Activity aid;
    private int type = ActivityContentType.TEXT.getV();
    private String content;
    private int sort;
    private String extend;
    @Column(name = "activity_status")
    private Integer activityStatus;

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
