package cyou.wssy001.baseserviceprovider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.Provider;
import java.security.Security;

@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "cyou.wssy001.**")
//@MapperScan(value = "cyou.wssy001.wikiprovider.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
public class BaseServiceProviderApplication {

    static {
        Provider bc = Security.getProvider("BC");
        if (bc == null) Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        SpringApplication.run(BaseServiceProviderApplication.class, args);
    }
}
