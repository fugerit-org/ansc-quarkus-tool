package org.fugerit.java.anscquarkustool.rest.val.tipofile;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum EnumTipoFile {

    PDF(1, "PDF"),
    JPG(2, "JPG"),
    TIF(3, "TIF"),
    P7M(4, "P7M");

    @Getter
    private Integer code;

    @Getter
    private String description;

    EnumTipoFile(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EnumTipoFile fromDescription(String description) {
        for (EnumTipoFile tipo : EnumTipoFile.values()) {
            if (tipo.getDescription().equalsIgnoreCase(description)) {
                return tipo;
            }
        }
        return null;
    }

}
