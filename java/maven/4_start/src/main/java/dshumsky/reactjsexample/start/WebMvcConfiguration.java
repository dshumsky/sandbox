package dshumsky.reactjsexample.start;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // disable caching
        VersionResourceResolver versionResolver = new VersionResourceResolver()
                .addContentVersionStrategy("/**");

        registry.addResourceHandler("/**/*.html", "/**/*.js")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true).addResolver(versionResolver);
    }

    /*
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // forward requests to /admin and /user to their index.html
        registry.addViewController("/admin").setViewName(
                "forward:/admin/index.html");
        registry.addViewController("/user").setViewName(
                "forward:/user/index.html");
    }*/
}
