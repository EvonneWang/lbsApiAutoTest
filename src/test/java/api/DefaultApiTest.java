package api;

import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class DefaultApiTest {
    String baseURL = "http://134.175.161.212:8086/sysadmin/";

    @Test(groups = {"1", "2"})
    public void authorityControllerAppDockerForDeveloperPost() {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        JSONObject params = new JSONObject();
        params.put("developerId", "string");
        params.put("dockerId", "string");

        HttpPost post = new HttpPost(baseURL + "sysadmin/permission/appDockerForDeveloper");

        post.setHeader("content-type", "application/json");

        try {
            post.setEntity(new StringEntity(params.toString(), "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(post);
            String res = EntityUtils.toString(response.getEntity());
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test(groups = "1")
    public void authorityControllerDeveloperAuthorizationPost() {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        JSONObject params = new JSONObject();
        params.put("developerID", "111a");
        params.put("developername", "张三");
        params.put("email", "example@chinasofti.com");

        HttpPost post = new HttpPost(baseURL + "sysadmin/permission/developerAuthorization");

        post.setHeader("content-type", "application/json");

        try {
            post.setEntity(new StringEntity(params.toString(), "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(post);
//            String res = EntityUtils.toString(response.getEntity());
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 202);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = "1")
    public void authorityControllerGetSandBoxPost() {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        JSONObject params = new JSONObject();
        params.put("developerId", "5ce51202ebd6453d71d86129");

        HttpPost post = new HttpPost(baseURL + "sysadmin/permission/getSandBox");

        post.setHeader("content-type", "application/json");

        try {
            post.setEntity(new StringEntity(params.toString(), "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(post);
//            String res = EntityUtils.toString(response.getEntity());
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
