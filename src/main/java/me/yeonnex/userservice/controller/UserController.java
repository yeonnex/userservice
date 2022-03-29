package me.yeonnex.userservice.controller;

import me.yeonnex.userservice.dto.UserDto;
import me.yeonnex.userservice.service.UserService;
import me.yeonnex.userservice.vo.RequestUser;
import me.yeonnex.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 회원가입
     * // TODO @param 에 뭐 적으려하면 빨간줄 뜨는데 이거 어케 고치냐... 빌드는 다 되는데 IDE 문제인가...
     * @param
     * @return HTTP 응답과 함께 body 에 UserDto 정보를 리턴합니다
     */
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    /**
     * 전체 유저 조회
     * @param
     * @return body에 ResponseUser 타입의 요소로 이루어진 리스트를 리턴합니다.
     */
    //TODO 페이징으로 유저 정보 불러오기
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getAllUsers(){
        List<UserDto> userAll = userService.getUserAll();
        ModelMapper mapper = new ModelMapper();
        List<ResponseUser> users;
        users = new ArrayList<>();
        userAll.forEach(u -> users.add(new ModelMapper().map(u, ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * id 로 유저 찾기
     * @param id
     * @return ResponseEntity<ResponseUser>
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseUser> getUserById(@PathVariable Long id){
        ModelMapper mapper = new ModelMapper();
        UserDto userById = userService.getUserById(id);
        System.out.println("=====컨트롤러에서 유저 출력========");
        System.out.println(userById);
        
//        UserDto userDto = userById.orElseThrow(() -> new NoSuchElementException());
//        System.out.println("=====컨트롤러에서 유저 출력========");
//        System.out.println(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(userById, ResponseUser.class));
    }

}
