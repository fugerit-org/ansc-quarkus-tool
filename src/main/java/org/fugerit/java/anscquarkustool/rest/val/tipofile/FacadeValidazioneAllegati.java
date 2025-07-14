package org.fugerit.java.anscquarkustool.rest.val.tipofile;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocTypeValidator;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.DocValidatorTypeCheck;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.p7m.P7MContentValidator;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class FacadeValidazioneAllegati {

    private Map<EnumTipoFile, DocTypeValidator> validators;

    private static final Map<String, EnumTipoFile> MAP_TYPES = Map.of( ImageValidator.MIME_JPG, EnumTipoFile.JPG,
            ImageValidator.MIME_TIFF, EnumTipoFile.TIF,
            PdfboxValidator.MIME_TYPE, EnumTipoFile.PDF,
            P7MValidator.MIME_TYPE, EnumTipoFile.P7M );

    public FacadeValidazioneAllegati() {
        this.validators = new HashMap<>();
        this.validators.put( EnumTipoFile.PDF, PdfboxStrictValidator.DEFAULT );
        this.validators.put( EnumTipoFile.JPG, ImageValidator.JPG_VALIDATOR );
        this.validators.put( EnumTipoFile.TIF, ImageValidator.TIFF_VALIDATOR );
        this.validators.put( EnumTipoFile.P7M,
                P7MContentValidator.newValidator( DocValidatorFacade.newFacadeStrict(
                        this.validators.values().toArray( new DocTypeValidator[0] ) ) ) );
    }

    public ValidazioneResult validate(byte[] data, EnumTipoFile tipoFile) {
        return SafeFunction.get( () -> {
            DocTypeValidator validator = this.validators.get( tipoFile );
            try ( InputStream is = new ByteArrayInputStream(data) ) {
                DocTypeValidationResult result = validator.validate( is );
                EnumTipoFile innerFile = tipoFile;
                if ( EnumTipoFile.P7M == tipoFile ) {
                    P7MContentValidator p7mValidator = (P7MContentValidator) this.validators.get( tipoFile );
                    try ( InputStream isInner = new ByteArrayInputStream(data) ) {
                        String type = p7mValidator.checkInnerType( isInner );
                        innerFile = EnumTipoFile.fromDescription(  type );
                    }
                }
                return new ValidazioneResult( tipoFile, innerFile, result.isResultOk(), result.getValidationMessage() );
            }
        } );

    }

    public ValidazioneResult validate(InputStream is, EnumTipoFile tipoFile) {
        return this.validate( SafeFunction.get( () -> StreamIO.readBytes( is ) ), tipoFile);
    }

}
