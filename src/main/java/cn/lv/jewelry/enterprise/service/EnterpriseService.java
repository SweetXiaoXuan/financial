package cn.lv.jewelry.enterprise.service;

import cn.lv.jewelry.enterprise.dao.EnterpriseDao;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by lixing on 16/3/19.
 */
@Component
@Transactional
public class EnterpriseService {
    @Resource(name = "enterpriseDao")
    private EnterpriseDao enterpriseDao;
    @Bean
    public EnterpriseService enterpriseService()
    {
        return new EnterpriseService();
    }
}
