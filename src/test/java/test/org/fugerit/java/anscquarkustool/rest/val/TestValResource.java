package org.fugerit.java.anscquarkustool.rest.val;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TestValResource {

    @Test
    public void testValidatePdfFile() {
        File testFile = new File("src/test/resources/sample/pdf_as_pdf.pdf");
        RestAssured.given()
                .multiPart("file", testFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("/val/PDF")
                .then()
                .statusCode( Response.Status.OK.getStatusCode() )
                .body("valid", equalTo(Boolean.TRUE));
    }

    @Test
    public void testValidateP7MFile() {
        File testFile = new File("src/test/resources/sample/pdf_as_pdf_p7m.pdf");
        RestAssured.given()
                .multiPart("file", testFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("/val/P7M")
                .then()
                .statusCode( Response.Status.OK.getStatusCode() )
                .body("valid", equalTo(Boolean.TRUE));
    }

    @Test
    public void testInvalidExtension() {
        File testFile = new File("src/test/resources/sample/png_as_pdf.pdf");
        RestAssured.given()
                .multiPart("file", testFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("/val/PDF")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("valid", equalTo(Boolean.FALSE));
    }

}
