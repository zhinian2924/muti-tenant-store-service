package com.example.storesaas.identity.auth.dto;

/**
 * Login DTO.
 * @param username
 * @param mobile
 * @param password
 * @param code
 * @param loginType
 */
public record LoginDTO(
        String username,
        String mobile,
        String password,
        String code,
        String loginType) {
}
