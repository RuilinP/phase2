package Controller;

import Gateway.UserGate;
import Interface.UserMessageLoadSave;
import Interface.UserData;

import Presenter.GamePresenter;

import UseCase.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * the controller class that interacts with Userinputs from RegularUser
 */
public class RegularUserNavigatorController implements UserData {
    private String username;
    /**
     * the constructor for this controller class
     * @param un
     */
    public RegularUserNavigatorController(String un){
        username = un;
    }
    /**
     * return the current User's username
     * @return: the username of current (Regular)User
     */
    public String currentUser(){
        return username;
    }


    /**
     * the main method that is run to accept user inputs and redirect to corresponding controllers
     */
    public void run() {

        while (true) {
            System.out.println();
            System.out.println();
            System.out.println();


            GamePresenter gamePresenter = new GamePresenter();
            ArrayList<String> choices = new ArrayList<>();
            choices.add("1. Select a Game to create/edit/play");
            choices.add("2. Open message box");
            choices.add("3. Reset password");
            choices.add("3. Logout");
            int choice = 1 + gamePresenter.displayChoices(this, choices, "Hello, "+ username + ". what would you like to do?");
            if (choice == 1) {
                /* Game*/

                TemplateEditorController te = new TemplateEditorController();
                GameMainController gameController = new GameMainController(te ,this::currentUser);
                gameController.gameMenu();

            } else if (choice == 2){
                MessageController c1 = new MessageController(username);
                c1.run();
            }
            else if(choice == 4) {
                /*logout*/
                gamePresenter.displayTextScene(this, "CONTINUE", "Successfully logged out");
                break;

            } else if (choice ==3){
                ArrayList<String> inputs = new ArrayList<>();
                inputs.add("New password:");
                List<String> userinputs = gamePresenter.displayInputs(this, inputs, "Reset password");
                String password = userinputs.get(0);
                UserMessageLoadSave gate = new UserGate();
                UserManager userManager = new UserManager(gate);
                boolean status = userManager.resetPassword(this.username, password);
                if (status){
                    gamePresenter.displayTextScene(this, "CONTINUE", "Reset successful");
                } else {
                    gamePresenter.displayTextScene(this, "BACK", "Password of poor Strength, please try again!");
                }
            } else {
                gamePresenter.displayTextScene(this, "BACK","Invalid choice, please try again");
            }
        }
    }
}
