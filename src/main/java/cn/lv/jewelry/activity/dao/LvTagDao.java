package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.LvTag;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class LvTagDao extends BasicDao<LvTag> {
    @Bean
    public LvTagDao lvTagDao() {
        return new LvTagDao();
    }


    public void save(long id, String tag, int type) {
        LvTag lvTag = new LvTag();
        lvTag.setRid(id);
        lvTag.setCategory(type);
        lvTag.setTag(Integer.parseInt(tag));
        save(lvTag);
    }

    public Page<LvTag> getLvTagBytag(int type, int tag, int page) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.activity.daoBean.LvTag l where l.category=:type and l.tag=:tag";
        Query query = session.createQuery(hql);
        query.setInteger("type", type);
        query.setLong("tag", tag);

        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<LvTag> pages = pageWaterfallAdapter.execute(page, 20, new ResultTransfer<LvTag>() {
            @Override
            public List<LvTag> transfer(List list) {
                return list;
            }
        });
        return pages;
    }
}
