package org.study.trade.logger.center.api;

import java.util.List;

/**
 * @author Tomato
 * Created on 2021.07.03
 */
public interface DemoService {

    List<String> split(String str, String regex);

    String merge(List<String> strList, char separator);
}
