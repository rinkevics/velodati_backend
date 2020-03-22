package lv.datuskola;

import lv.datuskola.config.Secrets;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
public class MainApp {

   public static Properties decryptedProperties;

   public static void main(String[] args) throws IOException, GeneralSecurityException {
      System.out.print("Start:");
      Scanner scan = new Scanner(System.in);
      String password = scan.next();
      decryptedProperties = Secrets.readAndDecrypt(password);

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