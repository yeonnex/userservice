## 프로젝트 개요
회원가입, 로그인, 권한부여 기능의 스프링부트 Rest API 서버이다.
클라이언트와 서버는 json 데이터를 주고 받는다.

## APIs
### [회원가입] POST /users - 
```java

```
### [전체사용자조회] GET /users
```java

```
### [상세정보확인] GET /users/{user_id}
```java

```

## Spring Security
### Authentication + Authorization
1. 애플리케이션에 spring security 의존성 추가
2. **WebSecurityConfigurerAdapter**를 상속받는 Security Configuration 클래스 생성
3. Security Configuration 클래스에 **@EnableWebSecurity** 추가
4. Authentication ▶ **configure(AuthenticationManagerBuilder auth)** 메서드를 재정의
5. 비밀번호 encode 를 위한 **BCryptPasswordEncoder** 빈 정의
   - 비밀번호를 해싱하기 위해 Bcrypt 알고리즘 사용
   - 랜덤 Salt 를 부여하여 여러번 Hash 를 적용한 암호화 방식
6. Authorization ▶ **configure(HttpSecurity http)** 메서드 재정의