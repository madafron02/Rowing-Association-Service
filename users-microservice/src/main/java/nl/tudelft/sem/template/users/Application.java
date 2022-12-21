package nl.tudelft.sem.template.users;

import nl.tudelft.sem.template.users.domain.Organisation;
import nl.tudelft.sem.template.users.domain.OrganisationRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

/**
 * Example microservice application.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner initialise(OrganisationRepo organisationRepo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (organisationRepo.count() == 0) {
                    System.out.println("Organisation names added to the database!");
                    organisationRepo.save(new Organisation("DelftRowing"));
                    organisationRepo.save(new Organisation("Laga"));
                    organisationRepo.save(new Organisation("Proteus"));
                    organisationRepo.save(new Organisation("OlympicRowers"));
                }
            }
        };
    }
}
