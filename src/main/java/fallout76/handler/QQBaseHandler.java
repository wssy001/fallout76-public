package fallout76.handler;

import fallout76.entity.message.QQMessageEvent;

import java.util.List;

public interface QQBaseHandler {
    String getPostType();

    String getMessageType();

    List<String> getKeys();

    String description();

    void execute(QQMessageEvent qqMessageEvent, String key);

    String errorMsgTemplate = """
            {
                "group_id": %d,
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
