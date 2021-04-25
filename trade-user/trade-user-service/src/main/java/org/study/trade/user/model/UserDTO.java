package org.study.trade.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(
            message = "nick name can't be blank",
            groups = {Register.class})
    private String nick;

    @NotNull(
            message = "gender can't be null",
            groups = {Register.class})
    private Byte gender;

    private Integer telephone;

    @NotBlank(
            message = "password can't be null",
            groups = {Register.class, Login.class})
    private String password;

    private Date createTime;

    private Date updateTime;

    public interface Register {
    }

    public interface Login {
    }
}
