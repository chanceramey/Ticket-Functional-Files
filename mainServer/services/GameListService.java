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

    public Command getGamesList() {
        Map<String, Game> gameMap = ServerModel.getInstance().getWaitingGames();
        Game[] gameArray = new Game[gameMap.size()];
        int index = 0;
        for (Game g : gameMap.values()) {
            gameArray[index] = g;
            index++;
        }
        clientProxy.onGetServerGameList(gameArray);
        return clientProxy.getCommand();
    }
}
