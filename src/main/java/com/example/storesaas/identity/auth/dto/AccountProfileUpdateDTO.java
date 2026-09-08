package com.example.storesaas.identity.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Account profile update DTO.
 * @param nickname
 * @param password
 */
public record AccountProfileUpdateDTO(
        @NotBlank String nickname,
        String password
) {
}
