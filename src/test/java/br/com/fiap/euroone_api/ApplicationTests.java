package br.com.fiap.euroone_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Sanity check: garante que o contexto do Spring sobe sem erros.
 * Usa H2 em memória para não depender do MySQL nos testes.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:euroone-test;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ApplicationTests {

    @Test
    void contextLoads() {
        // Se o Spring subir sem exceção, o teste passa.
    }
}
