package fallout76.handler;

public interface QQGuildBaseHandler extends QQBaseHandler {
    @Override
    default String getPostType() {
        return "message";
    }

    @Override
    default String getMessageType() {
        return "guild";
    }

    String getSubType();

    String errorMsgTemplate = """
            {
                "guild_id": %s,
                "channel_id": %s,
                "message": [
                    {
                        "type": "text",
                        "data": {
                            "text": "\n%s\n"
                        }
                    }
                ]
            }
            """;
}
