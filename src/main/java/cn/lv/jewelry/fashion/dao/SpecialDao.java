package cn.lv.jewelry.fashion.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.fashion.daoBean.Special;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecialDao extends BasicDao<Special> {

    @Bean
    public SpecialDao specialDao() {
        return new SpecialDao();
    }

    public Page<Special> getList(int current) {
        Session session = getSession();
        String hql = "from Special order by id desc";
        Query query = session.createQuery(hql);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Special> page = pageWaterfallAdapter.execute(current, 20, new ResultTransfer<Special>() {
            @Override
            public List<Special> transfer(List list) {
                return list;
            }
        });
        return page;
    }
}
