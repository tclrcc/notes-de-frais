package fr.coloricchio.notesdefrais.infrastructure.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sans ce contrôle, l'absence de NDF_DB_PASSWORD se manifeste par une erreur d'authent PostgreSQL
 */
@Configuration
public class VerificationConfiguration {

    @Bean
    static BeanFactoryPostProcessor verifierVariablesRequises() {
        return beanFactory -> {
            String motDePasse = System.getenv("NDF_DB_PASSWORD");
            if (motDePasse == null || motDePasse.isBlank()) {
                throw new IllegalStateException(
                        "La variable d'environnement NDF_BD_PASSWORD n'est pas définie. "
                        + "Voir la section 'Démarrage' du README.");
            }
        };
    }

}
