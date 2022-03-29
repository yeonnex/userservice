package me.yeonnex.userservice.service;

import me.yeonnex.userservice.dto.UserDto;
import me.yeonnex.userservice.jpa.UserEntity;
import me.yeonnex.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        UserEntity newUser = userRepository.save(userEntity);
        return mapper.map(newUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        System.out.println("===========유저 출력===========");
        System.out.println(user);
        user.orElseThrow(()->new NoSuchElementException());

        // 아래 코드에서 .get() 을 해준 이유는, Optional 타입의 user 를 바로 mapper 로
        // 바꿀 경우 값이 매칭이 다 null 로 떠서임... Optional 타입을 매퍼로 바로 다른
        // 타입으로 바꿀 수 없나봄.. 쩄든 뭐.. 위에서 orElseThrow 로 예외처리 해줬으니 상관은 없을듯하다
        UserEntity u = user.get();
        ModelMapper mapper = new ModelMapper();
        UserDto map = mapper.map(u, UserDto.class);
        System.out.println("===========map 출력===========");
        System.out.println(map);
        return map;
    }

    @Override
    public List<UserDto> getUserAll() {
        List<UserEntity> allUserEntity = (List)userRepository.findAll();
        ModelMapper mapper = new ModelMapper();
        List<UserDto> userDtoList =
                allUserEntity.stream().map(u -> mapper.map(u, UserDto.class)).collect(Collectors.toList());

        return userDtoList;
    }
}
