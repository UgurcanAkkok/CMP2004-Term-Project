import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

public class Message {
    static final Logger LOGGER = Logger.getLogger(Message.class.getName());

    public static final String SEPERATOR = "#";
    public static final String PACKET_START = "UE11";
    public static final String PACKET_END = "11EU";

    public static final String LECTURE_JOIN = "LectureJoin";
    /* UE11#LectureJoin#USERNAME#PASSWORD#11EU */
    public static final String LECTURE_LEAVE = "LectureLeave";
    /* UE11#LectureLeave#USERNAME#11EU */
    public static final String LECTURE_START = "LectureStart";
    public static final String LECTURE_END = "LectureEnd";
    public static final String JOIN_FAILURE = "JoinFailure";
    /* UE11#JoinFailure#USERNAME#11EU */
    public static final String ADDSHAPE = "AddShape";
    public static final String REMOVESHAPE = "RemoveShape";
    public static final String CHAT = "Chat";
    /* UE11#Chat#USERNAME#MESSAGE BODY#11EU */

    public static final List<String> opList = List.of(LECTURE_JOIN, LECTURE_END, LECTURE_LEAVE,
            LECTURE_START, ADDSHAPE, REMOVESHAPE);

    String operation;
    List<String> arguments;

    private static String pack(String msg) {
        return PACKET_START + SEPERATOR + msg + SEPERATOR + PACKET_END;
    }

    public static String chat(String user, String body) {
        var temp = String.join(SEPERATOR, CHAT, user, body);
        return pack(temp);
    }

    public static String lectureJoin(String user, String pass) {
        var temp = String.join(SEPERATOR, LECTURE_JOIN, user, pass);
        return pack(temp);
    }

    public static String joinFailure(String user) {
        var temp = String.join(SEPERATOR, JOIN_FAILURE, user);
        return pack(temp);
    }

    public List<String> getarguments() {
        return this.arguments;
    }

    public String getoperation() {
        return this.operation;
    }

    /*
     * Message Format: UE11#<OPERATION>#[ ARGUMENT1 ]#[ ARGUMENT2 ]#[...]#11EU
     * Example Message: UE11#LectureStart#41287#11EU
     */
    Message(String data) throws InvalidMessageException {
        var messageArray = List.of(data.split(SEPERATOR));
        if ((messageArray.get(0) != PACKET_START)
                || (messageArray.get(messageArray.size() - 1) != PACKET_END)) {
            LOGGER.log(Level.WARNING, "We have an invalid message, " + messageArray.toString());
        } else {
            /*
             * After checking if message is from correct source, removes the unneccessary
             * parts
             */
            messageArray.remove(0);
            messageArray.remove(messageArray.size() - 1);
            throw new InvalidMessageException();
        }
        if (checkOperation(messageArray.get(0)) == false) {
            throw new InvalidMessageException(data);
        } else {
            operation = messageArray.get(0);
            arguments = messageArray.subList(1, messageArray.size());
        }

    }

    Boolean checkOperation(String op) {
        /* Returns false if op is not a valid one */
        return opList.contains(op);
    }

}
