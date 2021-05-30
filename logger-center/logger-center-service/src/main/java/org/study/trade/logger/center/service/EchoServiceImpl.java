package org.study.trade.logger.center.service;

import org.apache.dubbo.config.annotation.Service;
import org.study.trade.logger.center.api.EchoService;

/**
 * @author Tomato
 * Created on 2021.05.30
 */
@Service(version = "${tomato.service.version}")
public class EchoServiceImpl implements EchoService {

    public String echo(String str) {
        return str;
    }
}
