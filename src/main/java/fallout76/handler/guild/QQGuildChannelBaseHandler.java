package fallout76.handler.guild;

import fallout76.handler.QQGuildBaseHandler;

public interface QQGuildChannelBaseHandler extends QQGuildBaseHandler {
    @Override
    default String getSubType() {
        return "channel";
    }
}
