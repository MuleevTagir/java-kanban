import utils.Managers;
import utils.Server;

import java.io.IOException;

public class HttpTaskServer {

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server(Managers.getDefault());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        if (server != null) {
            server.start();
        }
    }
}