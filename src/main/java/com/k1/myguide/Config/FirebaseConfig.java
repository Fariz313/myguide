package com.k1.myguide.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.Data;

@Configuration
@Component
@Data
public class FirebaseConfig {
	@Value("${fireBase.dbBaseUrl}")
	private String dbBaseUrl;
	@Value("${fireBase.collection}")
	private String collectionName;
	@Value("${fireBase.authFileLocation}")
	private String authFileLocation;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
