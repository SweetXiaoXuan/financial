package cn.com.ql.wiseBeijing.policy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ql.wiseBeijing.policy.dao.PolicyCategoryDao;
import cn.com.ql.wiseBeijing.policy.dao.PolicyItemDao;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyCategory;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyItem;
import cn.com.ql.wiseBeijing.policy.frontBean.FPolicyItem;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.util.ValidateMode;

@Component
public class PolicyService {
	@Bean
	public PolicyService policyService() {
		return new PolicyService();
	}

	@Resource(name = "policyCategoryDao")
	private PolicyCategoryDao policyCategoryDao;
	@Resource(name = "policyItemDao")
	private PolicyItemDao policyItemDao;

	@Transactional
	public ReturnValue<List<Map>> getIndexAll() {
		ReturnValue<List<Map>> r = new ReturnValue<List<Map>>();
		Page<Map> page = policyCategoryDao.getAllIndex();
		r.setObject(page.getPageContent());
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setMeg("");
		return r;
	}

	@Transactional
	public ReturnValue<Map> saveFirstLevel(String title, String logo,String url,
			boolean hasNextTitle) {
		ReturnValue<Map> r = new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		PolicyCategory category = new PolicyCategory();
		category.setStatus("0");
		category.setPubstatus("0");
		category.setTitle(title);
		category.setListimage(logo);
		category.setClevel(0);
		category.setUrls(url);
		if (hasNextTitle)
			category.setNext(1);
		else
			category.setNext(0);
		category.setParent_id(0);
		String id = policyCategoryDao.save(category).toString();
		Map m = new HashMap();
		m.put("id", id);
		r.setObject(m);
		return r;
	}

	@Transactional
	public ReturnValue<Map> saveSecondLevel(String title, int parent_id) {
		ReturnValue<Map> r = new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		PolicyCategory category = new PolicyCategory();
		category.setStatus("0");
		category.setPubstatus("0");
		category.setParent_id(parent_id);
		category.setNext(0);
		category.setTitle(title);
		category.setClevel(1);
		String id = policyCategoryDao.save(category).toString();
		Map m = new HashMap();
		m.put("id", id);
		r.setObject(m);
		return r;
	}

	@Transactional
	public ReturnValue<Map> saveDetailLevel(FPolicyItem fitem, int parent_id) {
		ReturnValue<Map> r = new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		PolicyItem item = new PolicyItem();
		item.setComment_flag(fitem.getComment_flag());
		item.setShare_flag(fitem.getShare_flag());
		item.setSort_flag(fitem.getSort_flag());
		item.setContent(fitem.getContent());
		item.setTitle(fitem.getTitle());
		item.setDescription(fitem.getDescription());
		item.setKeywords(fitem.getKeywords());
		item.setUrls(fitem.getUrls());
		PolicyCategory category = policyCategoryDao.get(PolicyCategory.class,
				parent_id);
		if (category == null) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("didn't find the parent,abord");
		} else {
			item.setPid(parent_id);
			String id = policyItemDao.save(item).toString();
			Map m = new HashMap();
			m.put("id", id);
			r.setObject(m);
		}
		return r;
	}
	@Transactional
	public ReturnValue<FPolicyItem> getDetailLevel(String id) {
		FPolicyItem fitem=new FPolicyItem();
		ReturnValue<FPolicyItem> r = new ReturnValue<FPolicyItem>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		PolicyItem item = policyItemDao.get(PolicyItem.class,id);
		if (item == null) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("didn't find the parent,abord");
		} else {
			fitem.setNid(item.getNid());
			fitem.setComment_flag(item.getComment_flag());
			fitem.setShare_flag(item.getShare_flag());
			fitem.setCreatetime(item.getCreatetime());
			fitem.setPubdate(item.getPubdate());
			fitem.setKeywords(item.getKeywords());
			fitem.setUrls(item.getUrls());
			fitem.setTitle(item.getTitle());
			fitem.setDescription(item.getDescription());
			r.setObject(fitem);
		}
		return r;
	}
	@Transactional
	public ReturnValue<String> getDetailContent(String id) {
		FPolicyItem fitem=new FPolicyItem();
		ReturnValue<String> r = new ReturnValue<String>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		PolicyItem item = policyItemDao.get(PolicyItem.class,id);
		if (item == null) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("didn't find the parent,abord");
		} else {
			r.setObject(item.getContent());
		}
		return r;
	}
	@Transactional
	public ReturnValue<String> isUpdateIndex()
	{
		ReturnValue<String> r = new ReturnValue<String>();
		r.setObject(policyCategoryDao.maxTime());
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		return r;
	}
	@Transactional
	public ReturnValue<String> delete(String id,boolean isItem){
		ReturnValue<String> r = new ReturnValue<String>();
		if(isItem){
			int j= policyItemDao.del(id);
			if(j>0){
				r.setMeg("success!!");
				r.setFlag(ReturnValue.FLAG_SUCCESS);
			}else{
				r.setMeg("didn't find the id "+id);
				r.setFlag(ReturnValue.FLAG_FAIL);
			}
		}else{
			int j= policyItemDao.delete(id);
			int i =policyCategoryDao.delete(id);
			if(i+j>0){
				r.setMeg("success!!");
				r.setFlag(ReturnValue.FLAG_SUCCESS);
			}else{
				r.setMeg("didn't find the id "+id);
				r.setFlag(ReturnValue.FLAG_FAIL);
			}
		}
		return r;
	}
	@Transactional
	public ReturnValue<Map<String, Object>> get(String id,boolean isItem){
		ReturnValue<Map<String, Object>> returnValue = new ReturnValue<>();
		Map<String, Object> map = new HashMap<>();
		if(isItem){
			PolicyItem item = policyItemDao.get(id+"");
			map.put("title", item.getTitle());
			map.put("description", item.getDescription());
			map.put("keywords",item.getKeywords());
			map.put("id", item.getNid());
		}else{
			PolicyCategory item = policyCategoryDao.get(id+"");
			map.put("title", item.getTitle());
			map.put("description", item.getDescription());
			map.put("keywords",item.getKeywords());
			map.put("id", item.getId());
		}
		returnValue.setObject(map);
		returnValue.setMeg("success!!");
		returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
		return returnValue;
	}
	@Transactional
	public ReturnValue<String> getContent(String id){
		ReturnValue<String> rv =new ReturnValue<>();
		rv.setObject(policyItemDao.get(id).getContent());
		rv.setMeg("success!!");
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}
	@Transactional
	public void update(String id,String title,String description,String keywords,String content,boolean isItem){
		if(isItem){
			PolicyItem item = policyItemDao.get(id);
			if(!ValidateMode.isNull(content)){
				item.setContent(content);
			}
			if(!ValidateMode.isNull(description)){
				item.setDescription(description);
			}
			if(!ValidateMode.isNull(keywords)){
				item.setKeywords(keywords);
			}
			if(!ValidateMode.isNull(title)){
				item.setTitle(title);
			}
			policyItemDao.update(item);
		}else{
			PolicyCategory category = policyCategoryDao.get(id);
			if(!ValidateMode.isNull(description)){
				category.setDescription(description);
			}
			if(!ValidateMode.isNull(keywords)){
				category.setKeywords(keywords);
			}
			if(!ValidateMode.isNull(title)){
				category.setTitle(title);
			}
			policyCategoryDao.update(category);
		}
	}
}
