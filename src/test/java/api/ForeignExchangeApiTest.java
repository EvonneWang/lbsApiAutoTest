package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import common.Util;
import io.restassured.RestAssured;
import model.DataVO;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ForeignExchangeApiTest {
    static JsonSchemaFactory jsonschemaemaFactory = null;

    DataVO data = new DataVO();

    ForeignExchangeApiTest() {
        data.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
    }

    @BeforeClass(groups = {"all"})
    public static void setUp() {
        RestAssured.baseURI = "http://3.130.122.199:8086/foreignexchange/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    @Test(groups = {"all", "foreign-exchange"})
    public void depositAccountEnquiryAccountDetailsGet() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/foreignExchange/foreignExchange.txt"))
                .post("foreignExchange/foreignExchange").then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/foreignExchange/foreignExchange").using(jsonschemaemaFactory));
    }
}
