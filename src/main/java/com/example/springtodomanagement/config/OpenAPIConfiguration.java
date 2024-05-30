package com.example.springtodomanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
//        Server server = new Server();
//        server.setUrl("http://localhost:8080");
//        server.setDescription("Development");
        Contact myContact = new Contact();
        myContact.setName("Hazhir Rahmani");
        myContact.setEmail("programmer4906@gmail.com");

        Info information = new Info()
                .title("Todo Management System API")
                .version("3.0")
                .description("This API exposes endpoints to manage Todos.")
                .contact(myContact);
        return new OpenAPI().info(information)/*.servers(List.of(server))*/;
    }
}