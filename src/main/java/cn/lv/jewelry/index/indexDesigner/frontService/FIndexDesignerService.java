package cn.lv.jewelry.index.indexDesigner.frontService;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

/**
 * Created by lixing on 16/4/9.
 */
@Component
@Path("/lv/indexDesigner")
public class FIndexDesignerService {
    @Bean
    public FIndexDesignerService fIndexDesignerService() {
        return new FIndexDesignerService();
    }


}
