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
public class ProductBrandDao extends BasicDao<ProductCategory>{
	@Bean
	public ProductBrandDao productBrandDao(){
		return new ProductBrandDao();
	}


}
