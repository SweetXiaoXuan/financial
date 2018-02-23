package cn.lv.jewelry.index.indexLabel.frontService;

import cn.xxtui.support.util.MeaasgeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

/**
 * 标签相关
 */
@Component
@Path("/lv/label")
public class FLabelService {
    private final static Logger logger = LoggerFactory.getLogger(FLabelService.class);
    @Bean
    public FLabelService fLabelService() {
        return new FLabelService();
    }
    private MeaasgeUtil me = new MeaasgeUtil();
}
