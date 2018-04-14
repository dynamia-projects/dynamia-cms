package tools.dynamia.cms.site.templates

import org.springframework.core.Ordered
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView

class JsonViewResolver implements ViewResolver, Ordered {

	private int order = 0

    @Override
    View resolveViewName(String viewName, Locale locale) throws Exception {
		MappingJackson2JsonView view = new MappingJackson2JsonView()
        view.setPrettyPrint(true)

        return view
    }

    int getOrder() {
		return order
    }

    void setOrder(int order) {
		this.order = order
    }

}