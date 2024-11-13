package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import ui.LoginClient;
import ui.PreLoginClient;
import ui.ServerFacade;
import ui.State;

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
            serverFacade.register(userData);
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

    @Test
    @DisplayName("Success to observe game")
    public void successObserveGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("slypuppy", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        client.create("new");
        String str = client.observe("1");
        if (str.contains("Error")) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Failure to observe game")
    public void failObserveGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("slypuppy", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());

        try {
            client.observe("1");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Success to give help")
    public void successHelpGamesTest() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("Man", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient client = new LoginClient(serverUrl, authData.authToken());
        String str = client.help();
        Assertions.assertNotNull(str);
    }

    @Test
    @DisplayName("Success to change clients")
    public void changeClientsTestSuccess() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        UserData userData = new UserData("slypuppy", "Mongi", "Email");
        serverFacade.register(userData);

        PreLoginClient client = new PreLoginClient(serverUrl);
        client.login("slypuppy", "Mongi");

        Assertions.assertEquals(client.getState(), State.SIGNEDIN);
    }

    @Test
    @DisplayName("Failure to change clients")
    public void changeClientsTestFailure() throws Exception {
        serverFacade.clearDataBase();

        PreLoginClient client = new PreLoginClient(serverUrl);
        try {
            client.login("slypuppy", "Dave");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }

    }

    @Test
    @DisplayName("Register clients")
    public void registerClientsTestSuccess() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        PreLoginClient client = new PreLoginClient(serverUrl);
        client.register("slypuppy", "Dave", "email");
        client.login("slypuppy", "Dave");
        Assertions.assertEquals(client.getState(), State.SIGNEDIN);
    }

    @Test
    @DisplayName("Fail to Register clients")
    public void registerClientsTestFail() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        PreLoginClient client = new PreLoginClient(serverUrl);

        try {
            client.register("myDawg", "Dave");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }

    }

    @Test
    @DisplayName("Overall Client Tests")
    public void overallClientsTestSucceed() throws Exception {
        serverFacade.clearDataBase();

        //Register user
        PreLoginClient client = new PreLoginClient(serverUrl);
        //Register user
        UserData userData = new UserData("Man", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient loginClient = new LoginClient(serverUrl, authData.authToken());

        client.register("Adam", "hi", "me");
        Assertions.assertEquals(client.getState(), State.SIGNEDIN);

        loginClient.create("NewGame");
        loginClient.join("1", "WHITE");
        Assertions.assertEquals(loginClient.getState(), State.INGAME);
    }

    @Test
    @DisplayName("Overall Client Tests Fail")
    public void overallClientsTestFail() throws Exception {
        serverFacade.clearDataBase();

        UserData userData = new UserData("Man", "Mongi", "Email");
        AuthData authData = serverFacade.register(userData);

        LoginClient loginClient = new LoginClient(serverUrl, authData.authToken());

        loginClient.create("NewGame2");
        loginClient.join("1", "WHITE");
        Assertions.assertEquals(loginClient.getState(), State.INGAME);

        try {
            loginClient.join("2", "WHITE");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Join Games Fail")
    public void joinGamesFail() throws Exception {
        serverFacade.clearDataBase();

        UserData userData = new UserData("Oldman", "Mongi", "hotshot@gmail.com");
        AuthData authData = serverFacade.register(userData);

        LoginClient loginClient = new LoginClient(serverUrl, authData.authToken());

        try {
            loginClient.join("1", "BLACK");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Join Games Success")
    public void joinGamesSuccess() throws Exception {
        serverFacade.clearDataBase();

        UserData userData = new UserData("Oldman", "Mongi", "hotshot@gmail.com");
        AuthData authData = serverFacade.register(userData);

        LoginClient loginClient = new LoginClient(serverUrl, authData.authToken());

        loginClient.create("firstGame");
        loginClient.join("1", "BLACK");
        Assertions.assertEquals(loginClient.getState(), State.INGAME);

        loginClient.create("SecondGame");
        loginClient.join("2", "BLACK");
        Assertions.assertEquals(loginClient.getState(), State.INGAME);
    }

}
