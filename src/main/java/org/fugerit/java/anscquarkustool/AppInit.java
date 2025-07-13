package org.fugerit.java.anscquarkustool;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="validazione", description="Validazione file allegati"),
        },
        info = @Info(
                title="Strumenti di utilità per ANSC",
                version = "1.0.0",
                contact = @Contact(
                        name = "Matteo Franci",
                        url = "https://github.com/fugerit-org/ansc-quarkus-tool",
                        email = "mttfranci@sogei.it") ),
        servers = {
                @Server( description = "Locale", url = "http://localhost:8080" )
        }
)
@Slf4j
@ApplicationScoped
public class AppInit {

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
    }

}
