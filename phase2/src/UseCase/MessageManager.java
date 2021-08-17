package UseCase;

import Entity.AdminUser;
import Entity.Message;
import Entity.RegularUser;
import Entity.User;
import Gateway.MessageGate;

import java.util.*;

public class MessageManager {
    private List<Message> bufferedMessages;
    private UserManager um = new UserManager();

    public MessageManager(){
        bufferedMessages = new ArrayList<>();


        MessageGate MG = new MessageGate();
        HashMap<String, List<Object>> tempMessages = (HashMap<String, List<Object>>) MG.load().get(0);

        for (Map.Entry mapElement :tempMessages.entrySet()){
            String id = (String)mapElement.getKey();
            List lst = (List)mapElement.getValue();
            String msg = (String)lst.get(0);
            String from = (String)lst.get(1);
            String to = (String)lst.get(2);
            String attachment = (String)lst.get(3);
            Date time = (Date)lst.get(4);
            boolean status = (boolean) lst.get(5);

            Message tempMessage = new Message(id, msg, from, to ,attachment,time, status);
            bufferedMessages.add(tempMessage);


        }

    }

    public boolean addMessage(ArrayList<Object> info){
        String to = (String)info.get(2);
        if (um.SearchUser(to) != null){
            String id = UUID.randomUUID().toString();
            String from = (String)info.get(1);
            String attachment = (String)info.get(3);
            Date time = (Date)info.get(4);
            String msg = (String)info.get(0);
            bufferedMessages.add(new Message(id, msg,from,to, attachment, time,false));

            save(id, msg,from,to,attachment, time,false);
            return true;
        } else {return false;}


    }

    public void addMessageEveryone(ArrayList<Object> info){
        String from = (String)info.get(0);
        Date time = (Date)info.get(3);
        String msg = (String)info.get(1);
        String attachment = (String)info.get(2);
        List<User> tos = um.getBufferedUsers();
        for (User temp: tos){
            String id = UUID.randomUUID().toString();
            bufferedMessages.add(new Message(id,msg,from,temp.getUsername(),attachment,time,false));
            save(id, msg,from,temp.getUsername(),attachment,time,false);
        }
    }


    public List<Message> getMessage(String username){
        ArrayList<Message> msgs = new ArrayList<>();
        for (Message msg: bufferedMessages){
            if (msg.getTo().equals(username)) {
                msgs.add(msg);

                save(msg.getid(), msg.getMsg(),msg.getFrom(),msg.getTo(),msg.getAttachment(),msg.getTime(),true);
            }
        }
        return msgs;
    }


    public boolean deleteMessage(String id){
        for (Message msg: bufferedMessages){
            if (msg.getid().equals(id)) {
                bufferedMessages.remove(msg);
                save2(msg.getid(), msg.getMsg(),msg.getFrom(),msg.getTo(),msg.getAttachment(),msg.getTime(),true);
                return true;
            }
        }
        return false;
    }

    private void save(String id,String msg, String from, String to,String attachment, Date time, boolean status){
        MessageGate MG = new MessageGate();
        HashMap<String, List<Object>> oldMessages = (HashMap<String, List<Object>>) MG.load().get(0);

        List<Object> sections = new ArrayList <Object>();
        sections.add(msg);
        sections.add(from);
        sections.add(to);
        sections.add(attachment);
        sections.add(time);
        sections.add(status);
        oldMessages.put(id, sections);
        List<HashMap> MessageData = new ArrayList<HashMap>();
        MessageData.add(oldMessages);

        MG.save(MessageData);
    }

    private void save2(String id,String msg, String from, String to,String attachment, Date time, boolean status){
        MessageGate MG = new MessageGate();
        HashMap<String, List<Object>> oldMessages = (HashMap<String, List<Object>>) MG.load().get(0);

        List<Object> sections = new ArrayList <Object>();
        sections.add(msg);
        sections.add(from);
        sections.add(to);
        sections.add(attachment);
        sections.add(time);
        sections.add(status);
        oldMessages.remove(id, sections);
        List<HashMap> MessageData = new ArrayList<HashMap>();
        MessageData.add(oldMessages);

        MG.save(MessageData);
    }
}
