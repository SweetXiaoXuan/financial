package cn.com.ql.wiseBeijing.auth.frontBean;

import cn.com.ql.wiseBeijing.auth.daoBean.Api;

public class FApi {
    public FApi(Api api)
    {
        id=api.getId();
        title=api.getTitle();
        url=api.getUrl();
        info=api.getInfo();
        level=api.getLevel();
    }
    public FApi()
    {}
    private int id;
    private String title;
    private String url;
    private String info;
    private String createtime;
    private Integer level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
