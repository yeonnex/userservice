package me.yeonnex.userservice;

import me.yeonnex.userservice.jpa.UserEntity;
import me.yeonnex.userservice.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
                .email("syhoneyjam@naver.com")
                .name("seoyeon")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .build();
        userRepository.save(user1);

        UserEntity user2 = UserEntity.builder()
                .email("yeonnex@gmail.com")
                .name("yeonseo")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .build();
        userRepository.save(user2);

        UserEntity user3 = UserEntity.builder()
                .email("water@naver.com")
                .name("firefire")
                .encryptedPwd(bCryptPasswordEncoder.encode("1234"))
                .userId(UUID.randomUUID().toString())
                .build();
        userRepository.save(user3);
    }
}
