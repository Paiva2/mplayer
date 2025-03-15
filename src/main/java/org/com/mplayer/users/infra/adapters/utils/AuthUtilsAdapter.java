package org.com.mplayer.users.infra.adapters.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.com.mplayer.users.domain.ports.out.utils.AuthUtilsPort;
import org.com.mplayer.users.infra.exception.InvalidAuthorizationTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

@Component
public class AuthUtilsAdapter implements AuthUtilsPort {
    @Value("${auth.jwt.private-rsa}")
    private String keyPath;

    @Value("${auth.jwt.public-rsa}")
    private String publicKey;

    private final static Integer EXP_TIME_SECONDS = 25200; // 7h in seconds
    private final static String TOKEN_ISSUER = "mPlayer-app";

    @Override
    public String generate(String subject) {
        try {
            Date now = new Date();

            String privateKeyContent = readKeyWithPath(keyPath);

            RSAKey privRsaJWK = RSAKey.parseFromPEMEncodedObjects(privateKeyContent).toRSAKey();

            JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .issuer(TOKEN_ISSUER)
                .subject(subject)
                .expirationTime(expTimeDate())
                .notBeforeTime(now)
                .issueTime(now)
                .claim("external-id", subject)
                .build();

            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(privRsaJWK.getKeyID()).build(),
                jwtClaims
            );

            JWSSigner signer = new RSASSASigner(privRsaJWK);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String verify(String token, String claim) {
        try {
            String privateKeyContent = readKeyWithPath(keyPath);
            RSAKey privRsaJWK = RSAKey.parseFromPEMEncodedObjects(privateKeyContent).toRSAKey();
            RSAKey rsaPublicJWK = privRsaJWK.toPublicJWK();

            JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);

            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(verifier)) {
                throw new InvalidAuthorizationTokenException("Invalid token!");
            }

            boolean isTokenExpired = new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime());

            if (isTokenExpired) {
                throw new InvalidAuthorizationTokenException("Invalid token. Token is expired!");
            }

            return signedJWT.getJWTClaimsSet().getClaimAsString(claim);
        } catch (InvalidAuthorizationTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readKeyWithPath(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private Date expTimeDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, EXP_TIME_SECONDS);

        return calendar.getTime();
    }
}
