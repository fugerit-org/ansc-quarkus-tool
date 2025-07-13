package org.fugerit.java.anscquarkustool.rest.val.tipofile;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.DocValidatorTypeCheck;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.p7m.P7MContentValidator;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class FacadeValidazioneAllegati {

    private DocValidatorTypeCheck facade;

    private P7MContentValidator p7mValidator;

    private static final Map<String, EnumTipoFile> MAP_TYPES = Map.of( ImageValidator.MIME_JPG, EnumTipoFile.JPG,
            ImageValidator.MIME_TIFF, EnumTipoFile.TIF,
            PdfboxValidator.MIME_TYPE, EnumTipoFile.PDF,
            P7MValidator.MIME_TYPE, EnumTipoFile.P7M );

    public FacadeValidazioneAllegati() {
        this.facade = DocValidatorTypeCheck.newInstance( DocValidatorFacade.newFacadeStrict(
                PdfboxStrictValidator.DEFAULT,
                ImageValidator.JPG_VALIDATOR,
                ImageValidator.TIFF_VALIDATOR ) );
        this.p7mValidator = P7MContentValidator.newValidator( facade.getFacade() );
    }

    public ValidazioneResult validate(byte[] data) {
        return SafeFunction.get( () -> {
            String mimeType = facade.checkType( data );
            if ( mimeType != null ) {
                return new ValidazioneResult( MAP_TYPES.get( mimeType ), MAP_TYPES.get( mimeType ), Boolean.TRUE );
            } else {
                try ( InputStream is = new ByteArrayInputStream(data) ) {
                    String checkP7MType = this.p7mValidator.checkInnerType( is );
                    if ( checkP7MType != null ) {
                        return new ValidazioneResult( EnumTipoFile.P7M, MAP_TYPES.get( checkP7MType ), Boolean.TRUE );
                    }
                } catch (Exception e) {
                    log.warn( "p7m non valido : {}", e );
                }
            }
            return new ValidazioneResult( null, null, Boolean.FALSE );
        } );

    }

    public ValidazioneResult validate(InputStream is) {
        return this.validate( SafeFunction.get( () -> StreamIO.readBytes( is ) ) );
    }

}
