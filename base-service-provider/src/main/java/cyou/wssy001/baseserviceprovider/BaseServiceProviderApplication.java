package cyou.wssy001.baseserviceprovider;

import cyou.wssy001.baseserviceprovider.config.ProjectRuntimeHintRegister;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.security.Provider;
import java.security.Security;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("cyou.wssy001.**")
@ImportRuntimeHints(value = {ProjectRuntimeHintRegister.class})
public class BaseServiceProviderApplication {

    static {
        Provider bc = Security.getProvider("BC");
        if (bc == null) Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        SpringApplication.run(BaseServiceProviderApplication.class, args);
    }
}
