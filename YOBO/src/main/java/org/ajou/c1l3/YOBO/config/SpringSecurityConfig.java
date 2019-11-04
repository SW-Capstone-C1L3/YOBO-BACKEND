//package org.ajou.c1l3.YOBO.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {
//
//    //    private UserDetailsService userDetailsService;
////    private PasswordEncoder passwordEncoder;
//    private AuthenticationProvider authenticationProvider;
//
//    public SpringSecurityConfig(/*UserDetailsService userDetailsService,
//                                PasswordEncoder passwordEncoder,*/
//            AuthenticationProvider authenticationProvider) {
////        this.userDetailsService = userDetailsService;
////        this.passwordEncoder = passwordEncoder;
//        this.authenticationProvider = authenticationProvider;
//    }
//
//    /*
//     * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체.
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        /*
//         * AuthenticationProvider 구현체
//         */
//        auth.authenticationProvider(authenticationProvider);
////        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//    }
//
//    /*
//     * 스프링 시큐리티 룰을 무시하게 하는 Url 규칙.
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/resources/**")
//                .antMatchers("/css/**")
//                .antMatchers("/vendor/**")
//                .antMatchers("/js/**")
//                .antMatchers("/favicon*/**")
//                .antMatchers("/img/**")
//        ;
//    }
//
//    /*
//     * 스프링 시큐리티 룰.
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/login*/**").permitAll()
//                .antMatchers("/logout/**").permitAll()
//                .antMatchers("/chatbot/**").permitAll()
//                .anyRequest().authenticated()
//                .and().logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(logoutSuccessHandler())
//                .and().csrf()
//                .disable()
//                .addFilter(jwtAuthenticationFilter())
//                .addFilter(jwtAuthorizationFilter())
//                .exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler())
//                .authenticationEntryPoint(authenticationEntryPoint())
////        .and().sessionManagement()
////              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        ;
//    }
//
//    /*
//     * SuccessHandler bean register
//     */
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
//        successHandler.setDefaultTargetUrl("/index");
//        return successHandler;
//    }
//
//    /*
//     * FailureHandler bean register
//     */
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
//        failureHandler.setDefaultFailureUrl("/loginPage?error=error");
//        return failureHandler;
//    }
//
//    /*
//     * LogoutSuccessHandler bean register
//     */
//    @Bean
//    public LogoutSuccessHandler logoutSuccessHandler() {
//        CustomLogoutSuccessHandler logoutSuccessHandler = new CustomLogoutSuccessHandler();
//        logoutSuccessHandler.setDefaultTargetUrl("/loginPage?logout=logout");
//        return logoutSuccessHandler;
//    }
//
//    /*
//     * AccessDeniedHandler bean register
//     */
//    @Bean
//    public AccessDeniedHandler accessDeniedHandler() {
//        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
//        accessDeniedHandler.setErrorPage("/error/403");
//        return accessDeniedHandler;
//    }
//
//    /*
//     * AuthenticationEntryPoint bean register
//     */
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return new CustomAuthenticationEntryPoint("/loginPage?error=e");
//    }
//
//    /*
//     * Form Login시 걸리는 Filter bean register
//     */
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
//        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
//        jwtAuthenticationFilter.setUsernameParameter("username");
//        jwtAuthenticationFilter.setPasswordParameter("password");
//
//        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
//        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
//
//        jwtAuthenticationFilter.afterPropertiesSet();
//
//        return jwtAuthenticationFilter;
//    }
//
//    /*
//     * Filter bean register
//     */
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
//        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
//        return jwtAuthorizationFilter;
//    }
//}
//
