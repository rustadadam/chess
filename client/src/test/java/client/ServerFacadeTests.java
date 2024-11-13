package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import ui.LoginClient;
import ui.ServerFacade;

import java.util.Collection;


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
        serverFacade.clearDataBase();
        UserData userData = new UserData("Adam", "Austria", "Email");
        AuthData auth = serverFacade.register(userData);
        Assertions.assertNotNull(auth);
    }

    @Test
    //Tests to ensure the server facade works
    public void registerFail() throws Exception {
        serverFacade.clearDataBase();
        UserData userData = new UserData("Adam", "Austria", "Email");
        AuthData auth = serverFacade.register(userData);

        try {
            AuthData auth2 = serverFacade.register(userData);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(auth);
        }

    }

    @Test
    @DisplayName("Successfully list all games")
    public void successListGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("Adam", "Austria", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        Assertions.assertEquals("Battles available to join: \n[38;5;12m", client.list());
    }

    @Test
    @DisplayName("Fail to list all games")
    public void failListGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("Adam", "kansa", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        client.create("Game");
        Assertions.assertNotEquals("Battles available to join: \n[38;5;12m", client.list("DFWOGSP"));
    }

    @Test
    @DisplayName("Success to create game")
    public void successCreateGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("Adam", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        client.create("Howny");
        client.create("HUMAN");
        Assertions.assertTrue(client.list().length() > 20);
    }

    @Test
    @DisplayName("Fail to create game")
    public void failCreateGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("legit", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        try {
            client.create();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }

}
