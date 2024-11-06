package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
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
        serverFacade.register(userData);

        //We have no games yet
        Object games = serverFacade.listGames();
        Assertions.assertNotNull(games);
//        Assertions.assertEquals(serverFacade.listGames().getGames().get("games").size(), 0, "Games already exist.");
//
//        //Add game
//        gameService.createGame("myGame");
//
//        //Check game size
//        Collection<GameData> games = gameService.getGames().get("games");
//        Assertions.assertEquals(1, games.size(), "Incorrect number of games.");
//
//        //Check if info is right
//        for (GameData gameData : games) {
//            Assertions.assertEquals(gameData.gameName(), "myGame", "Incorrect game name.");
//        }
    }

}
