import org.jetbrains.annotations.NotNull;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.util.HashMap;

public class Util {

    static Key[] generateKeys(){
        KeyPairGenerator rsa = null;
        try {
            rsa = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        rsa.initialize(1024);
        KeyPair pair = rsa.generateKeyPair();
        Key[] keys = new Key[2];
        keys[0] = pair.getPrivate();
        keys[1] = pair.getPublic();
        return keys;
    }

    static Key createSymetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return generator.generateKey();
    }

    static void sendPublicKey(OutputStream out, PublicKey pk){
        try {
            ObjectOutputStream outObject = new ObjectOutputStream(out);
            outObject.writeObject(pk);
            outObject.flush();
            outObject.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    static PublicKey receivePublicKey(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream inObject = new ObjectInputStream(in);
        return (PublicKey) inObject.readObject();
    }

    static byte[] cryptObject(Object obj, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        String algo = key.getAlgorithm()=="RSA" ? "RSA" : "AES";
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outObjectBos = new ObjectOutputStream(bos);
        outObjectBos.writeObject(obj);

        return cipher.doFinal(bos.toByteArray());
    }

    static Object decryptObject(byte[] bytes, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException {
        String algo = key.getAlgorithm()=="RSA" ? "RSA" : "AES";
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptBytes = cipher.doFinal(bytes);

        ByteArrayInputStream bis = new ByteArrayInputStream(decryptBytes);
        ObjectInputStream inObjectBis = new ObjectInputStream(bis);
        return inObjectBis.readObject();
    }

    static void send(OutputStream out, Object obj){

    }

}
