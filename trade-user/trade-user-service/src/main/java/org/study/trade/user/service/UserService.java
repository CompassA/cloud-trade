package org.study.trade.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.study.trade.common.error.BusinessException;
import org.study.trade.user.ErrorEnum;
import org.study.trade.user.mapper.UserDataMapper;
import org.study.trade.user.mapper.data.UserData;
import org.study.trade.user.model.UserModel;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
public class UserService implements UserDetailsService {

    private final UserDataMapper userDataMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDataMapper userDataMapper,
                       PasswordEncoder passwordEncoder) {
        this.userDataMapper = userDataMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserModel register(UserModel userModel) throws BusinessException {
        UserData userData = new UserData();
        BeanUtils.copyProperties(userModel, userData);
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        try {
            if (userDataMapper.insertSelective(userData) < 1) {
                throw new BusinessException(ErrorEnum.REGISTER_INSERT_ERROR.create());
            }
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorEnum.PARAMETER_ERROR.create());
        }
        return userModel;
    }

    /**
     * 根据用户名查用户
     * @param username 账户名 or 邮箱
     * @return 用户完整信息
     * @throws UsernameNotFoundException 未找到用户时抛异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData userData = userDataMapper.selectByNickName(username);
        if (userData == null) {
            throw new UsernameNotFoundException("user not found");
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userData, userModel);
        return userModel;
    }
}
