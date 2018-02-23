package cn.com.ql.wiseBeijing.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.Api;
import cn.com.ql.wiseBeijing.dao.BasicDao;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApiDao extends BasicDao<Api> {
    private static Logger logger = LogManager.getLogger(ApiDao.class);

    //验证该用户是否具有API 权限
    public boolean isThrough(String url, String userId) {
        String hql = "select id,title,url from api where id in (select api_id from rel_auth_api where auth_id in (select auth_id from rel_u_auth where user_id=" + userId + "))";
        Query query = getSession().createSQLQuery(hql).setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        for (Map<String, Object> map : list) {
            if (url.contains(map.get("url").toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证URL是否可以通过
     *
     * @param url
     * @return
     */
    @Transactional
    public Api apiInfo(String url) {
        String hql = "from Api where url=:url";
        Query query = getSession().createQuery(hql);
        query.setString("url", url);
        try {
            Object o = query.uniqueResult();
            if (o != null) {
                Api api = (Api) query.uniqueResult();
                return api;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("URL 异常", ex);
            return null;
        }
    }

    public boolean grantApi(String authid, String apids) {

        return false;
    }

}