package com.example.storesaas.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 消费者资料更新DTO
 *
 * @param nickname 昵称
 * @param avatarUrl 头像URL
 */
public record CustomerProfileUpdateDTO(
        @NotBlank(message = "昵称不能为空")
        @Size(max = 32, message = "昵称长度不能超过32个字符")
        String nickname,
        @Size(max = 255, message = "头像地址长度不能超过255个字符")
        String avatarUrl
) {
}
