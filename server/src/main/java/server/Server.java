package server;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.GameRequest;
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

import java.sql.DatabaseMetaData;
import java.util.HashMap;

import java.util.Map;
import java.util.Objects;


public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson gson = new Gson();
    private final WebSocketHandler webSocketHandler;

    public Server() {
        try {
            this.authService = new AuthService();
            this.gameService = new GameService();
            this.userService = new UserService();
            webSocketHandler = new WebSocketHandler();
        } catch (DataAccessException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        var s = new Server();
        var port = s.run(0); //Need to keep 0
        System.out.println("Running on port: " + port);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.webSocket("/ws", webSocketHandler);

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

    private Object errorHandler(Exception e, Response res) {

        // Create a map to hold the error message
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());

        if (Objects.equals(e.getMessage(), "Error: bad request")) {
            res.status(400);
        } else if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
            res.status(401);
        } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
            res.status(403);
        } else {
            res.status(500);
        }

        // Convert the map to JSON and return
        return new Gson().toJson(errorMap);

    }

    private String verifyAuth(Request req, Response res) throws DataAccessException {
        try {
            String authToken = gson.fromJson(req.headers("Authorization"), String.class);

            return authService.verifyAuth(authToken);
        } catch (Exception e) {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    private Object clearDataBase(Request req, Response res) {
        try {
            //Call the delete functions
            userService.deleteAllUserData();
            gameService.deleteAllGame();
            authService.deleteAllAuth();
            return "";
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    private Object listGames(Request req, Response res) {
        //Call the get games function
        try {
            String userName = verifyAuth(req, res);

            return new Gson().toJson(gameService.getGames());
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            String userName = verifyAuth(req, res);

            GameRequest data = new Gson().fromJson(req.body(), GameRequest.class);

            // Access the fields using getter methods
            String playerColor = data.getPlayerColor();
            int gameID = data.getGameID();

            //Let the server handle it all
            gameService.joinGame(userName, playerColor, gameID);

            return "";
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    private Object createGame(Request req, Response res) {
        try {
            String userName = verifyAuth(req, res);

            GameData data = gson.fromJson(req.body(), GameData.class);

            //Call the create game function
            GameData game = gameService.createGame(data.gameName());

            //Return just the id
            Map<String, Integer> gameMap = new HashMap<>();
            gameMap.put("gameID", game.gameID());

            return new Gson().toJson(gameMap);
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    private Object login(Request req, Response res) throws DataAccessException {
        try {
            //Verify password
            UserData newUser = gson.fromJson(req.body(), UserData.class);

            boolean isCorrect = userService.verifyPassword(newUser);

            if (!isCorrect) {
                throw new DataAccessException("Wrong Password");
            } //Check the error here

            //Get the auth
            AuthData newAuth = authService.getAuth(newUser.username());

            //Return here
            return new Gson().toJson(newAuth);
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        try {
            String userName = verifyAuth(req, res);

            //delete the auth
            authService.logout(userName);

            //Return here
            return "";
        } catch (Exception e) {
            return errorHandler(e, res);
        }

    }


    private Object register(Request req, Response res) throws DataAccessException {
        try {
            UserData newUser = gson.fromJson(req.body(), UserData.class);
            //Add the user
            userService.register(newUser);
            //returns the same things as the login
            return login(req, res);
        } catch (Exception e) {
            return errorHandler(e, res);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

