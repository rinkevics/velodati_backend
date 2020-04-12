package lv.datuskola.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public String handleCityNotFoundException(Exception ex, WebRequest request) {
        String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        logger.error("Log exception in " + url, ex);
        return "error";
    }

    static public class StringCleaner extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) {
            if (text == null) {
                setValue(null);
            } else {
                String safe = HtmlUtils.htmlEscape(text);
                setValue(safe);
            }
        }
    }

    @InitBinder
    public void bindStringCleaner(WebDataBinder webDataBinder) {
        StringCleaner stringCleaner = new StringCleaner();
        webDataBinder.registerCustomEditor(String.class, stringCleaner);
    }

}