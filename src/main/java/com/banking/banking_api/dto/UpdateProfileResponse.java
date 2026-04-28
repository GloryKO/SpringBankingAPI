package com.banking.banking_api.dto;
import com.banking.banking_api.entity.User;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UpdateProfileResponse {
    private String name;
    private String phoneNumber;
    private String address;
    private String countryCode;
    private String email;

    public static UpdateProfileResponse fromEntity(User user) {
        return UpdateProfileResponse.builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .countryCode(user.getCountryCode())
                .email(user.getEmail())
                .build();
    }
}
