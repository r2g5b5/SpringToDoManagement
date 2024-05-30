package com.example.springtodomanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
        //return new OpenAPI().info(information)/*.servers(List.of(server))*/;
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(information)
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

}
