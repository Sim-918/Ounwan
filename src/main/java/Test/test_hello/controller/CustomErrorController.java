package Test.test_hello.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null) {
            int status = Integer.parseInt(statusCode.toString());
            if (status == 404) {
                return "error/404"; // → templates/error/404.html
            }
            if (status == 500) {
                return "error/404"; // → templates/error/404.html
            }
            //여기에 500(서버에러), 403(접근 금지)를 추가하고
        }
        // 여기선 etc(예상 못한 오류)를 default를 출력하면 됨
        return "error/404";
    }
}
