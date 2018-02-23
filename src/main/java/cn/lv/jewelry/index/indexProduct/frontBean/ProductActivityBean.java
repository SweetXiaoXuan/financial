package cn.lv.jewelry.index.indexProduct.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductActivityBean {

    private String id;
    @FormParam("pid")
    private String pid;
    @FormParam("aid")
    private String aid;
    @FormParam("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

