## Spring Security
### Authentication + Authorization
1. 애플리케이션에 spring security 의존성 추가
2. **WebSecurityConfigurerAdapter**를 상속받는 Security Configuration 클래스 생성
3. Security Configuration 클래스에 **@EnableWebSecurity** 추가
4. Authentication ▶ **configure(AuthenticationManagerBuilder auth)** 메서드를 재정의
5. 비밀번호 encode 를 위한 **BCryptPasswordEncoder** 빈 정의
6. Authorization ▶ **configure(HttpSecurity http)** 메서드 재정의