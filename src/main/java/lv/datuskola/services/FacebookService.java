package lv.datuskola.services;

import lv.datuskola.auth.PropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacebookService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String FB_APP_ID = "273875460184611";

    private Map<String, String> validTokens = new HashMap<>();

    @Autowired
    public PropertyProvider propertyProvider;

    public String isValid(String token) {
        String userId = validTokens.get(token);
        if (userId != null) {
            return userId;
        }

        var fb = new FacebookService();
        var accessTokenURL = fb.accessTokenURL(propertyProvider.get("facebook"));
        JsonObject response = fb.getGraph(accessTokenURL);
        var accessToken = response.get("access_token").toString().replace("\"", "");

        var debugURL = fb.debugURL(
                token,
                accessToken);
        response = fb.getGraph(debugURL);

        var jsonObject = response.get("data").asJsonObject();
        String isValid = jsonObject.get("is_valid").toString();

        if("true".equals(isValid)) {
            var user_id = jsonObject.get("user_id").toString().replace("\"", "");
            validTokens.put(token, user_id);
            return user_id;
        } else {
            logger.error("Facebook service "+ jsonObject.get("error").asJsonObject().toString());
        }
        return null;
    }

    private String accessTokenURL(String secret) {
        return "https://graph.facebook.com/oauth/access_token" +
                "?client_id=" + FB_APP_ID +
                "&client_secret=" + secret +
                "&grant_type=client_credentials";
    }

    private String debugURL(String token, String accessToken) {
        String fbLoginUrl = "https://graph.facebook.com/debug_token?"
                + "input_token=" + token
                + "&access_token=" + accessToken
                + "&scope=email";
        return fbLoginUrl;
    }

    private JsonObject getGraph(String url) {
        URL fbGraphURL;
        try {
            fbGraphURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid code received ", e);
        }
        StringBuffer b = null;
        try {
            var fbConnection = fbGraphURL.openConnection();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(
                    fbConnection.getInputStream()));
            String inputLine;
            b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect with Facebook ", e);
        }

        var object = getGraphData(b.toString());
        return object;
    }

    private JsonObject getGraphData(String fbGraph) {
        var jsonReader = Json.createReader(new StringReader(fbGraph));
        var object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }
}
