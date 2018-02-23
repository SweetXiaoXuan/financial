package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/23.
 */
@Table(name = "activity_relative_activity")
@Entity
public class RelativeActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "m_aid")
    private Activity maid;//主活动id
    @ManyToOne
    @JoinColumn(name = "r_aid")
    private Activity raid;//相关活动id
    private int status;
    private String extend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Activity getMaid() {
        return maid;
    }

    public void setMaid(Activity maid) {
        this.maid = maid;
    }

    public Activity getRaid() {
        return raid;
    }

    public void setRaid(Activity raid) {
        this.raid = raid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
