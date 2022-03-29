package me.yeonnex.userservice.service;

import me.yeonnex.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    List<UserDto> getUserAll();
}
