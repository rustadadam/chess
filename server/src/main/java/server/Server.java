package server;

import spark.*;
=

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Clear endpoint
        Spark.delete("/db", this::clear_db);

        //Register Endpoint ? Where do I put the body stuff?
        Spark.post("/user", (req, res) ->
            {  {String username = req.queryParams("username");
                String password = req.queryParams("password");
                String email = req.queryParams("email");
                return (username, password, email)}
            );

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear_db(Request req, Response res) throws ResponseException {
        var id = Integer.parseInt(req.params(":id"));
        var pet = service.getPet(id);
        if (pet != null) {
            service.deletePet(id);
            webSocketHandler.makeNoise(pet.name(), pet.sound());
            res.status(204);
        } else {
            res.status(404);
        }
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
