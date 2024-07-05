package project.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySource({
	"file:${user.home}/config/store/cloth.properties",
})
public class ClothApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothApplication.class, args);
	}

}
