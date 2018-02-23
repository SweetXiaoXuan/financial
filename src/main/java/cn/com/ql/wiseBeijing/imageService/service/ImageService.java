package cn.com.ql.wiseBeijing.imageService.service;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ql.wiseBeijing.imageService.dao.ImagesDao;
import cn.com.ql.wiseBeijing.imageService.daoBean.Images;
import cn.com.ql.wiseBeijing.imageService.frontBean.FImages;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
@Component
public class ImageService {
	@Bean
	public ImageService imageService()
	{
		return new ImageService();
	}
	@Resource(name = "imagesDao")
	private ImagesDao imagesDao;
	@Transactional
	public ReturnValue<FImages> getImages(String pid) {
		ReturnValue<FImages> returnValue = new ReturnValue<FImages>();
		returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
		Images images = imagesDao.get(Images.class, pid);
		if (images != null) {
			FImages fImages = new FImages();
			fImages.setDescription(images.getDescription());
			fImages.setPid(images.getPid());
			fImages.setUrl(images.getUrl());
			returnValue.setObject(fImages);
			return returnValue;
		} else {
			returnValue.setFlag(ReturnValue.FLAG_FAIL);
		}
		return returnValue;
	}
}
