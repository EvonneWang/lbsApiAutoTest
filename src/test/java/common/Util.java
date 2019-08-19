package common;

import model.DataVO;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Util {
    public static String getStringValues(File filePath) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");

        BufferedReader br = new BufferedReader(in);
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static String getStringValue(String filePath) throws IOException {
        URL fileUrl = Util.class.getResource(filePath);
        File file = new File(fileUrl.getFile());
        return file.isFile() ? getStringValues(file) : null;
    }

    public static JSONObject getObject(String filePath) throws IOException {
        URL fileUrl = Util.class.getResource(filePath);
        File file = new File(fileUrl.getFile());
//        return JSON.parseObject(getStringValue(filePath), clazz);
        JSONObject obj = new JSONObject().fromObject(getStringValues(file));
        return obj;
    }

    public static int getRandomCustomerId() {
//        int r3 = new Random().nextInt(6);
        return ((int) ((Math.random() * 9 + 1) * 100000));
    }

    //(System.currentTimeMillis()+5*60*1000)
    public static JSONObject setHeader(DataVO dataVO) {
        JSONObject params = new JSONObject();
        params.put("content-type", "application/json");
        if (dataVO.getToken() != null) {
            params.put("token", dataVO.getToken());
        }
        if (dataVO.getMessageid() != null) {
            params.put("messageid", dataVO.getMessageid());
        }
        if (dataVO.getClientid() != null) {
            params.put("clientid", dataVO.getClientid());
        }
        return params;
    }
}
