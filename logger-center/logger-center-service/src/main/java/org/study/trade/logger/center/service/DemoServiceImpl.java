package org.study.trade.logger.center.service;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.study.trade.logger.center.api.DemoService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomato
 * Created on 2021.07.03
 */
@Service(version = "${tomato.service.version}")
public class DemoServiceImpl implements DemoService {

    @Override
    public List<String> split(String str, String regex) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(regex)).collect(Collectors.toList());
    }

    @Override
    public String merge(List<String> strList, char separator) {
        if (CollectionUtils.isEmpty(strList)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(0);
        for (int i = 0; i < strList.size(); ++i) {
            builder.append(strList.get(i));
            if (i != strList.size() - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }
}
