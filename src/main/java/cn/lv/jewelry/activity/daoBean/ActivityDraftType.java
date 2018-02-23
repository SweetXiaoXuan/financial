package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/9/10 0010.
 */
public enum ActivityDraftType {
    //1-文字，2-图片，3-直播，4-视频，5-分享过来的评论, 6-公告：公告的留言属于评论的评论
    FALSE(0), TRUE(1);

    ActivityDraftType(Integer type) {this.type = type;}
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
