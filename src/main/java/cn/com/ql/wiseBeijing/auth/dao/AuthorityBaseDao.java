package cn.com.ql.wiseBeijing.auth.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.Authority;
import cn.com.ql.wiseBeijing.auth.daoBean.Menu;
import cn.com.ql.wiseBeijing.dao.BasicDao;

@Component
public class AuthorityBaseDao extends BasicDao<Menu> {
	@Bean
	public AuthorityBaseDao authorityBaseDao()
	{
		return new AuthorityBaseDao();
	}
	//根据用户ID菜单列表权限
	public List<Menu> getMenu(int userId){
		Session session  = getSession();
		String hql = "select id,menu_title,menu_desc from Menu where id in (select menu_id from RelAuthMenu where auth_id in (select auth_id from RelUserAuth where user_id ="+userId+"))";
		Query query = session.createQuery(hql);
	    return query.list();
	}
	//根据用户取得权限列表
	public List<Authority> getAuthority(int userId){
		Session session  = getSession();
		String hql = "select * from authority where id in (select auth_id from rel_u_auth where user_id ="+userId+")";
		Query query = session.createQuery(hql);
	    return query.list();
	}
	//根据权限获取菜单列表
	public List<Menu> getAuthMenu(int authId){
		Session session  = getSession();
		String hql = "select id,menu_title,menu_url,createtime from Menu where id in (select menu_id from RelAuthMenu where auth_id = "+authId+")";
		return session.createQuery(hql).list();
	}
	public boolean isMenu(String userId,String url){
		Session session  = getSession();
		String hql = "select id,menu_title,menu_url from Menu where id in (select menu_id from rel_auth_menu where auth_id in (select auth_id from rel_u_auth where user_id ="+userId+")) ";
		Query query2  = session.createSQLQuery(hql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query2.list();
		//进行地址检查
		for(Map<String, Object> map:list){
			if(url.contains(map.get("menu_url").toString().trim())){
				return true;
			}
		}
		return false;
	}
	public int saveAuth(Authority authority){
		return   (Integer) getSession().save(authority);
	}
	public int deleteAuth(Authority authority){
		try {
			getSession().delete(authority);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}
	public int saveMenu(Menu menu){
		return (Integer) getSession().save(menu);
	}
	public int deleteMenu(Menu menu){
		try {
			getSession().delete(menu);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}
	public int updateAuth(Authority authority){
		try {
			getSession().update(authority);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}
	public int updateMenu(Menu menu){
		try {
			getSession().update(menu);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}
	
}
