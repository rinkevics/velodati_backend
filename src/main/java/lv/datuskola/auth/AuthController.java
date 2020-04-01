package lv.datuskola.auth;

import lv.datuskola.services.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Controller
public class AuthController {

    @Autowired
    public FacebookService service;

    @PostMapping("/login")
    @Transactional
    @ResponseBody
    public String login(
            @RequestHeader("token") String token,
            HttpServletResponse response) {

        String userId = service.isValid(token);
        if(userId == null) {
            return "blank";
        }

        Cookie cookie = new Cookie("token", token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "blank";
    }
}
