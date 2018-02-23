package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityCommentPic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Component
public class ActivityCommentPicDao extends BasicDao<ActivityCommentPic> {
    @Bean
    public ActivityCommentPicDao activityCommentPicDao() {
        return new ActivityCommentPicDao();
    }
}