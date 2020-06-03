package com.github.rainstorms.domain;

import lombok.Data;

import java.util.List;

@Data
public class AclUser {
    private String userId;
    private List<String> userRoles;
}
