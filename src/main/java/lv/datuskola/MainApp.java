package lv.datuskola;

import lv.datuskola.auth.MemoryWarningSystem;
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

   public static  Properties properties;
   public static  String     SERVER_URL   = "server-url";
   private static String     SECRETS_FILE = "secrets-file";

   public static void main(String[] args) throws IOException, GeneralSecurityException {
      String password = getPassword();

      initProperties(args, password);

      initMemoryWarningSystem();
      new SpringApplicationBuilder()
              .sources(MainApp.class)
              .properties(getSpringProperties())
              .run(args);
   }

   private static String getPassword() {
      Scanner scan = new Scanner(System.in);
      return scan.next();
   }

   private static void initProperties(String[] args, String password) throws IOException, GeneralSecurityException {
      var argMap = unpackArguments(args);
      properties = new Properties();
      argMap.forEach((key, value) -> properties.put(key, value));
      Secrets.readAndDecrypt(argMap.get(SECRETS_FILE), password).forEach((key, value) -> properties.put(key, value) );
   }

   private static Map<String, String> unpackArguments(String[] args) {
      Map<String, String> argMap = new HashMap<>();
      for(String arg : args ) {
         String[] param = arg.split("=");
         String name = param[0];
         String value = param[1];
         argMap.put(name, value);
      }
      return argMap;
   }

   private static void initMemoryWarningSystem() {
      MemoryWarningSystem.setPercentageUsageThreshold(0.6);
      MemoryWarningSystem mws = new MemoryWarningSystem();
      mws.addListener((usedMemory, maxMemory) -> {
         System.out.println("Memory usage low!!!");
         double percentageUsed = ((double) usedMemory) / maxMemory;
         System.out.println("percentageUsed = " + percentageUsed);
         MemoryWarningSystem.setPercentageUsageThreshold(0.8);
      });
   }

   private static Map<String, Object> getSpringProperties() {
      Map<String, Object> props = new HashMap<>();
      props.put("spring.servlet.multipart.max-file-size", "10MB");
      props.put("spring.servlet.multipart.max-request-size", "10MB");
      return props;
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