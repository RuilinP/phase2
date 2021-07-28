package Controller;
import Entity.Game;
import UseCase.GameUseCase;


public class GameUpdater {

    public boolean gameChangeDialogue(Game game, String dialogue,int id ) {
        return game.setDialogueById(id, dialogue);
    }

    public boolean gameChangeChoice(Game game, String dialogue,int id ) {
        return game.setDialogueById(id, dialogue);
    }


    public void SetGamePrivate(Game game){
        game.setGamePublic(false);
    }

    public boolean addDialogue(Game game, String dialogue, int parentId){
        String P = game.getDialogueById(parentId);
        if(GameUseCase.choiceOfDialogue2(P, game).size() < game.getchoiceNumLimit()){
            game.addChoiceToDialogue(dialogue, parentId);
            return true;
        }else{
            System.out.println("oops, you have limit,you cant add more ");
            return false;
        }
    }

    public boolean addChoice(Game game, String dialogue, int parentId){
        String P = game.getDialogueById(parentId);
        if(GameUseCase.choiceOfDialogue2(P, game).size() < game.getchoiceNumLimit()){
            game.addChoiceToDialogue(dialogue, parentId);
            return true;
        }else{
            System.out.println("oops, you have limit,no more choice!");
            return false;
        }
    }
}
