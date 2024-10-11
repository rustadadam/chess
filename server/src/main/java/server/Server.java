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

        //Register Endpoint ? Where do I put the body stuff?
//        Spark.post("/user", (req, res) ->
//            {  {String username = req.queryParams("username");
//                String password = req.queryParams("password");
//                String email = req.queryParams("email");
//                return (username, password, email)}
//            );

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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

