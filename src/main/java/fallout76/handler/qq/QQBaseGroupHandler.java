package fallout76.handler.qq;


public interface QQBaseGroupHandler extends QQBaseHandler {
    @Override
    default String getPostType() {
        return "message";
    }

    @Override
    default String getMessageType() {
        return "group";
    }

}
