package org.birdhelpline.app.crypto.bcrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

public class BCryptPasswordEncoderMain {

    private static final Logger logger = LoggerFactory
            .getLogger(BCryptPasswordEncoderMain.class);

    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * Encode a single password if supplied as args[0]
     * Otherwise, encode the standard passwords:
     * <pre>"user1", "admin1", "user2"</pre>
     *
     * @param args single password to encode
     */
    public static void main(String[] args) {
        String[] passwords = {"test"};

        if (args.length == 1) {
            logger.info(encode(args[0]));
        } else {
            logger.info("Encoding passwords: {}", Arrays.toString(passwords));
            for (String psswd : passwords) {
                logger.info("[{}]", encode(psswd));
            }
        }
    }

} // The End...
