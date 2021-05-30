package org.study.trade.user.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.logger.center.api.EchoService;

/**
 * @author Tomato
 * Created on 2021.05.30
 */
@RestController
public class UserTestController {

    @Reference(version = "${service-list.echo-service-version}")
    private EchoService echoService;

    @GetMapping("/trade/user/test")
    public String echo(@Param("str") String str) {
        return echoService.echo(str);
    }
}
