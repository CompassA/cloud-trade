package org.study.trade.user.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.common.error.BusinessException;
import org.study.trade.common.response.CommonResponse;
import org.study.trade.user.ErrorEnum;
import org.study.trade.user.constants.ApiPath;
import org.study.trade.user.model.UserDTO;
import org.study.trade.user.model.UserModel;
import org.study.trade.user.service.UserService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping(ApiPath.USER_REGISTER)
    public CommonResponse<UserDTO> register(@RequestBody UserDTO userDTO) throws BusinessException {
        StringBuilder infoBuilder = new StringBuilder(0);
        if (!UserDTO.isValid(userDTO, infoBuilder)) {
            throw new BusinessException(
                    ErrorEnum.REGISTER_REQUEST_BODY_ERROR.create(infoBuilder.toString()));
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);

        UserModel responseModel = userService.register(userModel);

        UserDTO responseDTO = new UserDTO();
        BeanUtils.copyProperties(responseModel, responseDTO);

        return CommonResponse.success(responseDTO);
    }

    @PostMapping(ApiPath.USER_LOGIN)
    public CommonResponse<String> login(@RequestBody UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getNick(),
                        userDTO.getPassword()
                )
        );
        UserDTO responseDTO = new UserDTO();
        BeanUtils.copyProperties(authentication.getPrincipal(), responseDTO);
        responseDTO.setPassword(null);
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, responseDTO);
        redisTemplate.expire(token, 1, TimeUnit.DAYS);
        return CommonResponse.success(token);
    }

    @GetMapping("/test")
    public CommonResponse<Object> test() {
        return CommonResponse.success(null);
    }
}
