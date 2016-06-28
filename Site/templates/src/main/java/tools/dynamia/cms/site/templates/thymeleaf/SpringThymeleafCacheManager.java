package tools.dynamia.cms.site.templates.thymeleaf;

import java.util.List;
import java.util.Properties;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.thymeleaf.Template;
import org.thymeleaf.cache.AbstractCacheManager;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.cache.ICacheEntryValidityChecker;
import org.thymeleaf.dom.Node;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.integration.Containers;

public class SpringThymeleafCacheManager extends AbstractCacheManager {

	public static final String TEMPLATE_CACHE_NAME = "thymeleafTemplateCache";
	public static final String FRAGMENT_CACHE_NAME = "thymeleafFragmentCache";
	public static final String MESSAGE_CACHE_NAME = "thymeleafMessageCache";
	public static final String EXPRESSION_CACHE_NAME = "thymeleafExpressionCache";

	private CacheManager cacheManager;

	public SpringThymeleafCacheManager(CacheManager cacheManager) {
		super();
		this.cacheManager = cacheManager;
	}

	@Override
	protected ICache<String, Template> initializeTemplateCache() {
		return new CacheToICacheAdapter<Template>(cacheManager.getCache(TEMPLATE_CACHE_NAME));
	}

	@Override
	protected ICache<String, List<Node>> initializeFragmentCache() {

		return new CacheToICacheAdapter<List<Node>>(cacheManager.getCache(FRAGMENT_CACHE_NAME));
	}

	@Override
	protected ICache<String, Properties> initializeMessageCache() {

		return new CacheToICacheAdapter<Properties>(cacheManager.getCache(MESSAGE_CACHE_NAME));
	}

	@Override
	protected ICache<String, Object> initializeExpressionCache() {
		return new CacheToICacheAdapter<Object>(cacheManager.getCache(EXPRESSION_CACHE_NAME));
	}

	private static final class CacheToICacheAdapter<V> implements ICache<String, V> {

		private final Cache cache;

		private static final class CacheEntry<V> {
			private final V value;
			private final long timestamp;

			public CacheEntry(V value) {
				this.value = value;
				this.timestamp = System.currentTimeMillis();
			}

			public V getValue() {
				return value;
			}

			public long getTimestamp() {
				return timestamp;
			}
		}

		public CacheToICacheAdapter(Cache cache) {
			this.cache = cache;
		}

		private String siteKeyResolver(String key) {
			String siteKey = null;
			try {
				siteKey = SiteContext.get().getCurrent().getKey();
			} catch (Exception e) {
				try {
					SiteService service = Containers.get().findObject(SiteService.class);
					siteKey = service.getSite(CMSUtil.getCurrentRequest()).getKey();
				} catch (Exception e1) {
					System.err.println("No site key found for Cache Manager");
				}
			}

			if (siteKey == null) {
				siteKey = key;
			} else {
				siteKey = siteKey + key;
			}

			return siteKey;
		}

		@Override
		public void put(String key, V value) {
			key = siteKeyResolver(key);
			cache.put(key, new CacheEntry<V>(value));
		}

		@Override
		public V get(String key) {
			key = siteKeyResolver(key);
			@SuppressWarnings("unchecked")
			CacheEntry<V> cacheEntry = cache.get(key, CacheEntry.class);
			if (cacheEntry == null) {
				return null;
			} else {
				return cacheEntry.getValue();
			}
		}

		@Override
		public V get(String key, ICacheEntryValidityChecker<? super String, ? super V> validityChecker) {
			key = siteKeyResolver(key);
			@SuppressWarnings("unchecked")
			CacheEntry<V> cacheEntry = cache.get(key, CacheEntry.class);
			if (cacheEntry == null) {
				return null;
			} else {
				V value = cacheEntry.getValue();
				if (validityChecker == null
						|| validityChecker.checkIsValueStillValid(key, value, cacheEntry.getTimestamp())) {
					return value;
				} else {
					return null;
				}
			}
		}

		@Override
		public void clear() {
			cache.clear();
		}

		@Override
		public void clearKey(String key) {
			key = siteKeyResolver(key);
			cache.evict(key);
		}

	}

}
