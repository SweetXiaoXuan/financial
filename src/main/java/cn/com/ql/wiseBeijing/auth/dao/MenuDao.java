package cn.com.ql.wiseBeijing.auth.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.Menu;
import cn.com.ql.wiseBeijing.dao.BasicDao;

@Component
public class MenuDao extends BasicDao<Menu>{
	@Bean
	public MenuDao menuDao()
	{
		return new MenuDao();
	}
	public Menu getRoot(){
		return (Menu) getSession().get(Menu.class, 1);
	}
	public boolean isThrough(String userId,String url){
		Session session  = getSession();
		String hql = "select id,title,url from api where id in (select api_id from rel_menu_api where menu_id in (select menu_id from rel_auth_menu where auth_id in (select auth_id from rel_u_auth where user_id="+userId+"))) ";
		Query query2  = session.createSQLQuery(hql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query2.list();
		//进行地址检查
		for(Map<String, Object> map:list){
			if(url.contains(map.get("url").toString().trim())){
				return true;
			}
		}
		return false;
	}
	
}
