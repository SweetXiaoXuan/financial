package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;

public class NodeBeanVideo extends NodeBean {

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @FormParam("path")
    private String path;
    private String description;
    private String id;
    private String status;
    @FormParam("coverUrl")
    private String img;

    public String getImg() {
        return img;
    }


    public void setImg(String img) {
        this.img = img;
    }

    public NodeBeanVideo() {
    }

    public NodeBeanVideo(String id, String path, String description, String img, String status) {
        this.img = img;
        this.path = path;
        this.id = id;
        this.description = description;
        this.status = status;
    }

}
