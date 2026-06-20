package com.easybus.security;

import com.easybus.entities.User;
import com.easybus.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

  private Long id;

  @Setter private String fullName;

  @Setter private String email;

  @Setter private String username;

  @Setter private String avatarUrl;

  @Setter private String locale;

  @Setter private Boolean emailVerified;

  private Role role;

  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public static UserPrincipal create(User user) {
    var role = user.getRole() == null ? Role.USER : user.getRole();
    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

    return new UserPrincipal(
        user.getId(),
        user.getFullName(),
        user.getEmail(),
        user.getUsername(),
        user.getAvatarUrl(),
        user.getLocale(),
        user.getEmailVerified(),
        role,
        user.getPassword(),
        authorities);
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
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
