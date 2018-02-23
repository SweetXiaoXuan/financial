package cn.lv.jewelry.index.indexProduct.frontBean;

import cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixing on 16/4/7.
 */
public class PopularItemBean {

    private String id="1";
    private String cover_url="1";
    private String title="1";
    private String sub_title="1";
    private String description = "1";
    //pic list
    private List<String> crafts = new ArrayList<String>(){{add("1");add("2");}};
    private String fashionMan="1";
    //video object
    private String video="1";
    private ActivityBean activityBean;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getFashionMan() {
        return fashionMan;
    }

    public void setFashionMan(String fashionMan) {
        this.fashionMan = fashionMan;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ActivityBean getActivityBean() {
        return activityBean;
    }

    public void setActivityBean(ActivityBean activityBean) {
        this.activityBean = activityBean;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCrafts() {
        return crafts;
    }

    public void setCrafts(List<String> crafts) {
        this.crafts = crafts;
    }
}
