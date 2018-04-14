package tools.dynamia.cms.site.core

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
@EnableCaching
class SiteCacheConfig {

	
	@Bean
    CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject())
    }

	@Bean
    EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean()
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"))
        cmfb.setShared(true)
        return cmfb
    }
	
}