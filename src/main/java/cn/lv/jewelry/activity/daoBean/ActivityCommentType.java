package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
public enum ActivityCommentType {
    //1-文字，2-图片，3-直播，4-视频，5-分享过来的评论, 6-公告：公告的留言属于评论的评论 7-已删除， 8-分享过来的活动
    TEXT(1), PIC(2), LIVE(3), VIDEO(4), SHARECOMMENTS(5), ANNOUNCEMENT(6), IS_DELETE(7), FORWARDING_ACTIVIGTIES(8),
    // 0详情页 1活动页
    DETAILS(0), ACTIVITY(1);

    ActivityCommentType(Integer type) {this.type = type;}
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
