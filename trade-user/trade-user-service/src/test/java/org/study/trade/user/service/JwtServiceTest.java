package org.study.trade.user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.study.trade.user.model.UserDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tomato
 * Created on 2021.09.11
 */

public class JwtServiceTest {

    private JwtService jwtService;

    private UserDTO userDTO;

    @Before
    public void init() {
        jwtService = new JwtService();
        Environment mockEnv = mock(Environment.class);
        when(mockEnv.getProperty(any())).thenReturn("wwaFDVveDsasx21dc=634%3&%$$2r");
        jwtService.setEnvironment(mockEnv);

        userDTO = new UserDTO();
        userDTO.setId(2423532L);
        userDTO.setNick("mock nick");
        userDTO.setTelephone(12345678909L);
        userDTO.setGender((byte) 0);
    }

    @Test
    public void parseTest() {
        String jwt = jwtService.generateToken(userDTO);
        UserDTO userInfo = jwtService.getUserInfo(jwt);
        Assert.assertEquals(userInfo.getId(), userDTO.getId());
        Assert.assertEquals(userInfo.getGender(), userDTO.getGender());
        Assert.assertEquals(userInfo.getNick(), userDTO.getNick());
        Assert.assertEquals(userInfo.getTelephone(), userDTO.getTelephone());
    }

}
