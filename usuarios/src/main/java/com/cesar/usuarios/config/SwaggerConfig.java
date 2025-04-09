package com.cesar.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuários")
                        .version("1.0")
                        .description("Gerenciamento de usuários do Innovation Manager")
                        .contact(new Contact()
                                .name("Tary Nascimento")
                                .email("tary.junior47@gmail.com")
                                .url("https://github.com/TN-Junior")));
    }
}
