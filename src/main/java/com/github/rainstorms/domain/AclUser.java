package com.github.rainstorms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class AclUser {
    private String userId;
    private List<String> userRoles;
}
