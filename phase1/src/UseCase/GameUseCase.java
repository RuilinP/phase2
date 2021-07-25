package UseCase;
import Entity.Game;
import Gateway.GameGateway;
import Interface.SaveLoadGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GameUseCase {
    public static void main(String[] args) {
        HashMap<Integer, String> gameData = new HashMap<>();
        gameData.put(-4, "bruh");
        gameData.put(-3, "le bruh");
        gameData.put(-2, "false");
        gameData.put(-1, "4");
        gameData.put(0, "0");
        gameData.put(1, "1");
        gameData.put(2, "2");
        gameData.put(3, "3");
        gameData.put(4, "4");
        gameData.put(5, "5");
        gameData.put(21, "21");
        gameData.put(9, "9");

        List<HashMap> gamesData = new ArrayList<>();
        gamesData.add(gameData);
        GameGateway bruh = new GameGateway();
        bruh.save_games(gamesData);

        GameUseCase gameUseCase = new GameUseCase(new GameGateway());
        Game game = gameUseCase.privateGames.get(0);
        System.out.println(game);
//        System.out.println(game.getDialogueById(1));
//        System.out.println(game.getDialogueById(4));
//        System.out.println(game.getDialogueById(21));
//        System.out.println(game.getDialogueById(9));
//        System.out.println(game.getDialogueById(5));

    }

    private ArrayList<Game> publicGames = new ArrayList<>();
    private ArrayList<Game> privateGames = new ArrayList<>();
    private SaveLoadGame database;
    private Game currentGame;

    public GameUseCase(SaveLoadGame database){
        this.database = database;
        List<HashMap> gamesData = database.load_games();
        for (HashMap gameData: gamesData){
            Game game = hashMapToGame(gameData);
            if (game.getGamePublic()){
                this.publicGames.add(game);
            }
            else {
                this.privateGames.add(game);
            }
        }
    }

    public void createGame(int number_choice, String game_name, String author_name, String initial_dialogue){
        Game game = new Game(game_name, author_name, false, number_choice, initial_dialogue);
        privateGames.add(game);
    }

    public String getGameAsString(String gameName){
        return this.getGameByName(gameName).toString();
    }

    private Game getGameByName(String gameName){
        for(Game game: publicGames){
            if (game.getGameName().equals(gameName)){
                return game;
            }
        }
        for(Game game: privateGames){
            if (game.getGameName().equals(gameName)){
                return game;
            }
        }
        return null;
    }

    public ArrayList<String> getPublicGames(){
        ArrayList<String> game_list = new ArrayList<>();
        for(Game game: publicGames){
            game_list.add(game.getGameName());

        }
        return game_list;
    }

    public ArrayList<String> getPrivateGamesByAuthor(String author_name){
        ArrayList<String> game_list = new ArrayList<>();
        for(Game game: privateGames){
            if (game.getGameAuthor().equals(author_name)){
                game_list.add(game.getGameName());
            }
        }
        return game_list;
    }

    public ArrayList<String> getPublicGamesByAuthor(String author_name){
        ArrayList<String> game_list = new ArrayList<>();
        for(Game game: publicGames){
            if (game.getGameAuthor().equals(author_name)){
                game_list.add(game.getGameName());
            }
        }
        return game_list;
    }

    private HashMap<Integer, String> gameToHashMap(Game game){
        ArrayList<Integer> ids = game.getAllId();
        HashMap<Integer, String> gameData = new HashMap<>();
        gameData.put(-4, game.getGameName());
        gameData.put(-3, game.getGameAuthor());
        gameData.put(-2, String.valueOf(game.getGamePublic()));
        gameData.put(-1, String.valueOf(game.getchoiceNumLimit()));
        for (int id: ids){
            gameData.put(id, game.getDialogueById(id));
        }

        return gameData;
    }

    private Game hashMapToGame(HashMap<Integer, String> hashMap){
        if(!hashMap.containsKey(0) || !hashMap.containsKey(-1) || !hashMap.containsKey(-2) ||
                !hashMap.containsKey(-3) || !hashMap.containsKey(-4)){
            System.out.println("hashmap to game failed.");
            return null;
        }
        Game game = new Game(hashMap.get(-4), hashMap.get(-3), Boolean.getBoolean(hashMap.get(-2)),
                Integer.parseInt(hashMap.get(-1)), hashMap.get(0));

        ArrayList<Integer> childrenDialogueIds = new ArrayList<>();
        for ( int key : hashMap.keySet() ) {
            if (key > 0){
                childrenDialogueIds.add(key);
            }
        }
        ArrayList<Integer> parentDialogueIds = game.getParentDialogueIds(childrenDialogueIds);
        if(!checkHashmapFormat(parentDialogueIds, childrenDialogueIds)){
            System.out.println("hashmap to game failed.");
            return null;
        }

        addDialoguesToGame(parentDialogueIds, childrenDialogueIds, game, hashMap,0);

        return game;
    }

    private boolean checkHashmapFormat(ArrayList<Integer> parentDialogueIds,
                                       ArrayList<Integer> childrenDialogueIds){
        for (int parentId: parentDialogueIds){
            if (!childrenDialogueIds.contains(parentId) && parentId != 0){
                return false;
            }
        }
        return true;
    }

    private void addDialoguesToGame(ArrayList<Integer> parentDialogueIds, ArrayList<Integer> childrenDialogueIds,
                                   Game game, HashMap<Integer, String> hashMap, int parentDialogueId){
        ArrayList<Integer> queue = new ArrayList<>();
        for (int i = 0; i < parentDialogueIds.size(); i++){
            if (parentDialogueIds.get(i) == parentDialogueId){
                queue.add(childrenDialogueIds.get(i));
            }
        }
        Collections.sort(queue);

        for (int childId: queue){
            game.addChoiceToDialogue(hashMap.get(childId), parentDialogueId);
            addDialoguesToGame(parentDialogueIds, childrenDialogueIds, game, hashMap, childId);
        }
    }

    public boolean setGamePublic(String game_name){
        for (Game game: this.privateGames){
            if(game.getGameName().equals(game_name)){
                game.setGamePublic(true);
                publicGames.add(game);
                privateGames.remove(game);
                return true;
            }
        }
        return false;
    }

    public void saveGames(){
        List<HashMap> gamesData = new ArrayList<HashMap>();
        for (Game game: this.privateGames){
            gamesData.add(this.gameToHashMap(game));
        }

        for (Game game: this.publicGames){
            gamesData.add(this.gameToHashMap(game));
        }

        database.save_games(gamesData);
    }
}