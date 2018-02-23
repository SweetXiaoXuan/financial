package cn.com.ql.wiseBeijing.user.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.user.daoBean.MyInfo;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
@Component
public class MyInfoDao extends BasicDao<MyInfo> {
    @Bean
    public MyInfoDao myInfoDao() {
        return new MyInfoDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(MyInfoDao.class);

    /**
     * 查询用户的信息
     * @param pid 用户权限id
     * @return cn.com.ql.wiseBeijing.user.daoBean.MyInfo
     * @author liumengwei
     * @Date 2017/10/15
     */
    public MyInfo getMyInfo(Long pid) {
        Session session = getSession();
        Object object = null;
        try {
            String sql =
                    " from MyInfo where privilegeId = :pid";
            object = session.createQuery(sql)
                    .setLong("pid", pid)
                    .uniqueResult()
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (MyInfo) object;
    }

}
