package cn.lv.jewelry.index.indexFashion.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by lixing on 16/4/7.
 */
public class SpecialItemBean {
    private String id;
    @FormParam("description")
    private String description;
    @FormParam("lid")
    private String lid = "1";
    @FormParam("coverUrl")
    private String coverUrl;
    @FormParam("subject")
    private String subject;
    @FormParam("birthTime")
    private long birthTime;
    @FormParam("tags")
    private String[] tags;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
