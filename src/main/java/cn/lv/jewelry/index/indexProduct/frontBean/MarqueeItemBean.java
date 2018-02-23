package cn.lv.jewelry.index.indexProduct.frontBean;

/**
 * Created by lixing on 16/4/8.
 * 用来放精品首页跑马灯元素
 */
public class MarqueeItemBean<T> {
    private String id="1";
    private String cover_url="1";
    private String type="1";
    private T extention;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getExtention() {
        return extention;
    }

    public void setExtention(T extention) {
        this.extention = extention;
    }
}
