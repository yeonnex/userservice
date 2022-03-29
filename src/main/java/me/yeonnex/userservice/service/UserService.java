package me.yeonnex.userservice.service;

import me.yeonnex.userservice.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
