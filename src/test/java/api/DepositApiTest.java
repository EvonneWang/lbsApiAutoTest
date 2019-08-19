package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import common.JsonSchemaUtils;
import io.restassured.RestAssured;
import model.DataVO;
import common.Util;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class DepositApiTest {

    JsonSchemaFactory jsonschemaemaFactory = null;
    DataVO data = new DataVO();

    DepositApiTest() {
        data.setToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
        data.setClientid("devin");
        data.setMessageid("006f7113e5fa48559549c4dfe74e2cd6");
    }

    @BeforeClass(groups = {"all"})
    public void setUp() {
        RestAssured.baseURI = "http://3.130.122.199/8086/deposit/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    /***
     * @account-enquiry
     */
    @Test(enabled = true, groups = {"all", "account-enquiry"})
    public void depositAccountEnquiryAccountDetailsGet() throws IOException {
        String accountNumber = "HK720001001000000001001";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/account/accountDetails/" + accountNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountDetails").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-enquiry"})
    public void depositAccountEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/accountNumberValidation.txt"))
                .post("deposit/account/accountNumberValidation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountNumberValidation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-enquiry"})
    public void depositAccountEnquiryAllAccountGet() {
        String customerNumber = "001000000001";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/account/allAccounts/" + customerNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/allAccounts").using(jsonschemaemaFactory));
    }

    /**
     * @account-maintenance
     */
    @Test(enabled = true, groups = {"all", "account-maintenance"})
    public void depositAccountMaintenanceAccountClosureGet() {
        String accountNumber = "HK920001001000006239002";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/account/accountClosure/" + accountNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountClosure").using(jsonschemaemaFactory));
    }

    /**
     * @account-opening
     */
    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositAccountOpeningAccountCreationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/accountCreation.txt"))
                .post("deposit/account/accountCreation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositCurrentAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/currentAccountOpening.txt"))
                .post("deposit/account/currentAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/currentAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositFeAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/feAccountOpening.txt"))
                .post("deposit/account/feAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/feAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositMetAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/metAccountOpening.txt"))
                .post("deposit/account/metAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/metAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositSavingAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/savingAccountOpening.txt"))
                .post("deposit/account/savingAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/savingAccountOpening").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "account-opening"})
    public void depositTdAccountOpeningPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/tdAccountOpening.txt"))
                .post("deposit/account/tdAccountOpening")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/tdAccountOpening").using(jsonschemaemaFactory));
    }

    /**
     * @customer-maintenance
     */
    @Test(enabled = true, groups = {"all", "customer-maintenance"})
    public void depositCusMainCustContactInfoUpdatePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/custContactInfoUpdate.txt"))
                .post("deposit/account/custContactInfoUpdate")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/custContactInfoUpdate").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "customer-maintenance"})
    public void depositCusMainCustomerCreationPost() throws IOException {
        JSONObject requestObj = Util.getObject("/request/deposit/customerCreation.txt");
        requestObj.put("customerID", "U" + Util.getRandomCustomerId() + "(1)");
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("deposit/account/customerCreation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/customerCreation").using(jsonschemaemaFactory));
    }

    /**
     * Term Deposit API*
     *
     * @TermDeposit
     */
    @Test(enabled = true, groups = {"all", "TermDeposit"})
    public void depositTermDepositAllTermDepositGet() throws IOException {
        String customerNumber = "001000000001";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/term/allTermDeposit/" + customerNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/allTermDeposit").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "TermDeposit"})
    public void depositTermDepositAllEnquiryCustomerNumberGet() {
        String accountNumber = "HK760001001000000005100";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/term/termDeposit/" + accountNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/termDeposit").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "TermDeposit"})
    public void depositTermDepositTermDepositApplicationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/termDepositApplication.txt"))
                .post("deposit/term/termDepositApplication")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/termDepositApplication").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "TermDeposit", "error"})
    public void depositTermDepositTermDepositDrawDownPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/termDepositDrawDown.txt"))
                .post("deposit/term/termDepositDrawDown")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/termDepositDrawDown").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "TermDeposit"})
    public void depositTermDepositTermDepositEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/termDepositEnquiry.txt"))
                .post("deposit/term/termDepositEnquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/termDepositEnquiry").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "TermDeposit", "error"})
    public void depositTermDepositTermDepositRenewalPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/termDepositRenewal.txt"))
                .post("deposit/term/termDepositRenewal")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/termDepositRenewal").using(jsonschemaemaFactory));
    }

    /**
     * @Transaction
     */
    @Test(enabled = true, groups = {"all", "Transaction"})
    public void depositTransactionChequeBookCreationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/chequeBookCreation.txt"))
                .post("deposit/account/chequeBookCreation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/chequeBookCreation").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Transaction"})
    public void depositTransactionDepositPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/deposit.txt"))
                .post("deposit/account/deposit")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/deposit").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Transaction"})
    public void depositTransactionTransferPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/transfer.txt"))
                .post("deposit/account/transfer")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/transfer").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Transaction"})
    public void depositTransactionWithdrawalPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/withdrawal.txt"))
                .post("deposit/account/withdrawal")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/withdrawal").using(jsonschemaemaFactory));
    }

    /**
     * @Transaction History
     */

    @Test(enabled = true, groups = {"all", "Transaction History", "error"})
    public void depositTransactionLogEnquiryPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/enquiry.txt"))
                .post("deposit/transactionLog/enquiry")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/enquiry").using(jsonschemaemaFactory));
    }

    /**
     * @Vaccount
     */
    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountAccountBalancePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/accountBalance.txt"))
                .post("deposit/validate/accountBalance")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountBalance").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountAccountNumberExistsPost() throws IOException {
        String accountNumber = "HK720001001000000001001";
        given()
                .headers(Util.setHeader(data))
                .post("deposit/validate/accountNumberExists/" + accountNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/accountNumberExists").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountAmountFormatPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/amountFormat.txt"))
                .post("deposit/validate/amountFormat")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/amountFormat").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountAssociatedAccountsPost() throws IOException {
        String accountNumber = "HK720001001000000001001";
        given()
                .headers(Util.setHeader(data))
                .post("deposit/validate/associatedAccounts/" + accountNumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/associatedAccounts").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountCurAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/curAccountType.txt"))
                .post("deposit/validate/curAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/curAccountType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountCurrencyGet() throws IOException {
        String currency = "HKD";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/currency/" + currency)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/currency").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountFexAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/fexAccountType.txt"))
                .post("deposit/validate/fexAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/fexAccountType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountFundAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/fundAccountType.txt"))
                .post("deposit/validate/fundAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/fundAccountType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountMetAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/metAccountType.txt"))
                .post("deposit/validate/metAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/metAccountType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountSavOrCurTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/savOrCurType.txt"))
                .post("deposit/validate/savOrCurType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/savOrCurType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountStockAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/stockAccountType.txt"))
                .post("deposit/validate/stockAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/stockAccountType").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vaccount"})
    public void depositVaccountTdAccountTypePost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/tdAccountType.txt"))
                .post("deposit/validate/tdAccountType")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/tdAccountType").using(jsonschemaemaFactory));
    }

    /**
     * @Vcustomer
     */
    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVcustomerEmailFormatPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/deposit/emailFormat.txt"))
                .post("deposit/validate/emailFormat")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/emailFormat").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVcustomerExistingCustomerGet() throws IOException {
        String customerID = "U735535(9)";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/existingCustomer/" + customerID)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/existingCustomer").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVcustomerIdFormatGet() {
        String customerID = "U735535(9)";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/idFormat/" + customerID)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/idFormat").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVcustomerPhoneNumberFormatGet() {
        String phone = "64657884";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/phoneNumberFormat/" + phone)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/phoneNumberFormat").using(jsonschemaemaFactory));
    }


    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVtermDepositContractPeriodGet() {
        String period = "1day";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/contractPeriod/" + period)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/contractPeriod").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVtermDepositTdNumberExistsGet() {
        String tdnumber = "000000001";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/tdNumberExists/" + tdnumber)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/tdNumberExists").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "Vcustomer"})
    public void depositVtransactionTransTypeGet() {
        String transtype = "0001";
        given()
                .headers(Util.setHeader(data))
                .get("deposit/validate/transType/" + transtype)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/deposit/transType").using(jsonschemaemaFactory));
    }
    /**
     * this api has been deleted
     */
//    @Test(enabled = false, groups = {"all", "Vcustomer"})
//    public void depositVtransactionDateFormatGet() {
//        String date = "2019-03-12";
//        given()
//                .headers(Util.setHeader(data))
//                .get("deposit/validate/dateFormat/" + date)
//                .then()
//                .statusCode(200)
//                .body(matchesJsonSchemaInClasspath("response/deposit/dateFormat").using(jsonschemaemaFactory));
//    }

////    @Test(enabled = true)
////    public void depositVtransactionDateRangePost() {
////        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
////        HttpPost post = new HttpPost(baseURL + "deposit/validate/dateRange");
////        setPostHeader(post, data);
////
////        try {
////            post.setEntity(new StringEntity(Util.getStringValue("/request/deposit/dateRange.txt"), "UTF-8"));
////            CloseableHttpResponse response = httpClient.execute(post);
////            String str = EntityUtils.toString(response.getEntity());
////            Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
////            JsonSchemaUtils.assertResponseJsonSchema("/response/deposit/dateRange", str);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
//
////    @Test(enabled = false)
////    public void depositVtransactionTimeFormatGet() {
////        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
////        String time = "2019-04-23 15:52:43";
////        try {
////            HttpGet get = new HttpGet(baseURL + "deposit/validate/timeFormat/" + URLEncoder.encode(time).replace("+", "%20"));
////            setGetHeader(get, data);
////            CloseableHttpResponse response = httpClient.execute(get);
////            String str = EntityUtils.toString(response.getEntity());
////            JsonSchemaUtils.assertResponseJsonSchema("/response/deposit/timeFormat", str);
////            Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//

}
