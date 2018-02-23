package cn.com.ql.wiseBeijing.auth.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.auth.daoBean.UserApi;
import cn.com.ql.wiseBeijing.dao.BasicDao;

@Component
public class UserApiDao extends BasicDao<UserApi>{
 
//验证该用户是否具有API 权限
	public Map<String,Object> isThrough(String url,String userId){
		Map<String, Object> backMap = new HashMap<>();
	    String hql = "select * from user_api";
		Query query= getSession().createSQLQuery(hql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);		
		List<Map<String, Object>> list = query.list();
		for(Map<String,Object> map:list){
			 if(url.contains(map.get("url").toString())){
				 backMap = map;
				 if(map.get("status")==null){
					 map.put("isThrough", true);
					 return map;
				 }
				 if(map.get("status").toString().equals("0"))
				 {
					 if(userId==null){
						 map.put("isThrough", true);
						 return map;
					 }
					 if(map.get("notthrough")==null){
						 map.put("isThrough", true);
						 return map;
					 }
					 else if(map.get("notthrough").toString().contains(userId)){
						 map.put("isThrough", false);
						 return map;
					 }
					 map.put("isThrough", true);
					 return map;
				 }
				 else if (map.get("status").toString().equals("1")){
					 if(userId==null){
						 map.put("isThrough", false);
						 return map;
					 }
					 if(map.get("through")==null){
						 map.put("isThrough", false);
						 return map;
					 }
					 else if(map.get("through").toString().contains(userId)){
						 map.put("isThrough", true);
						 return map;
					 }
					 map.put("isThrough", false);
					 return map;
				 }
			 }
		}
		return backMap;
	}
	public List<Map<String,Object>> getList(){
		String hql = "select * from user_api";
		Query query= getSession().createSQLQuery(hql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);		
		List<Map<String, Object>> list = query.list();
		for(Map<String, Object> map:list){
			map.remove("createtime");
		}
		return list;
	}
	public UserApi updateStatus(String id ,String msg,String status){
		String hql ="update UserApi set status=:status,msg=:msg where id =:id";
		Query query= getSession().createQuery(hql);
		query.setString("status", status);
		query.setString("msg", msg);
		query.setString("id", id);
		query.executeUpdate();
		return get(UserApi.class, Integer.parseInt(id));
	}
}
