package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductToProduct;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductDao extends BasicDao<ProductToProduct>{
	@Bean
	public ProductToProductDao productToProductDao(){
		return new ProductToProductDao();
	}


}
