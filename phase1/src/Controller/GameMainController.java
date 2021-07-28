package Controller;

import Gateway.GameGate;
import Presenter.GamePresenter;
import UseCase.GameUseCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameMainController {
    public static void main(String[] args) {
        GameMainController gameController = new GameMainController();
//        gameController.regularGameMenu();
//        gameController.guestViewsGame();
        gameController.guestGameMenu();
    }

    private GameUseCase gameUseCase;
    private GameCreateController gameCreator;
    private GamePlayController gamePlayer;
    private GameEditController gameEditor;
    private GamePresenter gamePresenter = new GamePresenter();
    private Scanner scanner = new Scanner(System.in);


    public GameMainController(){
        gameUseCase = new GameUseCase(new GameGate());
        gameCreator = new GameCreateController(gameUseCase);
        gameEditor = new GameEditController(gameUseCase);
        gamePlayer = new GamePlayController(gameUseCase);
    }

    // This game menu is for regular/admin users
    public void regularGameMenu(){
        gamePresenter.displayScene("Choose and enter the corresponding integer.",
                new ArrayList<>(Arrays.asList(new String[]{
                "1: Create Game", "2: Edit Game", "3: Play Game", "4: View Games", "5: Exit"})));

        int userChoice = 0;
        while (true){
            try{
                userChoice = Integer.valueOf(scanner.next());
            }
            catch(NumberFormatException e){
                System.out.println(e);
            }

            switch (userChoice){
                case 1:{
                    System.out.println("bruh");
                }
                case 2:{
                    System.out.println("bruh");
                }
                case 3:{
                    System.out.println("bruh");
                }
                case 4:{
                    System.out.println("bruh");
                }
                case 5:{
                    break;
                }
            }
        }
    }


    // This game menu is for guest users
    public void guestGameMenu(){

        int userChoice = 0;
        while (true){
            gamePresenter.displayScene("Choose and enter the corresponding integer.",
                    new ArrayList<>(Arrays.asList(new String[]{"1: Create Game (Unsavable)",
                            "2: Play Game", "3: View Games", "4: Exit"})));
            try{
                userChoice = Integer.valueOf(scanner.next());
            }
            catch(NumberFormatException e){
                System.out.println(e);
            }

            if(userChoice == 1){
                System.out.println("bruh");
            }
            else if(userChoice == 2){
                System.out.println("?");
            }
            else if(userChoice == 3){
                this.guestViewsGame();
            }
            else if(userChoice == 4){
                break;
            }
        }
    }

    private void guestViewsGame(){
        int userChoice = 0;
        while (true) {
            ArrayList<String> publicGames = gameUseCase.getPublicGames();
            gamePresenter.displayScene("Enter 1 to exit.", publicGames);
            try {
                userChoice = Integer.valueOf(scanner.next());
            }
            catch (NumberFormatException e) {
                System.out.println(e);
            }

            if (userChoice == 1){
                break;
            }
        }
    }


}