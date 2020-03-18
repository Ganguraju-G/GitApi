package com.github.GitHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitHubApplication {

	public static void main(String[] args) {
		System.out.println("running application..................");
		SpringApplication.run(GitHubApplication.class, args);
	}

}
