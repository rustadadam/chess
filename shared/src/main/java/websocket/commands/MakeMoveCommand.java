package websocket.commands;

import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand {

    public final ChessPosition startPos;
    public final ChessPosition endPos;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID,
                           ChessPosition startPos, ChessPosition endPos) {
        super(commandType, authToken, gameID);

        this.startPos = startPos;
        this.endPos = endPos;
    }
}
