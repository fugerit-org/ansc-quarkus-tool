package org.fugerit.java.anscquarkustool;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class AppInit {

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
    }

}
