package Entity;

import Interface.GameStorage;

import java.util.ArrayList;
import java.util.List;

import Exception.AddDialogueException;

public class GameTree implements GameStorage {
    public static void main(String[] args) {
        GameTree it = (GameTree) GameStorage.initializeStorage(4, "0");
        System.out.println(it.choiceNumLimit + it.id);
        it.addChoiceToDialogue("1", 0);
        it.addChoiceToDialogue("2", 0);
        it.addChoiceToDialogue("5", 1);
        it.addChoiceToDialogue("6", 1);
        it.addChoiceToDialogue("7", 1);
        it.addChoiceToDialogue("8", 1);
        it.addChoiceToDialogue("9", 2);
        it.addChoiceToDialogue("25", 6);
//        System.out.println(it);
//        System.out.println(it.getDialogueById(25));
//        it.deleteDialogueById(5);
//        System.out.println(it);
//        System.out.println(it.getDialogueById(21));
        System.out.println(it.addChoiceToDialogue("bruh", 70));
        System.out.println(it.getParentId(3));
        System.out.println(it.getParentId(2));
        System.out.println(it.getParentId(5));



    }

    private int id;
    final int choiceNumLimit;
    private String dialogue;
    private List<GameTree> subtrees = new ArrayList<GameTree>();
    private GameTree parentTree;

    public GameTree(int choiceNumLimit, String initialDialogue){
        this.choiceNumLimit = choiceNumLimit;
        this.dialogue = initialDialogue;
        this.parentTree = null;
        this.id = this.setId();
    }

    private GameTree(String childDialogue, int parentDialogueId, GameTree initialTree) throws AddDialogueException {
        this.dialogue = childDialogue;
        try{
            this.parentTree = initialTree.getTreeById(parentDialogueId);
        }
        catch(IndexOutOfBoundsException b){
            throw new AddDialogueException("Cannot find parent dialogue.");
        }
        this.choiceNumLimit = this.parentTree.choiceNumLimit;

        boolean addSuccess = this.parentTree.addToSubtrees(this);
        if (!addSuccess){
            throw new AddDialogueException("Max number of choices exceeded.");
        }

        this.id = this.setId();
    }

    public boolean addChoiceToDialogue(String childDialogue, int parentDialogueId){
        try{
            new GameTree(childDialogue, parentDialogueId, this);
            return true;
        }
        catch (AddDialogueException b){
            System.out.println(b.getMessage());
            return false;
        }
    }

    private int setId(){
        if(this.parentTree == null){
            return 0;
        }
        else{
            int parentId = this.parentTree.id;
            return parentId * choiceNumLimit + this.parentTree.subtrees.indexOf(this) + 1;
        }
    }

    private void resetAllId(){
        id = setId();

        for (GameTree tree: subtrees){
            tree.resetAllId();
        }
    }

    public ArrayList<Integer> getAllId(){
        ArrayList<Integer> allId = new ArrayList<>();
        allId.add(this.id);
        for (GameTree subtree : this.subtrees) {
            allId.addAll(subtree.getAllId());
        }
        return allId;
    }

    private Integer getParentId(int childId){
        if (childId == 0){
            return null;
        }
        else if (childId % choiceNumLimit == 0){
            return childId / choiceNumLimit - 1;
        }
        else{
            return childId / choiceNumLimit;
        }
    }

    public ArrayList<Integer> getParentDialogueIds(List<Integer> childrenDialogueIds){
        ArrayList<Integer> parentIds = new ArrayList<>();
        for (int children: childrenDialogueIds){
            parentIds.add(getParentId(children));
        }
        return parentIds;
    }

    private boolean addToSubtrees(GameTree tree){
        if (subtrees.size() < choiceNumLimit){
            this.subtrees.add(tree);
            return true;
        }
        else{
            return false;
        }
    }


    private GameTree getTreeById(int id) throws IndexOutOfBoundsException{
        GameTree tree = this.getInitialTree();
        ArrayList<Integer> path = this.IDtoNodePath(id);

        for (int i : path){
            tree = tree.subtrees.get(i - 1);
        }

        return tree;
    }

    // helper function to getSpecificDialogue()
    // returns the initial tree
    private GameTree getInitialTree(){
        GameTree tree = this;
        while (this.parentTree != null){
            tree = this.parentTree;
        }
        return tree;
    }

    // helper function to getSpecificDialogue()
    private ArrayList<Integer> IDtoNodePath(int id){
        ArrayList<Integer> path = new ArrayList<Integer>();
        int ID = id;
        while (ID != 0){
            int remainder = ID % choiceNumLimit;
            ID = ID / choiceNumLimit;
            if (remainder == 0){
                remainder = choiceNumLimit;
                ID -= 1;
            }
            path.add(0, remainder);
        }
        return path;
    }

    // gets a specific dialogue through a tree ID
    public String getDialogueById(int id) {
        try{
            return getTreeById(id).dialogue;
        }
        catch(IndexOutOfBoundsException b){
            return null;
        }
    }

    public boolean setDialogueById(int id, String dialogue){
        try{
            getTreeById(id).dialogue = dialogue;
            return true;
        }
        catch(IndexOutOfBoundsException b){
            return false;
        }
    }

    public boolean deleteDialogueById(int id){
        if (id == 0){ return false; }
        try{
            GameTree initialTree = this.getInitialTree();
            GameTree tree = initialTree.getTreeById(id);
            ArrayList<GameTree> subtrees = (ArrayList<GameTree>) tree.parentTree.subtrees;
            int index = subtrees.indexOf(tree);
            if (index == -1){ return false; }
            else{
                subtrees.remove(index);
                if (subtrees.size() != index){
                    tree.parentTree.resetAllId();
                }
                return true;
            }
        }
        catch(IndexOutOfBoundsException b){ return false; }
    }

    private GameTree getTreeByDialogue(String dialogue){
        GameTree tree = null;
        if(this.dialogue.equals(dialogue)){
            return this;
        }

        for (GameTree i : this.subtrees){
            if(tree == null){
                tree = i.getTreeByDialogue(dialogue);
            }
            else{
                break;
            }
        }

        return tree;
    }

    public int getIdByDialogue(String dialogue){
        return getInitialTree().getTreeByDialogue(dialogue).id;
    }

    // Returns how many nodes the tree has
    public int size(){
        int total = 0;
        for (GameTree subtree : this.subtrees) {
            total += 1 + subtree.size();
        }

        if (this.id == 0){
            return total + 1;
        }

        return total;
    }

    @Override
    public String toString(){
        return printTree(0);
    }

    private String printTree(int depth){
        String space = this.depthToPrespace(depth);

        if (this.subtrees.size() == 0){
            return space + "(" + this.id + ")";
        }
        StringBuilder tree = new StringBuilder(space + "(" + this.id + ")");
        for (GameTree subtree : this.subtrees) {
            tree.append(subtree.printTree(depth + 1));
        }
        return tree.toString();
    }

    // helper function 1 to printTree()
    private String depthToPrespace(int depth){
        StringBuilder space = new StringBuilder("\n");
        for (int i = 0; i < depth; i++){
            space.append("     ");
        }
        return space.toString();
    }
}