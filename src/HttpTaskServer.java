import utils.Managers;
import utils.Server;

public class HttpTaskServer {

    public static void main(String[] args) {
        Server server = new Server(Managers.getDefault());
        server.start();
    }
}