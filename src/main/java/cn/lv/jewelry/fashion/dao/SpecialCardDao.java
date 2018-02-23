package cn.lv.jewelry.fashion.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.fashion.daoBean.SpecialCard;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecialCardDao extends BasicDao<SpecialCard>{

	@Bean
    public SpecialCardDao specialCardDao() {
        return new SpecialCardDao();
    }


    public List<SpecialCard> getSpeicalCards(long sid) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.fashion.daoBean.SpecialCard s WHERE s.sid=:sid ORDER BY s.id DESC";
        List<SpecialCard> specialCards = session.createQuery(hql).setLong("sid", sid).list();

        return specialCards;
    }
}
