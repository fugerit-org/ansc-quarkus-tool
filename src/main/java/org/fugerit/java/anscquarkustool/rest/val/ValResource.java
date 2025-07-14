package org.fugerit.java.anscquarkustool.rest.val;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
    @APIResponse(responseCode = "200",
            description = "Validazione avvenuta con esito positivo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ValOutput.class)))
    @APIResponse(responseCode = "400",
            description = "Validazione avvenuta con esito negativo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ValOutput.class)))
    @APIResponse(responseCode = "500",
            description = "Validazione avvenuta con errore",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ValOutput.class)))
    @Operation(
            summary = "Validazione file allegato",
            description = "Valida un file con estensione data tra quelle consentite nel sistema ANSC (PDF, JPG, TIF, P7M)."
    )
    @Tag(name="validazione", description="Validazione file allegati")
    public Response validateFile(@Parameter(description = "Estensione del file (PDF, JPG, TIF, P7M)") @PathParam("ext") String ext, @RestForm InputStream file) {
        ValOutput output = new ValOutput();
        output.setValid(Boolean.FALSE);
        try {
            ValidazioneResult result = this.facade.validate( file );
            log.info( "valid? : {}", result );
            output.setValid( result.isValido() );
            if ( output.isValid() ) {
                output.addAllBySeverity( ServiceMessage.newMessage( "OK", ServiceMessage.Severity.INFO, result.getDescriptionFormato() ) );
                return Response.ok(output).build();
            }
        } catch (Exception e) {
            return Response.status( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() ).entity(output).build();
        }
        return Response.status( Response.Status.BAD_REQUEST.getStatusCode() ).entity(output).build();
    }
}
