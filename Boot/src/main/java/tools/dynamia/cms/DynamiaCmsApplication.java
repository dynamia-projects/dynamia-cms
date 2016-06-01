package tools.dynamia.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.services.impl.JpaCrudService;
import tools.dynamia.io.IOUtils;
import tools.dynamia.io.impl.SpringResource;

@SpringBootApplication(scanBasePackages = "tools.dynamia", exclude = {VelocityAutoConfiguration.class})
@EntityScan(basePackages = "tools.dynamia")
public class DynamiaCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamiaCmsApplication.class, args);
    }

    @Bean
    public CrudService jpaCrudService() {
        return new JpaCrudService();
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager manager = new EhCacheCacheManager();
        manager.setCacheManager(ehcache().getObject());
        return manager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehcache = new EhCacheManagerFactoryBean();
        SpringResource resource = (SpringResource) IOUtils.getResource("classpath:ehcache.xml");
        ehcache.setConfigLocation(resource.getInternalResource());
        return ehcache;
    }

}
