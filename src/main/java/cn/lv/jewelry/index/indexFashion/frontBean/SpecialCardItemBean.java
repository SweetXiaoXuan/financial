package cn.lv.jewelry.index.indexFashion.frontBean;

import javax.ws.rs.FormParam;
import java.io.Serializable;

/**
 * Created by lixing on 16/4/7.
 */
public class SpecialCardItemBean implements Serializable{
    private long id;
    @FormParam("sid")
    private long sid;
    @FormParam("rid")
    private long rid;
    @FormParam("cardType")
    private int cardType;
    private String name;
    @FormParam("coverUrl")
    private String coverUrl;
    @FormParam("path")
    private String path;
    private String description;
    private int status;
    private Object extend;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
