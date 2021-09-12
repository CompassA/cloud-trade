package org.study.trade.user.mapper;

import org.study.trade.user.mapper.data.UserData;

public interface UserDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserData record);

    int insertSelective(UserData record);

    UserData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserData record);

    int updateByPrimaryKey(UserData record);

    UserData selectByNickName(String nickName);
}