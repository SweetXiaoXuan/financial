package cn.com.ql.wiseBeijing.auth.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.Authority;
import cn.com.ql.wiseBeijing.dao.BasicDao;
@Component
public class AuthDao extends BasicDao<Authority>{
	public List<Authority> getList(){
		return getSession().createQuery("from Authority").list();
	}
	public boolean grantAuthority(String uid,String authids){
		String auth[] = authids.split(",");
		//删除所有权限
		try {
			getSession().createSQLQuery("delete from rel_u_auth where user_id = "+uid+"").executeUpdate();
			for(String str:auth){
				if(str==null||str.equals(""))continue;
				String hql = "insert into rel_u_auth(auth_id,user_id) values("+str+","+uid+")";
				getSession().createSQLQuery(hql).executeUpdate();
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
