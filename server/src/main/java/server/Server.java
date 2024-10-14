package server;


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


public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    //private final WebSocketHandler webSocketHandler;

    public Server() {
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();
        //webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Clear endpoint
        Spark.delete("/db", this::clear_db);


        //List Games  endpoint
        Spark.get("/game", this::listGames);

        //Join Game
        Spark.put("/game", this::joinGame);

        //Create Game endpoint
        Spark.post("/game", this::createGame);

        //Logout endpoint
        Spark.delete("/session", this::logout);

        //Register Endpoint ? Where do I put the body stuff?
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    private Object clear_db(Request req, Response res) throws DataAccessException {
        //Call the delete functions
        userService.deleteAllUserData();
        gameService.deleteAllGame();
        authService.deleteAllAuth();
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        //Call the get games function
        return gameService.getGames();
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        //Let the server handle it all
        GameData game = gameService.joinGame(req);

        return "";
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        //Call the create game function
        GameData game = gameService.createGame(req);

        //Return just the id
        return game.gameID();
    }

    private Object login(Request req, Response res) throws DataAccessException {
        //Verify password
        boolean is_correct = userService.verifyPassword(req);

        if (!is_correct) {
            throw new DataAccessException("Error");
        } //Check the error here

        //Get the auth
        AuthData newAuth = authService.getAuth(req);

        //Return here
        return newAuth;
    }

    private Object logout(Request req, Response res) throws DataAccessException {

        //delete the auth
        authService.logout(req);

        //Return here
        return "";
    }


    private Object register(Request req, Response res) throws DataAccessException {
        //Get the user
        UserData newUser = userService.register(req);

        //returns the same things as the login
        return login(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

