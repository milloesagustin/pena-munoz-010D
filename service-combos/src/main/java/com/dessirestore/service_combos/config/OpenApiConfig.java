package com.dessirestore.service_combos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API DesireeStore - Servicio de Combos")
                        .version("1.0")
                        .description("Documentación centralizada a través del Gateway"))
                // ESTO ES LO QUE SOLUCIONA EL ENRUTAMIENTO HACIA EL GATEWAY:
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("Servidor a través del Gateway")
                ))
                // 1. Registramos el uso del esquema de autenticación
                .addSecurityItem(new SecurityRequirement().addList("SwaggerLogin"))
                // 2. Definimos la ventana que pedirá Usuario y Contraseña
                .components(new Components()
                        .addSecuritySchemes("SwaggerLogin", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .password(new OAuthFlow()
                                                // La ruta exacta de tu Gateway hacia tu nuevo adaptador
                                                .tokenUrl("http://localhost:9090/auth/swagger-login")
                                        )
                                )
                                .description("Ingresa tu Usuario y Contraseña del sistema")
                        ));
    }
}