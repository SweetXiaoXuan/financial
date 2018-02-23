package cn.lv.jewelry.index.indexActivity.frontBean;

import javax.ws.rs.FormParam;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
public class ActivityCommentBean {
    private  List<Map<String, Object>> OrganizerInfo;
    @FormParam("uid")
    private String uid;
    @FormParam("username")
    private String username;
    @FormParam("headPic")
    private String headPic;
    @FormParam("url")
    private String url;
    @FormParam("pic")
    private String pic;
    @FormParam("commentTime")
    private Long commentTime;
    @FormParam("text")
    private String text;
    @FormParam("pid")
    private Long pid;
    @FormParam("type")
    private Integer type;
    @FormParam("graphicType")
    private Integer graphicType;
    private Integer commentNumber;
    private Boolean hasNext;
    private List<Map<String, Object>> comments;
    private Map<String, Object> graphic;
    // 点赞
    private Integer likesNum;
    private List<Map<String, Object>> listLikes;
    @FormParam("activityId")
    private Long activityId;
    @FormParam("cid")
    private Long cid;
    @FormParam("content")
    private String content;

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public Long getCommentTime() {
        return commentTime;
    }
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
    public String getHeadPic() {
        return headPic;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public  List<Map<String, Object>> getComments() {
        return comments;
    }

    public void setComments( List<Map<String, Object>> comments) {
        this.comments = comments;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }
    public Integer getType() {
        return type;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
    }

    public List<Map<String, Object>> getListLikes() {
        return listLikes;
    }

    public void setListLikes(List<Map<String, Object>> listLikes) {
        this.listLikes = listLikes;
    }

    public  List<Map<String, Object>> getOrganizerInfo() {
        return OrganizerInfo;
    }

    public void setOrganizerInfo(List<Map<String, Object>> organizerInfo) {
        OrganizerInfo = organizerInfo;
    }

    public Map<String, Object> getGraphic() {
        return graphic;
    }
    public String getText() {
        return text;
    }
    public String getUrl() {
        return url;
    }

    public void setGraphic(Map<String, Object> graphic) {
        this.graphic = graphic;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getGraphicType() {
        return graphicType;
    }

    public void setGraphicType(Integer graphicType) {
        this.graphicType = graphicType;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
