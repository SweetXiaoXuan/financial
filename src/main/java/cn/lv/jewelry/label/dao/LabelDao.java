package cn.lv.jewelry.label.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.label.daobean.Label;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by 24593 on 2018/2/10.
 */
@Component
public class LabelDao extends BasicDao<Label> {
    @Bean
    public LabelDao labelDao() {
        return new LabelDao();
    }
}
