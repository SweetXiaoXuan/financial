package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductCategory;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryDao extends BasicDao<ProductCategory>{
	@Bean
	public ProductCategoryDao productCategoryDao(){
		return new ProductCategoryDao();
	}

	public List<ProductCategory> getCategory(int pageNum) {
		Session session = getSession();
		String hql = "from cn.lv.jewelry.product.daoBean.ProductCategory";
		Query query = session.createQuery(hql);
		PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
		Page<ProductCategory> pageBean = pageWaterfallAdapter.execute(pageNum, 50, new ResultTransfer<ProductCategory>() {
			@Override
			public List<ProductCategory> transfer(List list) {
				return list;
			}
		});
		return pageBean.getPageContent();
	}
}
