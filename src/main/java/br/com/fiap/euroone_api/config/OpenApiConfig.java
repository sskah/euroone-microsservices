package br.com.fiap.euroone_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Personalização da documentação OpenAPI/Swagger.
 *
 * A UI interativa é disponibilizada em:
 *   http://localhost:8080/swagger-ui.html
 * E o contrato JSON em:
 *   http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI euroOneOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EuroOne API")
                        .description("API REST do projeto EuroOne - Plataforma de aprendizagem em saúde "
                                + "desenvolvida para o Challenge FIAP x Eurofarma. Sprint 3 - "
                                + "Java Microsserviços.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipe IV-ONE")
                                .email("contato@euroone.fiap.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
