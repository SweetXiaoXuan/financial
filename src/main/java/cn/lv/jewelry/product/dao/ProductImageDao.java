package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductImage;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProductImageDao extends BasicDao<ProductImage>{
	@Bean
	public ProductImageDao productImageDao(){
		return new ProductImageDao();
	}
}
