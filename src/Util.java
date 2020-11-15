

import javax.crypto.*;
import java.io.*;
import java.security.*;

public class Util {

    static Key[] generateKeys(){
        KeyPairGenerator rsa = null;
        try {
            rsa = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        rsa.initialize(2048);
        KeyPair pair = rsa.generateKeyPair();
        Key[] keys = new Key[2];
        keys[0] = pair.getPrivate();
        keys[1] = pair.getPublic();
        return keys; //génére les clées privée et publique
    }

    static Key createSymetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
            generator.init(256);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return generator.generateKey(); //genere la clé symetrique
    }

    static void sendObject(OutputStream out, Object obj){ //envoie un objet à travers l'out
        try {
            ObjectOutputStream outObject = new ObjectOutputStream(out);
            outObject.writeObject(obj);
            outObject.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    static Object receiveObject(InputStream in) throws IOException, ClassNotFoundException { //recoit un objet à travers l'in
        ObjectInputStream inObject = new ObjectInputStream(in);
        return inObject.readObject();
    }

    static byte[] cryptObject(Object obj, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        String algo = key.getAlgorithm()=="RSA" ? "RSA" : "AES";
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outObjectBos = new ObjectOutputStream(bos);
        outObjectBos.writeObject(obj);
        return cipher.doFinal(bos.toByteArray()); //crypte un object avec la clé retourne un byte[] crypté
    }

    static Object decryptObject(byte[] bytes, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException {
        String algo = key.getAlgorithm()=="RSA" ? "RSA" : "AES";
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptBytes = cipher.doFinal(bytes);

        ByteArrayInputStream bis = new ByteArrayInputStream(decryptBytes);
        ObjectInputStream inObjectBis = new ObjectInputStream(bis);
        return inObjectBis.readObject(); //decypte un byte[] et le transforme en Object
    }


}
