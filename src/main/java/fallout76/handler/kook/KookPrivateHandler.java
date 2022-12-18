package fallout76.handler.kook;

import fallout76.handler.KookBaseHandler;

public interface KookPrivateHandler extends KookBaseHandler {

    @Override
    default String channelType() {
        return "PERSON";
    }
}
