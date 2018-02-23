package cn.lv.jewelry.index.indexProduct.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by lixing on 16/4/7.
 */
public class ProductToProductItemBean {

    private String id;
    @FormParam("mid")
    private String mid;
    @FormParam("rid")
    private String rid;
    @FormParam("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

