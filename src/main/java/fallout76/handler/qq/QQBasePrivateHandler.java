package fallout76.handler.qq;


import fallout76.handler.QQBaseHandler;

public interface QQBasePrivateHandler extends QQBaseHandler {
    @Override
    default String getPostType() {
        return "message";
    }

    @Override
    default String getMessageType() {
        return "private";
    }

}
