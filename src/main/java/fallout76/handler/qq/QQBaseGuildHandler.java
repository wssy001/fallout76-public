package fallout76.handler.qq;


public interface QQBaseGuildHandler extends QQBaseHandler {
    @Override
    default String getPostType() {
        return "message";
    }

    @Override
    default String getMessageType() {
        return "guild";
    }

}
