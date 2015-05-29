package com.dynamia.cms.site.banners.services;

import java.util.List;

import com.dynamia.cms.site.banners.domain.Banner;

public interface BannerService {

	public abstract List<Banner> getBannersByCategory(Long categoryId);

}