package me.yeonnex.userservice.jpa;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users", schema = "citytimer")
public class UserEntity {
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

}
