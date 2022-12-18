package fallout76.handler.kook;

import fallout76.handler.KookBaseHandler;

public interface KookGuildHandler extends KookBaseHandler {

    @Override
    default String channelType() {
        return "GROUP";
    }
}
