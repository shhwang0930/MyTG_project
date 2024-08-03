package com.shhwang0930.mytg.user.model.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    String oldPassword;
    String newPassword;
}
