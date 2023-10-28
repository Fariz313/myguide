package com.k1.myguide.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import lombok.Data;

@Configuration
@Component
@Data
public class FirebaseConfig {
	@Value("${fireBase.collection}")
	private String dbBaseUrl;
	@Value("${fireBase.collection}")
	private String collectionName;
	@Value("${fireBase.authFileLocation}")
	private String authFileLocation;

}
