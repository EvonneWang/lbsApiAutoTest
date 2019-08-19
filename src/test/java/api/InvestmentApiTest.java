package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.JsonObject;
import common.JsonSchemaUtils;
import common.Util;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.DataVO;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class InvestmentApiTest {
    DataVO data = new DataVO();
    JsonSchemaFactory jsonschemaemaFactory = null;

    InvestmentApiTest() {
        data.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
        data.setClientid("devin");
        data.setMessageid("006f7113e5fa48559549c4dfe74e2cd6");
    }

    @BeforeClass(groups = {"all"})
    public void setUp() {
        RestAssured.baseURI = "http://3.130.122.199:8086/investment/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    /**
     * @mutual-fund
     **/

    @Test(enabled = true, groups = {"all", "mutual-fund"})
    public void investmentFundAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/fundAccountOpening.txt"))
                .post("fund/fundAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/fundAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mutual-fund"})
    public void investmentHoldingEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/holdingEnquiry.txt"))
                .post("fund/holdingEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/holdingEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mutual-fund"})
    public void investmentRedemptionPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/redemption.txt"))
                .post("fund/redemption")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/redemption").using(jsonschemaemaFactory));
    }


    @Test(enabled = true, groups = {"all", "mutual-fund"})
    public void investmentSubscriptionPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/subscription.txt"))
                .post("fund/subscription")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/subscription").using(jsonschemaemaFactory));
    }

    /**
     * @order
     */

    @Test(enabled = true, groups = {"all", "order"}, dependsOnMethods = "investmentOrderInfoUpdatePost")
    public void investmentCancellationPost() throws IOException {
        JSONObject jsonObject = Util.getObject("/request/investment/cancellation.txt");
        jsonObject.put("id", data.getOderId());
        given()
                .headers(Util.setHeader(data))
                .body(jsonObject)
                .post("investment/order/cancellation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/cancellation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "order"})
    public void investmentOrderDetailRetrievalsByIdPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/orderDetailRetrievalsById.txt"))
                .post("investment/order/orderDetailRetrievalsById")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/orderDetailRetrievalsById").using(jsonschemaemaFactory));
    }


    @Test(enabled = true, groups = {"all", "order"}, dependsOnMethods = "investmentOrderRetrievalPost")
    public void investmentOrderInfoUpdatePost() throws IOException {
        JSONObject jsonObject = Util.getObject("/request/investment/orderInfoUpdate.txt");
        jsonObject.put("id", data.getOderId());
        jsonObject.put("expiryDate", (System.currentTimeMillis() + 5 * 60 * 1000));
        given()
                .headers(Util.setHeader(data))
                .body(jsonObject)
                .post("investment/order/orderInfoUpdate")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/orderInfoUpdate").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "order"}, dependsOnMethods = "investmentOrderPlacingPost")
    public void investmentOrderRetrievalPost() throws IOException {
        Response response = given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/orderRetrieval.txt"))
                .post("investment/order/orderRetrieval")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/orderRetrieval").using(jsonschemaemaFactory)).extract().response();
        data.setOderId(response.path("data.id[0]"));
    }

/**this is internal api*/
//    @Test(groups = {"all", "order", "error"})
//    public void investmentOrderStatusUpdatePost() throws IOException {
//        given()
//                .headers(Util.setHeader(data))
//                .body(Util.getObject("/request/investment/orderStatusUpdate.txt"))
//                .post("investment/order/orderStatusUpdate")
//                .then()
//                .statusCode(200)
//                .body(matchesJsonSchemaInClasspath("response/investment/orderStatusUpdate").using(jsonschemaemaFactory));
//    }


    /**
     * @stock Stock
     */
    @Test(enabled = true, groups = {"all", "stock"})
    public void investmentOrderPlacingPost() throws IOException {
        JSONObject jsonObject = Util.getObject("/request/investment/orderPlacing.txt");
        jsonObject.put("expiryDate", (System.currentTimeMillis() + 5 * 60 * 1000));
        given()
                .headers(Util.setHeader(data))
                .body(jsonObject)
                .post("investment/stock/orderPlacing")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/orderPlacing").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "stock"})
    public void investmentSettlementAccountUpdatePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/settlementAccountUpdate.txt"))
                .post("investment/stock/settlementAccountUpdate")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/settlementAccountUpdate").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "stock"})
    public void investmentStkAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/stkAccountOpening.txt"))
                .post("investment/stock/stkAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/stkAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "stock"})
    public void investmentStockListGet() {
        given()
                .headers(Util.setHeader(data))
                .get("investment/stock/stockList")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/stockList").using(jsonschemaemaFactory));
    }

    /**
     * @v-mutual
     */
    @Test(enabled = true, groups = {"all", "v-mutual"})
    public void investmentFundQuotationGet() {
        String fundcode = "U000001";
        given()
                .headers(Util.setHeader(data))
                .get("investment/fund/fundQuotation/" + fundcode)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/fundQuotation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "v-mutual"})
    public void investmentStockQuotationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/investment/stockQuotation.txt"))
                .post("investment/stock/stockQuotation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/investment/stockQuotation").using(jsonschemaemaFactory));
    }
}

