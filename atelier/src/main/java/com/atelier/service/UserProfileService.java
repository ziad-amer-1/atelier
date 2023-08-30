package com.atelier.service;

import com.atelier.dto.ChangePasswordDTO;
import com.atelier.dto.UserDTO;
import com.atelier.entity.AppUser;

public interface UserProfileService {
    UserDTO getUserInfo(String token);
    String updateUserPassword(String token, ChangePasswordDTO changePasswordDTO);

}
