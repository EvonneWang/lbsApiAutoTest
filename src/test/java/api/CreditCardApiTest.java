package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import common.Util;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import model.DataVO;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
//import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreditCardApiTest {
    DataVO data = new DataVO();
    JsonSchemaFactory jsonschemaemaFactory = null;

    CreditCardApiTest() {
        data.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
    }

    @BeforeClass(groups = {"all"})
    public void setUp() {
        RestAssured.baseURI = "http://3.130.122.199:8086/creditcard/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    private void getNewCreditCard() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://3.130.122.199:8086/creditcard/creditcard/accountOpening");
        post.setHeader("token", data.getToken());
        post.setHeader("content-type", "application/json");
        try {
            post.setEntity(new StringEntity(Util.getStringValue("/request/creditCard/accountOpening.txt"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(post);
            String str = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject().fromObject(str);
            data.setCreditCardNumber(jsonObject.get("creditcardnumber").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @credit-card
     */
    @Test(enabled = false, groups = {"all", "credit-card"})
    public void creditCardAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/accountOpening.txt"))
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .post("creditcard/accountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/accountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardCanCellationPost() throws IOException {
        getNewCreditCard();
        JSONObject requestObj = Util.getObject("/request/creditCard/canCellation.txt");
        requestObj.put("creditcardnumber", data.getCreditCardNumber());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("creditcard/cancellation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/cancellation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardCreditLimitDetailsPost() throws IOException {
        getNewCreditCard();
        JSONObject requestObj = Util.getObject("/request/creditCard/creditLimitDetails.txt");
        requestObj.put("creditcardnumber", data.getCreditCardNumber());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("creditcard/creditLimitDetails")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/creditLimitDetails").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardLimitDecreasePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/limitDecrease.txt"))
                .post("creditcard/limitDecrease")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/limitDecrease").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardLimitIncreasePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/limitIncrease.txt"))
                .post("creditcard/limitIncrease")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/limitIncrease").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardLossReportingPost() throws IOException {
        getNewCreditCard();
        JSONObject requestObj = Util.getObject("/request/creditCard/lossReporting.txt");
        requestObj.put("creditcardnumber", data.getCreditCardNumber());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("creditcard/lossReporting")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/lossReporting").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card"})
    public void creditCardNumberValidationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/numberValidation.txt"))
                .post("creditcard/numberValidation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditcard/numberValidation").using(jsonschemaemaFactory));
    }

    /**
     * @credit-card-payment
     */
    @Test(enabled = true, groups = {"all", "credit-card-payment"})
    public void creditCardPaymentCreditCardRepeymentPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/creditCardRepayment.txt"))
                .post("creditcard/creditCardRepayment")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/creditCardRepayment").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card-payment"})
    public void creditCardPaymentOutstandingPaymentPost() throws IOException {
        getNewCreditCard();
        JSONObject requestObj = Util.getObject("/request/creditCard/outstandingPayment.txt");
        requestObj.put("creditcardnumber", data.getCreditCardNumber());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("creditcard/outstandingPayment")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/outstandingPayment").using(jsonschemaemaFactory));
    }

    /**
     * @credit-card-rewards
     */
    @Test(enabled = true, groups = {"all", "credit-card-rewards"})
    public void creditCardPaymentProductEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/productEnquiry.txt"))
                .post("point/productEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/productEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card-rewards"})
    public void creditCardPaymentRedemptionPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/redemption.txt"))
                .post("point/redemption")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/redemption").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card-rewards"})
    public void creditCardPaymentRedemptionHistoryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/redemptionHistory.txt"))
                .post("point/redemptionHistory")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/redemptionHistory").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card-rewards"})
    public void creditCardPaymentTotalPointPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/totalPoint.txt"))
                .post("point/totalPoint")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/totalPoint").using(jsonschemaemaFactory));
    }

    /**
     * @credit-card-transaction
     */
    @Test(enabled = true, groups = {"all", "credit-card-transaction"})
    public void creditCardTransactiontRansactionDetailsPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/transactionDetails.txt"))
                .post("creditcard/transactionDetails")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/transactionDetails").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "credit-card-transaction"})
    public void creditCardTransactiontTransactionPostingPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/transactionPosting.txt"))
                .post("creditcard/transactionPosting")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/transactionPosting").using(jsonschemaemaFactory));
    }

    /**
     * @merchant
     */
    @Test(enabled = true, groups = {"all", "merchant"})
    public void merchantMerchantEnquiryGet() throws IOException {
        String merchantNumber = "HK0001001000009";
        given()
                .headers(Util.setHeader(data))
                .get("merchant/merchantEnquiry/" + merchantNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/merchantEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "merchant"})
    public void merchantTransactionsPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/transactions.txt"))
                .post("merchant/transactions")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/transactions").using(jsonschemaemaFactory));
    }

    /**
     * @vpoint
     */
    @Test(enabled = true, groups = {"all", "vpoint"})
    public void vpointMerchantCategoryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/merchantCategory.txt"))
                .post("validate/merchantCategory")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/merchantCategory").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "vpoint"})
    public void vpointNumberPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/creditCard/number.txt"))
                .post("validate/number")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/creditCard/number").using(jsonschemaemaFactory));
    }

}

