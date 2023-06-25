package dev.be.boardadminproject.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            model.addAttribute("request", request.getRequestURI());
            model.addAttribute("response", statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value() ||
                    statusCode == HttpStatus.FORBIDDEN.value() ||
                    statusCode == HttpStatus.BAD_REQUEST.value() ||
                    statusCode == HttpStatus.UNAUTHORIZED.value()
            ) {
                return "error/4xx";
            }

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() ||
                    statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()
            ) {
                return "error/5xx";
            }
        }

        return "error";
    }


}
