package com.aethernet.helpdesk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AetherNet Helpdesk API")
                        .version("1.0.0")
                        .description("API REST para sistema de gerenciamento de chamados técnicos (helpdesk). " +
                                "Permite gerenciar clientes, técnicos e chamados de suporte com controle de status e prioridades.")
                        .contact(new Contact()
                                .name("AetherNet Development Team")
                                .email("contato@aethernet.com")
                                .url("https://github.com/gabrieldnsilva"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}
