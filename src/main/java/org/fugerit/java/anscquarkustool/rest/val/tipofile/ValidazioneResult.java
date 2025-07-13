package org.fugerit.java.anscquarkustool.rest.val.tipofile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.fugerit.java.core.function.SafeFunction;

@AllArgsConstructor
@ToString
public class ValidazioneResult {

    @Getter
    private EnumTipoFile formato;

    @Getter
    private EnumTipoFile formatoRef;

    @Getter
    private boolean valido;

    public Integer getCodeFormato() {
        return SafeFunction.getIfNotNull( this.getFormato(), () -> this.getFormato().getCode() );
    }

    public Integer getCodeFormatoRef() {
        return SafeFunction.getIfNotNull( this.getFormatoRef(), () -> this.getFormatoRef().getCode() );
    }

    public String getDescriptionFormato() {
        return SafeFunction.getIfNotNull( this.getFormato(), () -> this.getFormato().getDescription() );
    }

    public String getDescriptionFormatoRef() {
        return SafeFunction.getIfNotNull( this.getFormatoRef(), () -> this.getFormatoRef().getDescription() );
    }

}
