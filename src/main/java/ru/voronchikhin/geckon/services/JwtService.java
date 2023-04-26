package ru.voronchikhin.geckon.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.voronchikhin.geckon.security.PersonDetails;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("26452948404D635166546A576E5A7234743777217A25432A462D4A614E645267556B58703273357638782F413F4428472B4B6250655368566D59713374367739"));
    private static final Key REFRESH_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("77217A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250655368566B5970337336763979244226452948404D635166546A576E5A72"));

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, SECRET_KEY);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, REFRESH_KEY);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    public String generateToken(PersonDetails details){
//        return generateToken(details);
//    }

    public String generateAccessToken(PersonDetails details){
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts
                .builder()
                .setSubject(details.getUsername())
                .setExpiration(accessExpiration)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .claim("roles", details.getAuthorities())
                .compact();
    }

    public String generateRefreshToken(PersonDetails details){
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts
                .builder()
                .setSubject(details.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(REFRESH_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateAccessToken(String token){
        return validateToken(token, SECRET_KEY);
    }

    public boolean validateRefreshToken(String token){
        return validateToken(token, REFRESH_KEY);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);

            return true;
        }catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

//    public Boolean isTokenValid(String token, PersonDetails personDetails){
//        final String username = extractUsername(token);
//
//        return (username.equals(personDetails.getUsername())) && !isTokenExpired(token);
//    }

//    private Key getSignInKey(){
//        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyByte);
//    }
//
//    private Key getRefreshKey(){
//        byte[] keyByte = Decoders.BASE64.decode(REFRESH_KEY);
//        return Keys.hmacShaKeyFor(keyByte);
//    }
}
