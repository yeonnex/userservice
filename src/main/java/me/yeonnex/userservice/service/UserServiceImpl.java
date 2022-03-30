package me.yeonnex.userservice.service;

import me.yeonnex.userservice.dto.UserDto;
import me.yeonnex.userservice.jpa.UserEntity;
import me.yeonnex.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public UserDto getUserDetailsByEmail(String userEmail) {
        UserEntity userEntity = userRepository.findByEmail(userEmail);
        if (userEntity == null)
            throw new UsernameNotFoundException(userEmail);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

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

        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPassword()));

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // <- 내 서비스는 username 을 검색해오지 않고,
        System.out.println("loadUserByUsername 호출" + username);
        UserEntity user = userRepository.findByEmail(username);// <- 변수 이름만 username 이지, 이메일 값이 들어있음. 이메일 값을 전달할 것이기 때문 // email 을 검색해올 것임
        // 이메일이 DB에 없다면, 예외 던짐 🤾‍
        if (user == null){
            throw new UsernameNotFoundException(user.getEmail());
        }
        UserEntity userEntity = new ModelMapper().map(user, UserEntity.class);

        // 이메일이 DB에 있다면, UserDetails 객체 반환! UserDetails 객체는 스프링에서 제공하는 User 클래스에서 만들 수 있음
        System.out.println("userEntity.getEmail() = " + userEntity.getEmail());

        // User 는, "검색된 엔티티의 이메일"과 "검색된 엔티티의 암호화된 비번"과 각종 "권한리스트"를 가지고 있는 유저 객체이다
        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true,true,true, new ArrayList<>()); // 마지막 인자인 리스트에는 로그인이 되었을 때 할 수 있는 작업 중 권한 부여. 일단 지금은 권한 아무것도 설정한게 없으므로 빈 리스트 전달.
    }
}
