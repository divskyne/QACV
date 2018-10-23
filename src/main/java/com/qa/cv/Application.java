package com.qa.cv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.qa.cv.model.Role;
import com.qa.cv.model.User;
import com.qa.cv.repo.UserRepository;

@SpringBootApplication
@ComponentScan
public class Application implements CommandLineRunner
{
	
	@Autowired
	private UserRepository userRepo;
	
	public static final int ROLE_MANAGER = 1;
	public static final int ROLE_SALES = 2;
	
 	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
 	
 	@Override
    public void run(String... strings) throws Exception {
	
		BCryptPasswordEncoder pe = new  BCryptPasswordEncoder();
		
		if (!(userRepo.count()>0)) {
			
			User user = new User();
			user.setUserName("Bob");
			user.setPassword(pe.encode("admin"));
			Role role = new Role();
			role.setId(ROLE_MANAGER);
			role.setRole("ROLE_MANAGER");
			user.setRole(role);
			userRepo.save(user);
			

			User userAlice = new User();
			userAlice.setUserName("Alice");
			userAlice.setPassword(pe.encode("password"));
			Role roleAlice = new Role();
			roleAlice.setId(ROLE_SALES);
			roleAlice.setRole("ROLE_SALES");
			userAlice.setRole(roleAlice);
			userRepo.save(userAlice);
			
		}
		
    }
}
