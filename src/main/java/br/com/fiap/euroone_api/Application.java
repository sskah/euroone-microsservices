package br.com.fiap.euroone_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API EuroOne.
 *
 * A anotação {@link SpringBootApplication} habilita:
 *  - a configuração automática do Spring Boot;
 *  - o scanning de componentes (@Component, @Service, @Repository, @RestController)
 *    a partir deste pacote base ({@code br.com.fiap.euroone_api}).
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
