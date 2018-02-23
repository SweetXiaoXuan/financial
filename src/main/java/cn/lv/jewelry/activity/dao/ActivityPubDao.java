package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityPub;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/8/19 0019.
 */
@Component
public class ActivityPubDao extends BasicDao<ActivityPub> {
    @Bean
    public ActivityPub activityPub() {
        return new ActivityPub();
    }
}
