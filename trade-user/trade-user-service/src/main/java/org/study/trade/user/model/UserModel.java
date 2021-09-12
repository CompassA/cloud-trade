package org.study.trade.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
@Setter
public class UserModel implements UserDetails {

    private Long id;

    private String nick;

    private Byte gender;

    private Long telephone;

    private String password;

    private Date createTime;

    private Date updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(0);
    }

    @Override
    public String getUsername() {
        return this.nick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
