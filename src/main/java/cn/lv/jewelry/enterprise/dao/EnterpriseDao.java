package cn.lv.jewelry.enterprise.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilegeStatus;
import cn.lv.jewelry.enterprise.daoBean.Enterprise;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class EnterpriseDao extends BasicDao<Enterprise> {
    @Bean
    public EnterpriseDao enterpriseDao()
    {
        return new EnterpriseDao();
    }

    public List<Map<String, Object>> getOrganizerInfo() {
        Session session = getSession();
        String sql =
                "select e.id as id, e.name as name, e.logo_pic as logoPic" +
                        " from enterprise e LEFT JOIN activity_privilege p " +
                        "on p.user_id = e.id " +
                        "where p.user_type = :userType";
        List<Map<String, Object>> query = session.createSQLQuery(sql)
                .setInteger("userType", ActivityPrivilegeStatus.ENTERPRISE.getStatus())
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();

        return query;
    }

    /**
     * 根据举办方名称查询
     * @param name 举办方名称
     * @return cn.lv.jewelry.enterprise.daoBean.Enterprise
     * @author liumengwei
     * @date 2017-11-5
     */
    public Enterprise getEnterprise(String name) {
        Session session = getSession();
        String hql =
                "from cn.lv.jewelry.enterprise.daoBean.Enterprise where name = '" + name + "'";
        Object query = session.createQuery(hql)
                .uniqueResult();

        return (Enterprise) query;
    }
}
