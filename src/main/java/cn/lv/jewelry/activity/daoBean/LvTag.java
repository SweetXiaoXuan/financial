package cn.lv.jewelry.activity.daoBean;

import javax.persistence.*;

/**
 * Created by lixing on 16/3/19.
 */
@Table(name = "lv_tag")
@Entity
public class LvTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long rid;
    private int category;
    private int tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}