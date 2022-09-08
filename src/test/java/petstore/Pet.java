package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {
    String uri = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPet() throws IOException{
        String jsonBody = lerJson("db/pet1.json");

        String token =
        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Garfield"))
                .body("status", is("available"))
                .body("tags.name", contains("feline"))
                .body("category.name", is("cat"))
        .extract()
                .path("category.name")
        ;

        System.out.println("O toke é "+ token);
    }

    @Test(priority = 2)
    public void consultarPet(){
        String petId = "2404201324042013";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Garfield"))
                .body("category.name",is("cat"))
                .body("status", is("available"))
        ;

    }

    @Test(priority = 3)
    public void alterarPet() throws IOException{
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Garfield"))
                .body("status", is("sold"))
        ;
    }

    @Test(priority = 4)
    public void excluidPet(){
        String petId = "2404201324042013";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri+ "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }

}