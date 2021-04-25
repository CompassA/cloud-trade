package org.study.trade.user.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.user.model.UserDTO;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@RestController
public class UserController {

    @PostMapping("/trade/api/user/register")
    public UserDTO register(
            @Validated({UserDTO.Register.class}) @RequestBody UserDTO userDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            for (FieldError fieldError : result.getFieldErrors()) {
                builder.append(fieldError.getDefaultMessage()).append("   ");
            }
            return new UserDTO().setNick(builder.toString());
        }
        return userDTO;
    }
}
