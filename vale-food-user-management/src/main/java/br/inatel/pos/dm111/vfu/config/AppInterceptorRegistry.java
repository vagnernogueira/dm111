package br.inatel.pos.dm111.vfu.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.inatel.pos.dm111.vfu.api.core.interceptor.AuthenticationInterceptor;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

	private static final List<String> ENDPOINTS_PATTERN = List.of("/valefood/users**", "/valefood/users/**",
			"/valefood/restaurants**", "/valefood/restaurants/**");

	private final AuthenticationInterceptor interceptor;

	public AppInterceptorRegistry(AuthenticationInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor).addPathPatterns(ENDPOINTS_PATTERN);
	}
}
