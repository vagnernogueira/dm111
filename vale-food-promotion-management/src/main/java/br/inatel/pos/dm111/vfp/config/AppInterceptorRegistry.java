package br.inatel.pos.dm111.vfp.config;

import br.inatel.pos.dm111.vfp.api.core.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

    private static final List<String> ENDPOINTS_PATTERN = List.of(
            "/valefood/promotions**",
            "/valefood/promotions/**"
            );

    private final AuthenticationInterceptor interceptor;

    public AppInterceptorRegistry(AuthenticationInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns(ENDPOINTS_PATTERN);
    }
}
