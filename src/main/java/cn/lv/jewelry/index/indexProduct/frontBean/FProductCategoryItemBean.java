package cn.lv.jewelry.index.indexProduct.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by lixing on 16/4/7.
 */
public class FProductCategoryItemBean {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String id;
    @FormParam("name")
    private String name;
    @FormParam("status")
    private int status;


}
