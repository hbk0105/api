package com.rest.api.config;

import com.rest.api.exception.RestAccessDeniedExceptionHandler;
import com.rest.api.exception.RestAuthenticationExceptionHandler;
import com.rest.api.filter.JwtRequestFilter;
import com.rest.api.handler.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // 커스텀 예외처리
    @Bean
    RestAccessDeniedExceptionHandler accessDeniedHandler() {
        return new RestAccessDeniedExceptionHandler();
    }

    // 커스텀 예외처리
    @Bean
    RestAuthenticationExceptionHandler authenticationExceptionHandler() {
        return new RestAuthenticationExceptionHandler();
    }

    @Autowired
    public CustomLogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        // web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**","/air-datepicker/**" ,"/assets/**" ,"/sweetalert/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //https://stackoverflow.com/questions/22524470/spring-security-3-2-csrf-disable-for-specific-urls
        // Build the request matcher for CSFR protection
        RequestMatcher csrfRequestMatcher = new RequestMatcher() {

            // Disable CSFR protection on the following urls:
            private AntPathRequestMatcher[] requestMatchers = {
                    new AntPathRequestMatcher("/api/**"),
                    new AntPathRequestMatcher("/h2-console/**")
            };

            @Override
            public boolean matches(HttpServletRequest request) {
                // If the request match one url the CSFR protection will be disabled
                for (AntPathRequestMatcher rm : requestMatchers) {
                    if (rm.matches(request)) { return false; }
                }
                return true;
            } // method matches

        }; // new RequestMatcher

        // h2 console - https://www.slipp.net/questions/546
        // cors : https://whydda.tistory.com/entry/HTTP-%EC%A0%91%EA%B7%BC%EC%A0%9C%EC%96%B4-CORS%EB%9E%80
        // csrf : https://changrea.io/spring/spring-security-session-csrf/
        // https://gigas-blog.tistory.com/124
        // https://stackoverflow.com/questions/32896966/how-do-i-set-x-frame-options-response-header-to-allow-from-values-using-spring
        http.cors().and().csrf()
                //.disable()
                // https://cheese10yun.github.io/spring-csrf/
                .requireCsrfProtectionMatcher(csrfRequestMatcher)
                // cookieCsrfTokenRepository CSRF-TOKEN는 쿠키로주어 X-CSRF-TOKEN헤더 이름으로 체크!
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.csrfTokenRepository(csrfTokenRepository())
                .and().headers().frameOptions().sameOrigin().and()
                .cors().configurationSource(corsConfigurationSource()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/users/**").hasAnyRole("USER","ADMIN")//.hasRole("USER")
                .antMatchers("/api/**", "/h2-console/**").permitAll()
                // https://ddakker.tistory.com/295
                .and().logout()
                /*.logoutUrl("/api/logout")
                .logoutSuccessUrl("/api/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler())*/
                //.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/api/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationExceptionHandler())
                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 허용 적용 - https://oddpoet.net/blog/2017/04/27/cors-with-spring-security/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        //configuration.addAllowedMethod(String.valueOf(Arrays.asList("HEAD","GET","POST","PUT","DELETE")));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}