package org.study.trade.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    private String nick;

    private Byte gender;

    private Long telephone;

    private String password;

    private Date createTime;

    private Date updateTime;

    public static boolean isValid(UserDTO userDTO, StringBuilder stringBuilder) {
        boolean result = true;
        if (StringUtils.isBlank(userDTO.nick)) {
            stringBuilder.append("nick name can't be blank;");
            result = false;
        }
        if (userDTO.gender == null) {
            stringBuilder.append("gender can't be null;");
            result = false;
        }
        if (userDTO.password == null) {
            stringBuilder.append("password can't be null;");
            result = false;
        }
        return result;
    }
}
