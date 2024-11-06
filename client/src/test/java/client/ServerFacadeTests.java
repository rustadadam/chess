package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static String serverUrl = "http://localhost:8080";
    private static ServerFacade serverFacade = new ServerFacade(serverUrl);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    //Tests to ensure the server facade works
    public void serverFacadeClear() throws Exception {
        serverFacade.clearDataBase();
        Assertions.assertTrue(true);
    }

    @Test
    //Tests to ensure the server facade works
    public void registerSuccess() throws Exception {
        UserData userData = new UserData("Adam", "Austria", "Email");
        serverFacade.register(userData);
        Assertions.assertTrue(true);
    }

}
