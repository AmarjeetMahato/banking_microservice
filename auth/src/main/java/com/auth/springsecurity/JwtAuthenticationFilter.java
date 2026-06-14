package com.auth.springsecurity;

import com.auth.domain.Users.repository.UserRepository;
import com.auth.utils.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.auth.utils.AppConstants.AUTH_PUBLIC_URLS;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

     private  final  JwtService jwtService;
     private  final UserRepository userRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        logger.info("Authorization header : {}");

        if(header != null && header.startsWith("Bearer ")){

            String token =  header.substring(7);
            try{
                if (!jwtService.isAccessToken(token)) {
                    //message pass kar hai---
                    filterChain.doFilter(request, response);
                    return;
                }

                Jws<Claims> parse = jwtService.parse(token);
                Claims payload = parse.getPayload();

                String userId = payload.getSubject();

                userRepository.findById(userId).ifPresent(user -> {

                    //check for user enable or not
                    if(user.isActive()){
                        // user mil chuka hai database se
                        List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() :
                                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(
                                        role.getRoleName()
                                        )
                                ).collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        if (SecurityContextHolder.getContext().getAuthentication() == null)
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                });

            }catch (ExpiredJwtException e) {
                request.setAttribute("error", "Token Expired");
                // e.printStackTrace();

            } catch (Exception e) {
                request.setAttribute("error", "Invalid Token");
//                e.printStackTrace();

            }
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return Arrays.stream(AppConstants.AUTH_PUBLIC_URLS)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

}
