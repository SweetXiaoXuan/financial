package cn.lv.jewelry.index.indexFashion.frontBean;

import javax.ws.rs.FormParam;

/**
 * Created by lixing on 16/4/7.
 */
public class FashionItemBean {
    private String id;
    @FormParam("name")
    private String nickname;
    private String avatar;
    private String level;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
