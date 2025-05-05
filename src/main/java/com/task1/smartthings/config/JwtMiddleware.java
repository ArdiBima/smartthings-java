package com.task1.smartthings.config;
import com.task1.smartthings.util.JwtUtilRSA;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.registry.Registry;
/**
 * JwtMiddleware is responsible for validating JWT tokens in incoming requests.
 * If a token is valid, the request is allowed to proceed; otherwise, an error response is returned.
 */
public class JwtMiddleware implements Handler {
    private JwtUtilRSA jwtUtilRSA;
    public JwtMiddleware(JwtUtilRSA jwtUtilRSA) {
        this.jwtUtilRSA=jwtUtilRSA;
    }
    @Override
    public void handle(Context ctx) {
        String authorizationHeader = ctx.getRequest().getHeaders().get("Authorization");
        
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized: No token provided");
        }

        String token = authorizationHeader.substring(7);
        System.out.println("Token: " + token);
        try {
            Integer userId=jwtUtilRSA.verifyToken(token);
            if (userId!=null) {
                ctx.next(Registry.single(Integer.class, userId));
            } else {
                throw new IllegalArgumentException("Unauthorized: No token provided no userId");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unauthorized: No token provided");
        }
    }
}


