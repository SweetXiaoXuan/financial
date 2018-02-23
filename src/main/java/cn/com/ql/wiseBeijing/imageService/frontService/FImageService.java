package cn.com.ql.wiseBeijing.imageService.frontService;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.imageService.frontBean.FImages;
import cn.com.ql.wiseBeijing.imageService.service.ImageService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.util.SystemConf;
import cn.xxtui.support.util.XXMediaType;

@Component
@Path("/images")
public class FImageService {
	@Bean
	public FImageService fimageService() {
		return new FImageService();
	}

	@Resource(name = "imageService")
	private ImageService is;

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/showImage/{pid}")
	public Response showImages(@PathParam("pid") String pid,@QueryParam("scale") String scale) {
		ResultBean rb = new ResultBean();
		ReturnValue<FImages> r = is.getImages(pid);
		rb.setStatus(ResultBean.ERROR);
		if (r.getFlag() == ReturnValue.FLAG_FAIL) {
			return Response.ok(rb.toString()).build();
		} else {
			URI url;
			try {
				url = new URI(SystemConf.get("image_server_url")
						+ r.getObject().getUrl());
				if(scale!=null&&scale.equals("small")){
					url = new URI(SystemConf.get("image_server_url")
							+ r.getObject().getUrl()+"_small");
					return Response.seeOther(url).build();
				}
				return Response.seeOther(url).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.ok(rb.toString()).build();
			}
		}
	}
}
