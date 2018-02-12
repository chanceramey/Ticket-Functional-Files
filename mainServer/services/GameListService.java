package services;

import java.util.Map;

import command.Command;
import communication.ClientProxy;
import model.Game;
import model.ServerModel;

/**
 * Created by Jeff on 2/9/2018.
 */

public class GameListService {
    ClientProxy clientProxy = new ClientProxy();
    ServerModel mServerModel = ServerModel.getInstance();
    public Command getGamesList() {
        Map<String, Game> gameMap = mServerModel.getWaitingGames();
        Game[] gameArray = new Game[gameMap.size()];
        int index = 0;
        for (Game g : gameMap.values()) {
            gameArray[index] = g;
            index++;
        }
        clientProxy.onGetServerGameList(gameArray);
        return clientProxy.getCommand();
    }

    public Command joinGame(String userId, String gameId) {
        try {
            Game game = mServerModel.getGame(gameId);
            game.joinGame(userId);
            clientProxy.onJoinGame(game);
            return clientProxy.getCommand();
        } catch (ServerModel.GameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
