package lv.datuskola.services;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Recaptcha {

    public static boolean isGoodCaptcha(String captcha) throws IOException{
        URL url = new URL ("https://www.google.com/recaptcha/api/siteverify");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()){
            var input = fun2(captcha).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        if(code != 200) {
            return false;
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JsonObject jsonObject = getGraphData(response.toString());

            if(jsonObject.getBoolean("success") &&
                    jsonObject.getJsonNumber("score").doubleValue() > 0.7) {
                return true;
            }
        }
        return false;
    }

    private static JsonObject getGraphData(String fbGraph) {
        var jsonReader = Json.createReader(new StringReader(fbGraph));
        var object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    private static String fun2(String captcha) {
        var params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("secret", "6LdfLOEUAAAAAIkIm28RG8oa9pq7r3iSVyWll4ua"));
        params.add(new NameValuePair("response", captcha));
        return getQuery(params);
    }

    static private String getQuery(List<NameValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.name, StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(pair.value, StandardCharsets.UTF_8));
        }

        return result.toString();
    }

}