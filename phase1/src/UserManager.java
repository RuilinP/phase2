import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> bufferedUsers;

    public UserManager(){
        this.bufferedUsers = new ArrayList<User>();
    }

    public void addUser(){}

    public User SearchUser(String username){
        int i;
        User temp;
        for (i = 0; i < bufferedUsers.length; i++) {
            temp = bufferedUsers[i];
            if (temp.getUsername(username) = username) {
                return temp;
            }
        }
        /* Username not found */

    }

    @Override
    public String toString() {
        return "UserManager{" +
                "bufferedUsers=" + bufferedUsers +
                '}';
    }
}


