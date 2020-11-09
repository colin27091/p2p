import javax.crypto.KeyGenerator;
import java.security.*;
import java.util.HashMap;

public class Util {

    static HashMap<PrivateKey, PublicKey> generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(1024);
        KeyPair pair = rsa.generateKeyPair();
        HashMap hm = new HashMap();
        hm.put(pair.getPrivate(), pair.getPublic());
        return hm;
    }

    static Key createSymetricKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        return generator.generateKey();
    }
}
