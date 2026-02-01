package com.ecommerce.ecommerce_backend.auth.dto;
import com.ecommerce.ecommerce_backend.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
  private String password;

    private String role; // USER / SELLER

}

