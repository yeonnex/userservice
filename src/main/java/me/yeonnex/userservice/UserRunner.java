package me.yeonnex.userservice;

import me.yeonnex.userservice.jpa.UserEntity;
import me.yeonnex.userservice.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserRunner implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserEntity user1 = UserEntity.builder()
                .email("moomoo@naver.com")
                .name("moo")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        userRepository.save(user1);

        UserEntity user2 = UserEntity.builder()
                .email("gamjagamja@gmail.com")
                .name("gamja")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        userRepository.save(user2);

        UserEntity user3 = UserEntity.builder()
                .email("dangundangun@naver.com")
                .name("dangun")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        userRepository.save(user3);
    }
}
