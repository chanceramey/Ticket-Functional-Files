package com.team.jcti.ttr.models;

import com.google.android.gms.games.Players;
import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.IPresenter;
import com.team.jcti.ttr.game.GamePresenter;
import com.team.jcti.ttr.message.MessagePresenter;
import com.team.jcti.ttr.state.NotTurnState;
import com.team.jcti.ttr.state.OneTrainPickedState;
import com.team.jcti.ttr.state.State;
import com.team.jcti.ttr.state.TurnState;
import com.team.jcti.ttr.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import model.Color;
import model.DestinationCard;
import model.GameHistory;

import model.Game;
import model.Player;
import model.StateType;
import model.TrainCard;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientGameModel extends Observable {

    private static ClientGameModel SINGLETON;
    public static ClientGameModel getInstance() {
        if(SINGLETON == null) {
            SINGLETON = new ClientGameModel();
        }
        return SINGLETON;
    }

    boolean active = false;

    private Board board = null;
    private List<Player> players;
    private String gameId;
    private int userPlayer;
    private int gameHistoryPosition;
    private List<GameHistory> gameHistoryArr = new ArrayList<>();
    private IGamePresenter activePresenter;
    private TrainCard[] faceUpCards;
    private Player currentPlayer;
    private int destDeckSize;
    private int trainDeckSize;

    // Longest path or destination card calculation related
    private Map<Player, List<Route>> mEachPlayersLongestPath = new HashMap<>();
    private Map<Player, List<String>> mEachPlayersCities = new HashMap<>();
    private Map<Player, List<DestinationCard>> mEachPlayersDestinationCards = new HashMap<>();
    private Map<Player, List<Route>> mAllClaimedRoutes = new HashMap<>();
    private Map<Player, Map<DestinationCard, Boolean>> mPlayersDestinationCardStatus = new HashMap<>();
    private Map<String, Route> mAllRoutes;
    private int mLengthOfLongestPath = 0;
    // until here

    public boolean isMyTurn() {
        return currentPlayer.isTurn();
    }

    public void setMyTurn(boolean myTurn) {
        currentPlayer.setTurn(myTurn);
    }
  
    private State state;

    public void startGame(Game game) {
        this.gameId = game.getID();
        List<String> playerStrings = game.getPlayers();
        this.players = new ArrayList<>();
        faceUpCards = new TrainCard[5];
        for (int i = 0; i < faceUpCards.length; i++){
            faceUpCards[i] = TrainCard.WILD;
        }
        Color[] colors = Color.values();
        for (int i = 0; i < playerStrings.size(); i++) {
            if (playerStrings.get(i).equals(ClientModel.getInstance().getUsername())) userPlayer = i;
            Player player = new Player(playerStrings.get(i), colors[i], i);
            players.add(player);
        }
        this.active = true;
        gameHistoryPosition = 0;
        currentPlayer = players.get(userPlayer);

        trainDeckSize = 240;
        destDeckSize = 30;
    }

    public IGamePresenter getActivePresenter() {
        return activePresenter;
    }

    public List<GameHistory> getGameHistory() {
        return gameHistoryArr;
    }

    public void addGameHistoryObj (GameHistory gameHistory) {
        gameHistoryArr.add(gameHistory);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isActive() {
        return active;
    }

    public int getGameHistoryPosition() {
        return gameHistoryPosition;
    }

    public String getGameID() {
        return gameId;
    }

    public Map<String, Route> getmAllRoutes() {
        return mAllRoutes;
    }

    public void increment(int numCommands) {
        gameHistoryPosition += numCommands;
    }

    public List<TrainCard> getPlayersTrainCards() {
        return players.get(userPlayer).getTrainCards();
    }

    public List<DestinationCard> getPlayersDestCards() {
        return players.get(userPlayer).getDestCards();
    }

    public boolean isFirstTurn(){
        return players.get(userPlayer).isFirstDestPick();
    }

    public Player getPlayerById(int id){
        for(Player player : players){
            if(player.getId() == id){

                return player;
            }
        }
        return null;
    }

    public TrainCard[] getFaceUpCards() {
        return faceUpCards;
    }

    public void drawDestCards(Integer playerIndex, Integer numCards, DestinationCard[] cards, Integer deckSize) {
        Player player = players.get(playerIndex);
        if (playerIndex == userPlayer) {
            player.addDestCards(cards);
            activePresenter.drawDestCards();
        }
        else {
            player.addDestCards(numCards);
        }

        String user = player.getUser();
        String message = String.format("***%s drew %d destination cards", user, numCards);
        gameHistoryArr.add(new GameHistory(user, message));

        destDeckSize = deckSize;
        activePresenter.update();
    }

    public void setActivePresenter(IGamePresenter gamePresenter) {
        this.activePresenter = gamePresenter;
    }

    public List<DestinationCard> getUsersDestCard() {
       List<DestinationCard> destCards = players.get(userPlayer).getDestCards();
        return destCards.subList(destCards.size() - 3, destCards.size());
    }

    public Player getUserPlayer() {
       return players.get(userPlayer);
    }

    public Map<Player, Map<DestinationCard, Boolean>> getPlayersDestinationCardStatus() {
        return mPlayersDestinationCardStatus;
    }

    public void receiveMessage(GameHistory gameHistory) {
        gameHistory.setChat(true);
        gameHistoryArr.add(gameHistory);
        if(activePresenter.getClass() == MessagePresenter.class) {
            activePresenter.update();
        }
        else activePresenter.displayError(gameHistory.getUser() + " sent a message!");
    }

    public void drawTrainCards(Integer player, Integer numberCards, TrainCard[] cards, Integer deckSize) {
        Player p = players.get(player);
        p.addTrainCards(cards);
        String user = p.getUser();

        String message = String.format("***%s drew %d Train card***", user, numberCards);
        GameHistory drewCard = new GameHistory(user, message);
        addGameHistoryObj(drewCard);
        trainDeckSize = deckSize;

        activePresenter.update();
    }

    public void discardTrainCards(Integer player, Integer numberCards, int[] pos, Integer deckSize) {
        Player p = players.get(player);
        p.removeTrainCards(pos);
        String user = p.getUser();

        String message = String.format("***%s discarded %d Train Cards***", user, numberCards);
        GameHistory discarded = new GameHistory(user, message);
        addGameHistoryObj(discarded);

        trainDeckSize = deckSize;

        activePresenter.update();
    }


    public void discardDestCards(Integer player, Integer numCards, int[] pos, Integer deckSize) {
        if(numCards == 0) return; //don't do anything if they didn't discard any cards

        Player p = players.get(player);
        if (player == userPlayer) {
            p.removeDestCards(pos);
        }
        else {
            p.removeDestCards(numCards);
        }

        if(p.isFirstDestPick()){
            p.setFirstDestPick(); //to false
        }

        String user = p.getUser();
        String message = String.format("***%s discarded %d Destination Cards***", user, numCards);
        gameHistoryArr.add(new GameHistory(user, message));

        this.destDeckSize = deckSize;

        activePresenter.update();
    }

    public int getDestDeckSize() {
        return destDeckSize;
    }

    public int getTrainDeckSize() { return trainDeckSize; }

    public void removeDestCardsFromDeck(int i) {
        destDeckSize -= i;
        activePresenter.update();
    }

    public void swapFaceUpCards(int[] pos, TrainCard[] cards, Integer deckSize) {
        int i = 0;
        for (int index : pos) {
            faceUpCards[index] = cards[i];
            i++;
        }

        this.trainDeckSize = deckSize;

        activePresenter.update();

    }

    public Board getBoard() {
        return board;
    }

    public int getLengthOfLongestPath() {
        return mLengthOfLongestPath;
    }

    public void initializeBoard(String JSONCities, String JSONRoutes) {
        this.board = new Board(JSONCities, JSONRoutes);
    }

    public void claimRoute(Integer player, String routeID) {
        Player p = players.get(player);
        board.claimRoute(p, routeID);
        String user = p.getUser();
        String message = String.format("***%s claimed a route %s***", user, routeID);
        GameHistory gameHistory = new GameHistory(user, message);
        addGameHistoryObj(gameHistory);
        activePresenter.update();
    }

    public void endGameRouteCalcSetup() {

        //create a map from Player to routesIds
        mAllRoutes = this.getBoard().getIdtoRouteMap();
        mEachPlayersDestinationCards = new HashMap<>();
        mEachPlayersCities = new HashMap<>();
        mAllClaimedRoutes = new HashMap<>();

        for (Player p : players) {

            // create a map from Player to DestinationCards
            mEachPlayersDestinationCards.put(p, p.getDestCards());

            //fill mAllClaimedRoutes
            ArrayList<Route> thisPlayersRoutes = new ArrayList<>();
            //create an empty array list for storing distinct cities
            mEachPlayersCities.put(p, new ArrayList<String>());
            // loop through routeIds for this player
            for (String routeId: p.getRoutesClaimed()){
                // get the route from the route id
                Route thisRoute = mAllRoutes.get(routeId);
                //if this route contains any cities that are not touched by other routes claimed by this player, save them. They are endpoints
                addCities(p, thisRoute);

                // add this Route to this players list of routes
                thisPlayersRoutes.add(thisRoute);
            }

            //add this players list of routes to map of Player to Routes (GameModel member variable)
            mAllClaimedRoutes.put(p, thisPlayersRoutes);
        }

    }

    public void makeSureSetupIsDone() {
        if (mAllRoutes == null) {
            this.endGameRouteCalcSetup();
        }
    }


     public Player calculateLongestRouteWinner() {

        makeSureSetupIsDone();

        //loop through the players in the game and find their longest route
         for (Player p: players) {

             List<String> thisPlayersDistinctCities = mEachPlayersCities.get(p);

             //recurse through the routes beginning at the (distinct cities)
             for (String city : thisPlayersDistinctCities) {
                 longestRouteHelper(p, city, new ArrayList<Route>(), 0);
             }
         }

         return getLongestPathWinner();
     }

    // Longest Path Related
    private void longestRouteHelper(Player p, String currentCity, List<Route> currentPath, int length){

        //look at the city at the end of that route and see if there is another route with that city
        List<Route> unSeenRoutesWithThisCity = playersOtherRoutesContainingThisCity(p, currentCity, currentPath);
        if (unSeenRoutesWithThisCity.size() == 0) {
            //end of path
            System.out.println("No routes with this city found, end of path");
            if (length > getLengthOfPath(mEachPlayersLongestPath.get(p))) {
                mEachPlayersLongestPath.remove(p);
                mEachPlayersLongestPath.put(p, currentPath);
            }
            return;
        }

        for (Route unSeenRoute : unSeenRoutesWithThisCity) {
            //go to the opposite end (the other city)
            String oppositeCity = getOppositeCity(currentCity, unSeenRoute);

            //keep track of the routes that are visited in a duplicated path list
            List<Route> duplicatePath = new ArrayList<>();
            for (Route r : currentPath) {
                duplicatePath.add(r);
            }
            duplicatePath.add(unSeenRoute);
            int thisLength = length + unSeenRoute.getLength();
            longestRouteHelper(p, oppositeCity, duplicatePath, thisLength);
        }

    }

    private Player getLongestPathWinner() {
        Player winner =  null;
        for (Map.Entry<Player, List<Route>> playerAndZerLongestPath : mEachPlayersLongestPath.entrySet()) {
            int thisPlayersLongestPathLength = getLengthOfPath(playerAndZerLongestPath.getValue());
            if (thisPlayersLongestPathLength > mLengthOfLongestPath) {
                mLengthOfLongestPath = thisPlayersLongestPathLength;
                winner = playerAndZerLongestPath.getKey();
            }
        }

        setLongestPathPoints(winner);

        return winner;
    }

    public void onGameEnded(List<Player> players){
        this.players = players;
        endGameRouteCalcSetup();
        checkDestinationCardCompletion();
        calculateLongestRouteWinner();
        ((GamePresenter)activePresenter).onGameEnded();
    }

    private void setLongestPathPoints (Player winner) {
        for (Player p : players) {
            if (winner.getUser().equals(winner.getUser())) {
                p.setLongestRoutePoints(10);
            } else {
                p.setLongestRoutePoints(0);
            }
        }
    }

    private int getLengthOfPath(List<Route> path) {
        int length = 0;
        if (path == null) return 0;
        else if (path.size() != 0) {
            for (Route route : path) {
                length += route.getLength();
            }
        }
        return length;
    }

    // Longest Path Related
    private void addCities(Player p, Route r){
        List<String> thisPlayersCities = mEachPlayersCities.get(p);

        thisPlayersCities.add(r.getSrcCity());
        thisPlayersCities.add(r.getDestCity());


    }

     // Destination Card Scoring Related
    public void checkDestinationCardCompletion(){

        //loop through destination cards and recurse
        for (Player p : players) {
            for (DestinationCard destinationCard : mEachPlayersDestinationCards.get(p)){
                Boolean thisCardsStatus = checkDestinationCardCompletionHelper(p, destinationCard.getSrcCity(), destinationCard.getDestCity(), new ArrayList<Route>());
                destinationCard.setFinished(thisCardsStatus);
            }
        }

    }

    // Destination Card Scoring Related
    private boolean checkDestinationCardCompletionHelper(Player p, String currentCity, String finalCity, List<Route> currentPath) {
        //start at starting city
        //check all the routes with this city
        List<Route> unSeenRoutesWithThisCity = playersOtherRoutesContainingThisCity(p, currentCity, currentPath);
        if (unSeenRoutesWithThisCity.size() == 0) {
            //if there is no route return false, end of path isn't the final city
            System.out.println("No routes with this city found, returning false");
            return false;
        }
        for (Route unSeenRoute : unSeenRoutesWithThisCity) {
            //go to the opposite end (the other city)
            String oppositeCity = getOppositeCity(currentCity, unSeenRoute);
            //if the opposite city is the destination city return true
            if (oppositeCity.equals(finalCity)) {
                System.out.println("Opposite city matches final city, returning true");
                return true;
            }
            else {
                //keep track of the routes that are visited in a duplicated path list
                List<Route> duplicatePath = new ArrayList<>();
                for (Route r : currentPath) {
                    duplicatePath.add(r);
                }
                duplicatePath.add(unSeenRoute);
                System.out.println("Opposite city does NOT match final city, recursing");
                return checkDestinationCardCompletionHelper(p, oppositeCity, finalCity, duplicatePath);
            }
        }
        System.out.println("Reached the end of the function without returning, returning false");
        return false;
    }

    // Destination Card Scoring Related
    private String getOppositeCity(String city, Route route) {
        if (city.equals(route.getDestCity())) {
            return route.getSrcCity();
        } else if (city.equals(route.getSrcCity())) {
            return route.getDestCity();
        } else return "city is not found in given route";
    }

    private List<Route> playersOtherRoutesContainingThisCity(Player p, String city, List<Route> currentPath) {

        //create new list of routes
        List<Route> unnavigatedRoutesContainingThisCity = new ArrayList<>();
        //loop through the players routes and search for the city
        for (Route route : mAllClaimedRoutes.get(p)){
            if (route.getSrcCity().equals(city)|| route.getDestCity().equals(city)) {
                //if this city is found check to make sure it isn't already in the current path
                if (!currentPath.contains(route)){
                    //if not found, add to list of routes
                    unnavigatedRoutesContainingThisCity.add(route);
                }
            }
        }

        //return list of routes
        return unnavigatedRoutesContainingThisCity;


    }

    public int getPlayersNumTrainCards(TrainCard card) {
        Player p = getUserPlayer();
        return p.getCountOfCardType(card);
    }
    public void updateState(Integer player, StateType state){
        Player p = players.get(player);
        p.setState(state);

        if(state == StateType.TURN_STATE){
            this.state = new TurnState((GamePresenter) activePresenter);
        }
        else if(state == StateType.NOT_TURN_STATE){
            this.state = new NotTurnState((GamePresenter) activePresenter);
        }
        else{
            this.state = new OneTrainPickedState((GamePresenter) activePresenter);
        }

        if(player == userPlayer){
            activePresenter.setState(this.state);
            activePresenter.displayError("It's your turn");
        }
    }

    public Route getRouteFromID(String routeId) {
        return board.getRouteFromID(routeId);
    }

    public int[] getClaimingCards(int length, TrainCard color) {
        return players.get(userPlayer).getRouteClaimingCards(length, color);
    }

    public State getState(){
        return state;
    }

    public City[] getCitiesFromDest(DestinationCard destCard) {
        return board.getCitiesFromDest(destCard);
    }

    public void finalTurn() {
        ((GamePresenter)activePresenter).finalTurn();
    }
}
