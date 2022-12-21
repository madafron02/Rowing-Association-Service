package nl.tudelft.sem.template.matching;

import nl.tudelft.sem.template.matching.domain.Certificate;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner initialise(CertificateRepo certificateRepo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (certificateRepo.count() == 0) {
                    System.out.println("Certificates added to the database!");
                    certificateRepo.save(new Certificate("C4"));
                    certificateRepo.save(new Certificate("4+"));
                    certificateRepo.save(new Certificate("8+"));
                }
            }
        };
    }

}
