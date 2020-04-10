package lv.datuskola;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainRecaptcha {

    public static void main(String[] args) throws IOException {
//        PropertyProvider propertyProvider = new PropertyProvider(prop);
    }

    public static void main2 (String []args) throws IOException{
        URL url = new URL ("https://www.google.com/recaptcha/api/siteverify");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()){
            var input = fun2().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        System.out.println(code);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }

    private static String fun2() {
        var params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("secret", "6LdfLOEUAAAAAIkIm28RG8oa9pq7r3iSVyWll4ua"));
        params.add(new NameValuePair("response", "03AERD8Xrl-dxib4e1OgtOX0reS3R1TE_we1l8k09eM83MIvFphsg2B-uEidF385qjQZjltEG2vfgrObAsixXPkWduDeg544Uzerko-jyJ5Atdvrplg-iAeN2MoRGryaCaL5MRFKDYlDEjsOMYhD9L-DOSGTvRBn6MwtGdm8CK06T_EfSkxTSmYnCia2vR1dcsmi3yGCFM73zKUWmxjctuI2UjkkstT9-ftCZUqKOdkEBn4Alaxsdas_v0Gpvopo6PERe625Sfp_peWWPgAovKnHWo-ilLhE7SvXoyR7AwMdT2M_HcpOI7IfXMubYoxW3lDyjih7j0_IyebqqVuzhjw5UnSl2K5-7vy5rz-_6UD2pe6PDyQ1pDQswcjWwIlOBfVDHnM1ses9E3"));
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