package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityCircle;
import cn.lv.jewelry.activity.daoBean.ActivityCommentComment;
import cn.lv.jewelry.activity.daoBean.ActivityCommentStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Component
public class ActivityCircleDao extends BasicDao<ActivityCircle> {
    @Bean
    public ActivityCircleDao activityCircleDao() {
        return new ActivityCircleDao();
    }

}