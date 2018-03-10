package com.team.jcti.ttr.communication;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.drawdestinationcard.IDrawDestinationCardPresenter;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.gamelist.GameListPresenter;
import com.team.jcti.ttr.gamelobby.GameLobbyPresenter;
import com.team.jcti.ttr.login.LoginPresenter;
import com.team.jcti.ttr.message.IMessagePresenter;
import com.team.jcti.ttr.message.MessagePresenter;
import com.team.jcti.ttr.models.ClientGameModel;
import com.team.jcti.ttr.models.ClientModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import interfaces.IClient;
import model.DestinationCard;
import model.Game;
import model.GameHistory;
import model.Player;
import model.TrainCard;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientFacade implements IClient {
    private ClientModel mClientModel = ClientModel.getInstance();
    private ClientGameModel mGameModel = ClientGameModel.getInstance();

    @Override
    public void onLogin(String authToken, String username) {
        mClientModel.setAuthToken(authToken);
        mClientModel.setUsername(username);
        LoginPresenter presenter = (LoginPresenter) mClientModel.getActivePresenter();
        presenter.onLogin();
    }

    @Override
    public void onRegister(String authToken, String username) {
        mClientModel.setAuthToken(authToken);
        mClientModel.setUsername(username);
        LoginPresenter presenter = (LoginPresenter) mClientModel.getActivePresenter();
        presenter.onRegister();
    }

    @Override
    public void displayError(String message) {
       IPresenter activePresenter = mClientModel.getActivePresenter();
       activePresenter.displayError(message);
    }

    @Override
    public void claimedRoute(Integer player, String routeID) {
        IGamePresenter activePresenter = mGameModel.getActivePresenter();
        Player p = mGameModel.getPlayers().get(player);
        p.addRoute(routeID);
        String user = p.getUser();
        String message = String.format("***%s claimed a route %s***", user, routeID);
        GameHistory gameHistory = new GameHistory(user, message);
        mGameModel.addGameHistoryObj(gameHistory);
        if(activePresenter.getClass() == GamePresenter.class) {
            GamePresenter gamePresenter = (GamePresenter) activePresenter;
            gamePresenter.onClaimRoute(player, routeID);

        }
        mGameModel.moveTurnPosition();
        activePresenter.update();
    }

    @Override
    public void promptRenewSession() {

    }

    @Override
    public void onCreateGame(Game game) {
        mClientModel.setGame(game);
        GameLobbyPresenter presenter = (GameLobbyPresenter) mClientModel.getActivePresenter();
        presenter.onCreateGame();
    }

    @Override
    public void onJoinGame(Game game) {
        mClientModel.setGame(game);
        GameListPresenter presenter = (GameListPresenter) mClientModel.getActivePresenter();
        presenter.onJoinGame();
    }

    @Override
    public void onLeaveGame() {
        mClientModel.setGame(null);
        GameLobbyPresenter presenter = (GameLobbyPresenter) mClientModel.getActivePresenter();
        presenter.onLeaveGame();
    }

    @Override
    public void onGameStarted() {
        GameLobbyPresenter presenter = (GameLobbyPresenter) mClientModel.getActivePresenter();
        presenter.onGameStarted();
    }


    @Override
    public void onGetServerGameList(Game[] games) {
        List<Game> gamesList = new ArrayList<>();
        for (Game game : games) {
            gamesList.add(game);
        }
        mClientModel.setWaitingGames(gamesList);
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void addGametoList(Game game) {
        List<Game> gameList = mClientModel.getWaitingGames();
        gameList.add(game);
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void removeGameFromList(String gameID) {
        for(int i = 0; i < mClientModel.getWaitingGames().size(); i++) {
            if (gameID.equals(mClientModel.getWaitingGames().get(i).getID())) {
                mClientModel.getWaitingGames().remove(i);
                return;
            }
        }
        mClientModel.getActivePresenter().update();
    }

    @Override
    public void updateGame(Game game) {
        IPresenter presenter = mClientModel.getActivePresenter();
       presenter.updateGame(game);
    }

    @Override
    public void receiveMessage(GameHistory gameHistory) {
        gameHistory.setChat(true);
        MessagePresenter presenter = (MessagePresenter) mGameModel.getActivePresenter();
        presenter.updateGameHistory(gameHistory);
    }
    @Override
    public void drawTrainCards(Integer player, Integer numCards, TrainCard[] cards) {
        IGamePresenter presenter = mGameModel.getActivePresenter();
        Player p = mGameModel.getPlayers().get(player);
        p.addTrainCards(cards);
        String user = p.getUser();
        String message = String.format("***%s drew %d Train cards***", user, numCards);
        GameHistory drewCards = new GameHistory(user, message);
        mGameModel.addGameHistoryObj(drewCards);
        mGameModel.moveTurnPosition();
        presenter.update();

    }

    @Override
    public void discardTrainCards(Integer player, Integer numCards, int[] pos) {

    }

    @Override
    public void drawDestCards(Integer player, Integer numCards, DestinationCard[] cards) {
        mGameModel.drawDestCards(player, numCards, cards);
    }

    @Override
    public void discardDestCards(Integer player, Integer numCards, int[] pos) {
        IGamePresenter presenter = mGameModel.getActivePresenter();
        Player p = mGameModel.getPlayers().get(player);
        if(p.isFirstDestPick()){
            p.setFirstDestPick(); //to false
        }
        //mGameModel.moveTurnPosition();
    }

    @Override
    public void swapFaceUpCards(int[] pos, TrainCard[] cards) {

        GamePresenter presenter = (GamePresenter) mClientModel.getActivePresenter();
        presenter.updateFaceUpCards(pos, cards);
    }
}
