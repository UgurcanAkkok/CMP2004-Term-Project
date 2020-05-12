import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

public class Message {
    static final Logger LOGGER = Logger.getLogger(Message.class.getName());

    private static final String SEPERATOR = "#";
    private static final String PACKET_START = "UE11";
    private static final String PACKET_END = "11EU";

    private static final String LECTURE_JOIN = "LectureJoin";
    private static final String LECTURE_LEAVE = "LectureLeave";
    private static final String LECTURE_START = "LectureStart";
    private static final String LECTURE_END = "LectureEnd";
    private static final String ADDSHAPE = "AddShape";
    private static final String REMOVESHAPE = "RemoveShape";

    private static final List<String> opList = List.of(LECTURE_JOIN, LECTURE_END, LECTURE_LEAVE,
            LECTURE_START, ADDSHAPE, REMOVESHAPE);

    String operation;
    List<String> arguments;

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
    Message(String data) {
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
        }
        if (checkOperation(messageArray.get(0)) == false) {
            // TODO throw a custom error
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
