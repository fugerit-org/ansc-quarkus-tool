package org.fugerit.java.anscquarkustool.rest.val;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.fugerit.java.anscquarkustool.rest.val.tipofile.FacadeValidazioneAllegati;
import org.fugerit.java.anscquarkustool.rest.val.tipofile.ValidazioneResult;
import org.fugerit.java.emp.sm.service.ServiceMessage;
import org.jboss.resteasy.reactive.RestForm;

import java.io.InputStream;

@Path("/val")
@Slf4j
@ApplicationScoped
public class ValResource {

    @Inject
    FacadeValidazioneAllegati facade;

    @POST
    @Path("/{ext}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Validazione file allegato",
            description = "Valida un file con estensione data tra quelle consentite nel sistema ANSC (PDF, JPG, TIF, P7M)."
    )
    @Tag(name="validazione", description="Validazione file allegati")
    public Response validateFile(@Parameter(description = "Estensione del file (PDF, JPG, TIF, P7M)") @PathParam("ext") String ext, @RestForm InputStream file) {
        ValOutput output = new ValOutput();
        int status = Response.Status.BAD_REQUEST.getStatusCode();
        try {
            ValidazioneResult result = this.facade.validate( file );
            log.info( "valid? : {}", result );
            output.setValid( result.isValido() );
            if ( output.isValid() ) {
                output.addAllBySeverity( ServiceMessage.newMessage( "OK", ServiceMessage.Severity.INFO, result.getDescriptionFormato() ) );
                return Response.ok(output).build();
            }
        } catch (Exception e) {
            output.setValid(Boolean.FALSE);
        }
        return Response.status( Response.Status.BAD_REQUEST.getStatusCode() ).entity(output).build();
    }
}
