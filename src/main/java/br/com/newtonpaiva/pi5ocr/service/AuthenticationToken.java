package br.com.newtonpaiva.pi5ocr.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationToken {

    private final String jwtSecret = "MySecret";

    public String getBearer(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.substring(7, bearerToken.length());
        return token;
    }


    public Boolean authenticate(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new SignatureException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new Exception("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT claims string is empty.");
        }
    }
}
