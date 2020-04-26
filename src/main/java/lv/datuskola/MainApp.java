package lv.datuskola;

import lv.datuskola.auth.MemoryWarningSystem;
import lv.datuskola.config.Secrets;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.io.Console;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.security.GeneralSecurityException;
import java.util.*;

@SpringBootApplication
public class MainApp {

   public static Properties decryptedProperties;

   public static void main(String[] args) throws IOException, GeneralSecurityException {

      MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
      System.out.println("max " + memoryBean.getHeapMemoryUsage().getMax());
      System.out.println("init "+ memoryBean.getHeapMemoryUsage().getInit());
      System.out.println("used " + memoryBean.getHeapMemoryUsage().getUsed());

      Map<String, String> argMap = new HashMap<>();
      for(String aa : args ) {
         String[] param = aa.split("=");
         String name = param[0];
         String value = param[1];
         argMap.put(name, value);

         System.out.println(name + " " + value);
      }

      String secretsFile = argMap.get("secrets-file");
      String password = argMap.get("password");
      System.out.println("secrets "+ secretsFile);
      System.out.println("password " + password);

      MemoryWarningSystem.setPercentageUsageThreshold(0.6);
      MemoryWarningSystem mws = new MemoryWarningSystem();
      mws.addListener((usedMemory, maxMemory) -> {
         System.out.println("Memory usage low!!!");
         double percentageUsed = ((double) usedMemory) / maxMemory;
         System.out.println("percentageUsed = " + percentageUsed);
         MemoryWarningSystem.setPercentageUsageThreshold(0.8);
      });

      loadSecrets(argMap.get("secrets-file"), argMap.get("password"));
      new SpringApplicationBuilder()
              .sources(MainApp.class)
              .properties(getProperties())
              .run(args);
   }

   private static Map<String, Object> getProperties() {
      Map<String, Object> props = new HashMap<>();
      props.put("spring.servlet.multipart.max-file-size", "10MB");
      props.put("spring.servlet.multipart.max-request-size", "10MB");
      return props;
   }

   private static void loadSecrets(String secretsFile, String password) throws IOException, GeneralSecurityException {
      System.out.println("secrets "+ secretsFile);
      System.out.println("test " + password);
      decryptedProperties = Secrets.readAndDecrypt(secretsFile, password);
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