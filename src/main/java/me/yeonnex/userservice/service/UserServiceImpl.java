package me.yeonnex.userservice.service;

import me.yeonnex.userservice.dto.UserDto;
import me.yeonnex.userservice.jpa.UserEntity;
import me.yeonnex.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        // TODO
        userEntity.setEncryptedPwd("encrypted_password");

        UserEntity newUser = userRepository.save(userEntity);
        return mapper.map(newUser, UserDto.class);
    }
}
