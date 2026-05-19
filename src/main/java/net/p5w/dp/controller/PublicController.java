package net.p5w.dp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.Result;

@RestController
public class PublicController {

    @GetMapping("/")
    public Result<String> index(@RequestAttribute("requestId") String requestId) {
        Result<String> result = Result.success("p5w dp api running ok");
        result.setRequestId(requestId);
        return result;
    }
}