package cn.lv.jewelry.activity.daoBean;

/**
 * Created by Administrator on 2017/9/14 0014.
 */
public enum ActivityReviewType {
    //0待审核 1通过 2未通过 3删除
    PENDING_REVIEW(0), BY(1), DID_NOT_PASS(2), DELETE(3),
    //0全部 1推首页 2置顶 3推分栏 4置顶分栏 5保留
    ALL(0), PUSH_INDEX(1), STICKY(2), PUSH_COLUMNS(3), STICKY_COLUMNS(4), KEEP_IT(5),
    //0未推首页 1推首页
    NOT_PUSH_INDEX(0), IS_PUSH_INDEX(1),
    //0未推分栏 1推分栏
    NOT_PUSH_COLUMNS(0), IS_PUSH_COLUMNS(1),
    //0未删除 1已删
    NOT_DELETED(0),DELETED(1),
    //0不在首页显示 1在首页显示
    NOT_ON_THE_FRONT_PAGE(0), SHOW_ON_THE_FRONT_PAGE(1)
    ;

    ActivityReviewType(Integer type)
            {
        this.type = type;
            }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
