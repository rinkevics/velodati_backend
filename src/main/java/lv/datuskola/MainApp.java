package lv.datuskola;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MainApp {

   public static void main(String[] args) {
      Map<String, Object> props = new HashMap<>();
      props.put("spring.servlet.multipart.max-file-size", "10MB");
      props.put("spring.servlet.multipart.max-request-size", "10MB");

      new SpringApplicationBuilder()
              .sources(MainApp.class)
              .properties(props)
              .run(args);
   }

   // this is just to get rid of errors
   @Bean
   public TomcatServletWebServerFactory tomcatFactory() {
      return new TomcatServletWebServerFactory() {
         @Override
         protected void postProcessContext(Context context) {
            ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
         }
      };
   }
}