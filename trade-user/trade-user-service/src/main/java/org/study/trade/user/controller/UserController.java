package org.study.trade.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.common.error.BusinessException;
import org.study.trade.common.response.CommonResponse;
import org.study.trade.user.ErrorEnum;
import org.study.trade.user.constants.ApiPath;
import org.study.trade.user.model.UserDTO;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@RestController
public class UserController {

    @PostMapping(ApiPath.REGISTER)
    public CommonResponse<UserDTO> register(@RequestBody UserDTO userDTO) throws BusinessException {
        StringBuilder infoBuilder = new StringBuilder(0);
        if (!UserDTO.isValid(userDTO, infoBuilder)) {
            throw new BusinessException(
                    ErrorEnum.REGISTER_REQUEST_BODY_ERROR.create(infoBuilder.toString()));
        }
        return CommonResponse.success(userDTO);
    }
}
