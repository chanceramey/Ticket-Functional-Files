package com.team.jcti.ttr.models;

import com.team.jcti.ttr.IGamePresenter;
import com.team.jcti.ttr.communication.ServerProxy;
import com.team.jcti.ttr.drawdestinationcard.DrawDestinationCardPresenter;
import com.team.jcti.ttr.message.MessagePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import interfaces.IGame;
import model.Color;
import model.DestinationCard;
import model.FinalGamePoints;
import model.GameHistory;

import model.Game;
import model.playerStates.Player;
import model.TrainCard;

/**
 * Created by Jeff on 2/2/2018.
 */

public class ClientGameModel extends Observable implements IGame {

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
    private boolean turn;
    private int gameHistoryPosition;
    private List<GameHistory> gameHistoryArr = new ArrayList<>();
    private IGamePresenter activePresenter;
    private TrainCard[] faceUpCards;
    private FinalGamePoints[] allPlayersFinalPoints;
    private int destDeckSize;
    private int trainDeckSize;
    private int currentPlayer;
    private int exitNum;

    // Longest path or destination card calculation related
    private List<Route> mThisPlayersLongestPath = new ArrayList<>();
    private List<String> mThisPlayersCities = new ArrayList<>();
    private List<DestinationCard> mThisPlayersDestinationCards = new ArrayList<>();
    private List<Route> mThisPlayersClaimedRoutes = new ArrayList<>();
    private Map<DestinationCard, Boolean> mThisPlayersDestinationCardStatus = new HashMap<>();
    private Map<String, Route> mAllRoutes;
    private int mLengthOfLongestPath = 0;
    private boolean finalPointsReceived = false;
    // until here


    public boolean isMyTurn() {
        return turn;
    }

    public boolean exitDrawDest() {
        if (exitNum == 0) return false;
        exitNum--;
        return true;
    }


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


        gameHistoryPosition = 0;
        turn = false;
        trainDeckSize = 240;
        destDeckSize = 30;

        this.active = true;
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

    public String getID() {
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
            player.addDrawnDestCards(cards);
            activePresenter.drawDestCards();
            System.out.println(activePresenter);
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
        Player p = players.get(player);
        if (player == userPlayer) {
            p.removeDestCards(pos);
            exitNum++;
        }
        else {
            p.removeDestCards(numCards);
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
        if(player == userPlayer) updateDestCards();
        activePresenter.update();
    }

    private void updateDestCards() {
        endGameRouteCalcSetup();
        for (DestinationCard card : players.get(userPlayer).getDestCards()) {
            if (card.isFinished()) continue;
            boolean finished = checkDestinationCardCompletionHelper(players.get(userPlayer), card.getSrcCity(), card.getDestCity(), new ArrayList<Route>());
            card.setFinished(finished);
        }
    }

    public void endGameRouteCalcSetup() {

        //create a map from Player to routesIds
        mAllRoutes = this.getBoard().getIdtoRouteMap();

        Player p = this.getUserPlayer();

            // create a map from Player to DestinationCards
            mThisPlayersDestinationCards = p.getDestCards();

        //fill mAllClaimedRoutes
        //create an empty array list for storing distinct cities
        // loop through routeIds for this player
        for (String routeId: p.getRoutesClaimed()){
            // get the route from the route id
            Route thisRoute = mAllRoutes.get(routeId);
            //if this route contains any cities that are not touched by other routes claimed by this player, save them. They are endpoints
            addCities(p, thisRoute);

            // add this Route to this players list of routes
            if (!mThisPlayersClaimedRoutes.contains(thisRoute)) mThisPlayersClaimedRoutes.add(thisRoute);
        }


    }

    public void makeSureSetupIsDone() {
        if (mAllRoutes == null) {
            this.endGameRouteCalcSetup();
        }
    }


     public void calculateThisPlayersLongestRoute() {

         makeSureSetupIsDone();

         //loop through the players in the game and find their longest route

         Player p = this.getUserPlayer();

         //recurse through the routes beginning at the (distinct cities)
         for (String city : mThisPlayersCities) {
             longestRouteHelper(p, city, new ArrayList<Route>(), 0);
         }

     }

    // Longest Path Related
    private void longestRouteHelper(Player p, String currentCity, List<Route> currentPath, int length){

        //look at the city at the end of that route and see if there is another route with that city
        List<Route> unSeenRoutesWithThisCity = playersOtherRoutesContainingThisCity(currentCity, currentPath);
        if (unSeenRoutesWithThisCity.size() == 0) {
            //end of path
            System.out.println("No routes with this city found, end of path");
            if (length > getLengthOfPath(mThisPlayersLongestPath)) {
                mThisPlayersLongestPath = currentPath;
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

    public void onGameEnded(){
        endGameRouteCalcSetup();
        checkThisPlayersDestinationCardCompletion();
        calculateThisPlayersLongestRoute();
        FinalGamePoints myFinalPoints = createFinalGamePointsObject();
        sendFinalPoints(myFinalPoints);
        activePresenter.onGameEnded();
    }

    private void sendFinalPoints(FinalGamePoints myFinalPoints){
        ServerProxy serverProxy = ServerProxy.getInstance();
        ClientModel clientModel = ClientModel.getInstance();
        String auth = clientModel.getAuthToken();
        serverProxy.sendFinalPoints(auth, gameId, myFinalPoints);
    }

    private FinalGamePoints createFinalGamePointsObject() {
        int finishedDestinationPoints = 0;
        int unfinishedDestinationPoints = 0;
        for (DestinationCard destinationCard : mThisPlayersDestinationCards) {
            if (destinationCard.isFinished() == true){
                finishedDestinationPoints += destinationCard.getPointValue();
            } else {
                unfinishedDestinationPoints -= destinationCard.getPointValue();
            }
        }
        FinalGamePoints   myFinalPoints = new FinalGamePoints(userPlayer, getRoutePoints(mThisPlayersClaimedRoutes), finishedDestinationPoints, unfinishedDestinationPoints, getLengthOfPath(mThisPlayersLongestPath));
        return  myFinalPoints;
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

    public int getRoutePoints(List<Route> routes) {
        if (routes.size() <= 0) {
            return 0;
        }
        int points = 0;
        for (Route r : routes) {
            points += r.getPointValue();
        }
        return points;
    }

    // Longest Path Related
    private void addCities(Player p, Route r){

        mThisPlayersCities.add(r.getSrcCity());
        mThisPlayersCities.add(r.getDestCity());
    }

     // Destination Card Scoring Related
    public void checkThisPlayersDestinationCardCompletion(){

        Player p = this.getUserPlayer();

        //loop through destination cards of this playerOnly and recurse
        for (DestinationCard destinationCard : mThisPlayersDestinationCards){
            Boolean thisCardsStatus = checkDestinationCardCompletionHelper(p, destinationCard.getSrcCity(), destinationCard.getDestCity(), new ArrayList<Route>());
            destinationCard.setFinished(thisCardsStatus);
        }


    }

    // Destination Card Scoring Related
    private boolean checkDestinationCardCompletionHelper(Player p, String currentCity, String finalCity, List<Route> currentPath) {
        //start at starting city
        //check all the routes with this city
        List<Route> unSeenRoutesWithThisCity = playersOtherRoutesContainingThisCity(currentCity, currentPath);
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

    private List<Route> playersOtherRoutesContainingThisCity(String city, List<Route> currentPath) {

        //create new list of routes
        List<Route> unnavigatedRoutesContainingThisCity = new ArrayList<>();
        //loop through the players routes and search for the city
        for (Route route : mThisPlayersClaimedRoutes){
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

    public FinalGamePoints[] getAllPlayersFinalPoints() {
        return allPlayersFinalPoints;
    }

    public Route getRouteFromID(String routeId) {
        return board.getRouteFromID(routeId);
    }

    public int[] getClaimingCards(int length, TrainCard color) {
        return players.get(userPlayer).getRouteClaimingCards(length, color);
    }

    public City[] getCitiesFromDest(DestinationCard destCard) {
        return board.getCitiesFromDest(destCard);
    }

    public void setTurn(int player) {
        if (player == userPlayer) {
            activePresenter.displayError("It's your turn!");
            turn = true;
        }
        else {
            turn = false;
        }
        this.currentPlayer = player;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setLastTurn() {
        activePresenter.displayError("Last round of turns!!!");
    }

    public boolean playerHasEnoughTrains(int length) {
        if (players.get(userPlayer).getNumTrains() < length) return false;
        else return true;
    }


    public void setAllFinalPoints(FinalGamePoints[] allFinalPoints) {
        this.allPlayersFinalPoints = allFinalPoints;
        finalPointsReceived = true;
        activePresenter.update();
        System.out.println("All player points received");
    }

    public boolean isFinalPointsReceived() {
        return finalPointsReceived;
    }

    public void clearGame() {
        active = false;

        board = null;
        players = new ArrayList<>();
        gameId = null;
        userPlayer = -1;
        turn = false;
        gameHistoryPosition = -1;
        gameHistoryArr = new ArrayList<>();
        activePresenter = null;
        faceUpCards = new TrainCard[5];
        allPlayersFinalPoints = null;
        exitNum = 0;

        mThisPlayersLongestPath = new ArrayList<>();
        mThisPlayersCities = new ArrayList<>();
        mThisPlayersDestinationCards = new ArrayList<>();
        mThisPlayersClaimedRoutes = new ArrayList<>();
        mThisPlayersDestinationCardStatus = new HashMap<>();
        mAllRoutes = new HashMap<>();
        mLengthOfLongestPath = 0;
        finalPointsReceived = false;
    }
}
