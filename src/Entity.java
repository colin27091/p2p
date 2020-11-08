import java.io.IOException;
import java.security.*;

public interface Entity {

    void getGraphical();

    void putListener();

    void putIO() throws IOException;

    void generateKeys() throws NoSuchAlgorithmException;

    void receivePublicKey() throws IOException, ClassNotFoundException;

    void sendPublicKey() throws IOException, ClassNotFoundException;

}
