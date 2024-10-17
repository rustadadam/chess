package server;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import spark.Request;
import spark.Response;
import spark.Service;
import model.UserData; // I use these as the request objects
import model.AuthData;
import model.GameData;
import com.google.gson.Gson;

import java.util.Map;


public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson gson = new Gson();
    //private final WebSocketHandler webSocketHandler;

    public Server() {
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();
        //webSocketHandler = new WebSocketHandler();
    }

    public static void main(String[] args) {
        var s = new Server();
        var port = s.run(0);
        System.out.println("Running on port: " + port);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Clear endpoint
        Spark.delete("/db", this::clearDataBase);


        //List Games  endpoint
        Spark.get("/game", this::listGames);

        //Join Game
        Spark.put("/game", this::joinGame);

        //Create Game endpoint
        Spark.post("/game", this::createGame);

        //Login
        Spark.post("/session", this::login);

        //Logout endpoint
        Spark.delete("/session", this::logout);

        //Register Endpoint
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String verifyAuth(Request req, Response res) throws DataAccessException {
        //Returns the username associated with the auth
        //var headers = req.headers();
        String authToken = gson.fromJson(req.headers("Authorization"), String.class);

        return authService.verifyAuth(authToken);
    }

    private Object clearDataBase(Request req, Response res) throws DataAccessException {
        //Call the delete functions
        userService.deleteAllUserData();
        gameService.deleteAllGame();
        authService.deleteAllAuth();
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        //Call the get games function
        return new Gson().toJson(gameService.getGames());
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String userName = verifyAuth(req, res);
        String playerColor = gson.fromJson(req.params("playerColor"), String.class);
        int gameID = gson.fromJson(req.params("gameID"), Integer.class);

        //Let the server handle it all
        gameService.joinGame(userName, playerColor, gameID);

        return "";
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        String userName = verifyAuth(req, res);

        GameData data = gson.fromJson(req.body(), GameData.class);

        //Call the create game function
        GameData game = gameService.createGame(data.gameName());

        //Return just the id
        return new Gson().toJson(game.gameID());
    }

    private Object login(Request req, Response res) throws DataAccessException {
        //Verify password
        UserData newUser = gson.fromJson(req.body(), UserData.class);

        boolean is_correct = userService.verifyPassword(newUser);

        if (!is_correct) {
            throw new DataAccessException("Wrong Password");
        } //Check the error here

        //Get the auth
        AuthData newAuth = authService.getAuth(newUser.username());

        //Return here
        return new Gson().toJson(newAuth);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        String userName = verifyAuth(req, res);

        //delete the auth
        authService.logout(userName);

        //Return here
        return "";
    }


    private Object register(Request req, Response res) throws DataAccessException {

        UserData newUser = gson.fromJson(req.body(), UserData.class);

        //Add the user
        userService.register(newUser);

        //returns the same things as the login
        return login(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

