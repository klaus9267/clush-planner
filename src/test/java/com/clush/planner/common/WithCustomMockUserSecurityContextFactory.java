package com.clush.planner.common;

import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.NoSuchElementException;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
  @Autowired
  UserRepository userRepository;

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
    String uid = annotation.uid();
    String role = annotation.role();
    User user = userRepository.findByUid(uid).orElseThrow(NoSuchElementException::new);

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority(role)));
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(token);
    return context;
  }
}
