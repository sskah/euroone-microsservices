package br.com.fiap.euroone_api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração central do ModelMapper.
 *
 * Expor um único bean garante que todos os Mappers da aplicação usem a mesma
 * configuração (estratégia STRICT evita mapeamentos ambíguos entre Entity e DTO).
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
