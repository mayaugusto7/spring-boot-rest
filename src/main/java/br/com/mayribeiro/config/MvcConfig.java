package br.com.mayribeiro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
	
		registry.addMapping("/rest/**")
				.allowedOrigins("http://localhost:8100")
				.allowedMethods("POST", "GET", "PUT", "DELETE")
				.allowedHeaders("Vary", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin *")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Origin *")
				.allowCredentials(false).maxAge(3600);
			
	}
	
}


