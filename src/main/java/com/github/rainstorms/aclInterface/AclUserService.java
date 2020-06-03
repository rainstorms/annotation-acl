package com.github.rainstorms.aclInterface;

import com.github.rainstorms.domain.AclUser;

import java.util.List;

public interface AclUserService {

    AclUser findUser(String userId);

    List<String> findUserRole(String userId);

}
