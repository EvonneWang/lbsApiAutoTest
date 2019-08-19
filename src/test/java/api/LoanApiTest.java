package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.sf.json.JSONObject;
import common.Util;
import model.DataVO;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoanApiTest {
    JsonSchemaFactory jsonschemaemaFactory = null;
    DataVO data = new DataVO();

    LoanApiTest() {
        data.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
        data.setClientid("devin");
        data.setMessageid("006f7113e5fa48559549c4dfe74e2cd6");
    }

    @BeforeClass(groups = {"all"})
    public void setUp() {
        RestAssured.baseURI = "http://3.130.122.199:8086/loan/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageAccountDetailEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/accountDetailEnquiry.txt"))
                .post("mortgage/accountDetailEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/accountDetailEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"}, dependsOnMethods = "leonMortageMortgageLoanAccountOpeningPost")
    public void leonMortageCancellationPost() throws IOException {
        JSONObject requestObj = Util.getObject("/request/loan/cancellation.txt");
        requestObj.put("accountnumber", data.getLeonAccount());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("mortgage/cancellation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/cancellation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageLoanCalculaterPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/loanCalculater.txt"))
                .post("mortgage/loanCalculater")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/loanCalculater").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageMortgageLoanAccountOpeningPost() throws IOException {
        Response response = given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/mortgageLoanAccountOpening.txt"))
                .post("mortgage/mortgageLoanAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/mortgageLoanAccountOpening").using(jsonschemaemaFactory)).extract().response();
        data.setLeonAccount(response.path("data"));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageMortgageLoanApplicationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/mortgageLoanApplication.txt"))
                .post("mortgage/mortgageLoanApplication")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/mortgageLoanApplication").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageNextRepaymentEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/nextRepaymentEnquiry.txt"))
                .post("mortgage/nextRepaymentEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/nextRepaymentEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageOverDueRepaymentEnquiryPost() throws IOException {
        Response response = given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/overDueRepaymentEnquiry.txt"))
                .post("mortgage/overDueRepaymentEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/overDueRepaymentEnquiry").using(jsonschemaemaFactory)).extract().response();
        data.setTotalpayment(response.path("data.outstandingrepayment.totalpayment[0]").toString());
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"}, dependsOnMethods = "leonMortageOverDueRepaymentEnquiryPost")
    public void leonMortageRepaymentPost() throws IOException {
        JSONObject requestObj = Util.getObject("/request/loan/repayment.txt");
        requestObj.put("repaymentamount", data.getTotalpayment());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("mortgage/repayment")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/repayment").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageRepaymentPlanPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/repaymentPlan.txt"))
                .post("mortgage/repaymentPlan")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/repaymentPlan").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "mortgage-loan"})
    public void leonMortageTransactionEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/loan/transactionEnquiry.txt"))
                .post("mortgage/transactionEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/loan/transactionEnquiry").using(jsonschemaemaFactory));
    }
}
