package me.yeonnex.userservice.jpa;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "citytimer")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String encryptedPwd;

    @Column
    private String roles; // USER,ADMIN <- 이런식으로 들어가고 나중에 컴마 기준으로 자를 것임

    @Column
    private Timestamp createdAt;

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

}
