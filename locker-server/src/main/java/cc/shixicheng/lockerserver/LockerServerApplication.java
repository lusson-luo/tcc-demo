package cc.shixicheng.lockerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class LockerServerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(LockerServerApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter());
		springApplication.run(args);
	}

}
