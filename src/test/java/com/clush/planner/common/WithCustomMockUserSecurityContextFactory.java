package com.clush.planner.common;

import com.clush.planner.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
    String uid = annotation.uid();
    String role = annotation.role();
    String name = annotation.name();

    User user = User.builder()
        .id(1L)
        .uid(uid)
        .password("test password")
        .name(name)
        .build();

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority(role)));
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(token);
    return context;
  }
}
