package lv.datuskola.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

  public void configureViewResolvers (ViewResolverRegistry registry) {
      //by default prefix = "/WEB-INF/" and  suffix = ".jsp"
      registry.jsp().prefix("/jsp/").suffix(".jsp");
  }
}