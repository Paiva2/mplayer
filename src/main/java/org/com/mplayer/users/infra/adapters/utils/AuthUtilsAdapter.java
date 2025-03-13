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
    public void verify(String token) {
        try {
            String privateKeyContent = readKeyWithPath(keyPath);
            RSAKey privRsaJWK = RSAKey.parseFromPEMEncodedObjects(privateKeyContent).toRSAKey();
            RSAKey rsaPublicJWK = privRsaJWK.toPublicJWK();

            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);

            if (!signedJWT.verify(verifier)) {
                //throw error
            }

            //new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
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
