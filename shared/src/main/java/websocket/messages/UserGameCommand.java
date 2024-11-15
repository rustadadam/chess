package websocket.messages;

import java.util.Objects;

public class UserGameCommand {
    private final UserGameCommand.CommandType CommandType;

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private String authToken;

    public UserGameCommand(UserGameCommand.CommandType type, String message) {

        this.CommandType = type;
        //this.message = message;
    }

    public UserGameCommand.CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return UserGameCommand.CommandType() == that.getCommandType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType());
    }
}
}