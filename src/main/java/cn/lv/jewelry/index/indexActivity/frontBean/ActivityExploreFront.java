package cn.lv.jewelry.index.indexActivity.frontBean;

import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.com.ql.wiseBeijing.user.daoBean.User;
import cn.com.ql.wiseBeijing.user.daoBean.UserBind;

import javax.persistence.Entity;
import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixing on 16/3/19.
 * 发布探索活动
 */
@Entity
public class ActivityExploreFront {

    @FormParam("subject")
    private String subject;
    @FormParam("indexPic")
    private String indexPic;
    @FormParam("content")
    private String content;
    @FormParam("indexPicIsShow")
    private Integer indexPicIsShow;
    @FormParam("headPic")
    private String headPic;
    @FormParam("draft")
    private Integer draft;
    @FormParam("status")
    private Integer status;
    @FormParam("videoImageIndex")
    private String videoImageIndex = "";
    @FormParam("videoImageLogo")
    private String videoImageLogo = "";

    public Map<String, Object> getMap(){
        return new HashMap<String, Object>(){
            {
                put("subject", getSubject());
                put("indexPic", getIndexPic());
                put("indexPicIsShow", getIndexPicIsShow());
                put("content", getContent());
                put("headPic", getHeadPic());
                put("draft", getDraft());
            }
        };
    }

    public Map<String, Object> getCmsMap(final User user, final UserBind userBind){
        return new HashMap<String, Object>(){
            {
                put("subject", getSubject());
                put("indexPic", getIndexPic());
                put("indexPicIsShow", getIndexPicIsShow());
                put("content", getContent());
                put("headPic", getHeadPic());
                put("draft", getDraft());
                String username = "";
                String nickname = "";
                if (user != null) {
                    username = user.getUsername();
                } else {
                    nickname = userBind.getUsername();
                }
                if ("admin".equals(StringUtil.isEmpty(username) ? nickname : username)) {
                    put("videoImageIndex", StringUtil.isEmpty(getVideoImageIndex()) ? "" : getVideoImageIndex());
                    put("videoImageLogo", StringUtil.isEmpty(getVideoImageLogo()) ? "" : getVideoImageLogo());
                }
            }
        };
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public Integer getIndexPicIsShow() {
        return indexPicIsShow;
    }

    public void setIndexPicIsShow(Integer indexPicIsShow) {
        this.indexPicIsShow = indexPicIsShow;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVideoImageLogo() {
        return videoImageLogo;
    }

    public void setVideoImageLogo(String videoImageLogo) {
        this.videoImageLogo = videoImageLogo;
    }

    public String getVideoImageIndex() {
        return videoImageIndex;
    }

    public void setVideoImageIndex(String videoImageIndex) {
        this.videoImageIndex = videoImageIndex;
    }
}
