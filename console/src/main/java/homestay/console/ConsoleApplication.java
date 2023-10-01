package homestay.console;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"homestay.module.city.mapper","homestay.module.homestay.mapper","homestay.module.user.mapper"})
@SpringBootApplication(scanBasePackages="homestay")
public class ConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

}
