package org.baoviet.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
