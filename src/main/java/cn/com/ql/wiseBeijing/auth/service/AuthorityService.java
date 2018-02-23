package cn.com.ql.wiseBeijing.auth.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.com.ql.wiseBeijing.auth.daoBean.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ql.wiseBeijing.auth.dao.ApiDao;
import cn.com.ql.wiseBeijing.auth.dao.AuthDao;
import cn.com.ql.wiseBeijing.auth.dao.MangerDao;
import cn.com.ql.wiseBeijing.auth.dao.MenuDao;
import cn.com.ql.wiseBeijing.auth.dao.UserApiDao;
import cn.com.ql.wiseBeijing.auth.daoBean.Manger;
import cn.com.ql.wiseBeijing.auth.daoBean.Menu;
import cn.com.ql.wiseBeijing.auth.daoBean.UserApi;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;

@Component
public class AuthorityService {
    @Resource(name = "menuDao")
    private MenuDao mDao;
    @Autowired
    private MangerDao mangerDao;
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private UserApiDao uApiDao;

    @Bean
    public AuthorityService authorityService() {
        return new AuthorityService();
    }

    @Transactional
    public ReturnValue<Menu> getRootMenu() {
        ReturnValue<Menu> rv = new ReturnValue<>();
        rv.setObject(mDao.getRoot());
        return rv;
    }

    @Transactional
    public boolean isThrough(String userId, String url) {
        return apiDao.isThrough(url, userId);
    }

    /**
     * url信息
     *
     * @param url
     * @return
     */
    public Api apiInfo(String url) {
        return apiDao.apiInfo(url);
    }

    @Transactional
    public Map<String, Object> isUserThrough(String url, String userId) {
        return uApiDao.isThrough(url, userId);
    }

    @Transactional
    public List<Map<String, Object>> getUApiList() {
        return uApiDao.getList();
    }

    @Transactional
    public UserApi updateStatus(String id, String msg, String status) {
        return uApiDao.updateStatus(id, msg, status);
    }

    @Transactional
    public int addManger(Manger manger) {
        int id = mangerDao.addManger(manger);
        authDao.grantAuthority(id + "", "3");
        return mangerDao.addManger(manger);
    }

    @Transactional
    public ReturnValue<Map> getList() {
        ReturnValue<Map> rv = new ReturnValue<Map>();
        try {
            rv.setObject(PageMapUtil.getMap(mangerDao.getList()));
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception e) {
            // TODO: handle exception
            rv.setMeg(e.getMessage());
            rv.setFlag(ReturnValue.FLAG_FAIL);
        }
        return rv;
    }

    @Transactional
    public boolean isRename(String username) {
        return mangerDao.isRename(username);
    }

    @Transactional
    public Manger login(String username, String password) {
        return mangerDao.login(username, password);
    }

    @Transactional
    public boolean grantAuthority(String uid, String authids) {
        return authDao.grantAuthority(uid, authids);
    }

    @Transactional
    public ReturnValue deleteManger(int mid) {
        ReturnValue rv = new ReturnValue();
        int update = mangerDao.delete(mid);
        if (update > 0) {
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
            rv.setMeg("success");
        } else {
            rv.setFlag(ReturnValue.FLAG_FAIL);
            rv.setMeg("no find the id is " + mid);
        }
        return rv;
    }
}
