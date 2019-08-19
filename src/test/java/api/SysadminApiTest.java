package api;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import common.JsonSchemaUtils;
import common.Util;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import model.DataVO;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SysadminApiTest {
    JsonSchemaFactory jsonschemaemaFactory = null;

    DataVO data = new DataVO();
    int port;

    @BeforeClass(groups = {"all"})
    public void setUp() {
        RestAssured.baseURI = "http://3.130.122.199:8086/sysadmin/";
        jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

    /**
     * @admin-controller
     */
    @Test(enabled = true, groups = {"all", "admin-controller"})
    public void sysadminAdminControllerLoginInPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/loginIn.txt"))
                .post("sysadmin/adminuser/loginIn")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/loginIn").using(jsonschemaemaFactory));
    }

    /**
     * @authority-controller
     */
    @Test(enabled = true, groups = {"all", "authority-controller"})
    public void sysadminAuthorityControllerAppDockerForDeveloperPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/appDockerForDeveloper.txt"))
                .post("sysadmin/permission/appDockerForDeveloper")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/appDockerForDeveloper").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "authority-controller"})
    public void sysadminAuthorityControllerDeveloperAuthorizationPost() throws IOException {
        JSONObject requestObj = Util.getObject("/request/sysadmin/developerAuthorization.txt");
        requestObj.put("developerID", Util.getRandomCustomerId());
        given()
                .headers(Util.setHeader(data))
                .body(requestObj)
                .post("sysadmin/permission/developerAuthorization")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/developerAuthorization").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "authority-controller"})
    public void sysadminAuthorityControllerGetSandBoxPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/getSandBox.txt"))
                .post("sysadmin/permission/getSandBox")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getSandBox").using(jsonschemaemaFactory));
    }

    /**
     * @branch-code-search-controller
     */
    @Test(enabled = true, groups = {"all", "branch-code-search-controller"})
    public void sysadminBranchCodeSearchControllerGetBranchCodeTableGet() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .get("sysadmin/branchdata/getBranchCodeTable")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getBranchCodeTable").using(jsonschemaemaFactory));
    }

    //there are too many response data, so the api is slow.
    @Test(enabled = false, groups = {"all", "branch-code-search-controller", "error"})
    public void sysadminBranchCodeSearchControllerGetBranchCodeTableInfoPost() throws IOException {
//        "SandBoxId": 9
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/getBranchCodeTableInfo.txt"))
                .post("sysadmin/branchdata/getBranchCodeTableInfo")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getBranchCodeTableInfo").using(jsonschemaemaFactory));
    }

    /**
     * @currency-controller
     */
    @Test(enabled = true, groups = {"all", "currency-controller"})
    public void sysadminCurrencyControllerCurrencyRetrievalGet() {
        given()
                .headers(Util.setHeader(data))
                .get("sysadmin/currency/currencyRetrieval")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/currencyRetrieval").using(jsonschemaemaFactory));
    }

    /**
     * @generate-available-controller
     */
    @Test(enabled = true, groups = {"all", "generate-available-controller"})
    public void sysadminGenerateAvailableControllerlGetNextAvailableNumberGet() {
        String item = "NextAvailableCustomerNumber";
        given()
                .header("token", "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA")
                .get("sysadmin/generate/getNextAvailableNumber/" + item)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getNextAvailableNumber").using(jsonschemaemaFactory));
    }

    /**
     * @lbs-sdk-controller
     */
    //This is ignored for the time being. This priority is very low.
    @Test(enabled = false, groups = {"all", "generate-available-controller", "error"})
    public void sysadminLbsSdkControllerDownloadsdkGet() {
        given()
                .header("content-type", "application/json")
                .header("token", "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA")
                .get("sysadmin/downloadsdk")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/downloadsdk").using(jsonschemaemaFactory));
    }

    /**
     * @login-controller
     */
    @Test(enabled = true, groups = {"all", "login-controller"})
    public void sysadminLoginControllerAuthorizeGet() {
        String loginPK = "67e6cc4d8cdc4ba48768968542bfe2d4";
        given()
                .header("content-type", "application/json")
                .header("token", "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA")
                .get("sysadmin/login/authorize/" + loginPK)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/authorize").using(jsonschemaemaFactory));
    }


    @Test(enabled = true, groups = {"all", "login-controller"})
    public void sysadminLoginControllerLoginInPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/loginIn.txt"))
                .post("sysadmin/login/loginIn")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/loginIn").using(jsonschemaemaFactory));
    }

    //this api can't be automated as the customerNumber must not in the sysadmin->t_login_in and in the table deposit->t_termdeposit_master.
    //now no have one api to get the customernumber from the DB.
    @Test(enabled = false, groups = {"all", "login-controller", "error"})
    public void sysadminLoginControllerUserCreationPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/userCreation.txt"))
                .post("sysadmin/login/userCreation")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/userCreation").using(jsonschemaemaFactory));
    }

    /**
     * @sand-box-search-controller
     */
    @Test(enabled = true, groups = {"all", "sand-box-search-controller"})
    public void sysadminSandBoxSearchControllerGetSandBoxTableGet() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .get("sysadmin/sandbox/getSandBoxTable")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getSandBoxTable").using(jsonschemaemaFactory));
    }

    @Test(enabled = true, groups = {"all", "sand-box-search-controller", "error"})
    public void sysadminSandBoxSearchControllerGetSandBoxTableInfoPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/getSandBoxTableInfo.txt"))
                .post("sysadmin/sandbox/getSandBoxTableInfo")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getSandBoxTableInfo").using(jsonschemaemaFactory));
    }

    /**
     * @sys-config-controller
     */
    @Test(enabled = true, groups = {"all", "sys-config-controller"})
    public void sysadminSysConfigControllerGetSystemParameterPost() throws IOException {
        given()
                .headers(Util.setHeader(data))
                .body(Util.getObject("/request/sysadmin/getSystemParameter.txt"))
                .post("sysadmin/sysconfig/getSystemParameter")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("response/sysadmin/getSystemParameter").using(jsonschemaemaFactory));
    }


}
