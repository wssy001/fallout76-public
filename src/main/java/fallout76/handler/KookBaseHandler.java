package fallout76.handler;

import fallout76.entity.message.KookEvent;

import java.util.List;

public interface KookBaseHandler {

    String channelType();

    List<String> getKeys();

    String description();

    void execute(KookEvent kookEvent, String key);

    String errorMsgTemplate = """
            {
                "type": 10,
                "target_id": %s,
                "content": "%s"
            }
            """;

    String errorMsgCard= """
            [
              {
                "type": "card",
                "theme": "secondary",
                "size": "lg",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "错误"
                    }
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "%s"
                    }
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "context",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.kookapp.cn/assets/2022-06/N6ymk3YYuC0sg0sg.png"
                      },
                      {
                        "type": "kmarkdown",
                        "content": "[辐射76小助手](https://www.kookapp.cn/app/oauth2/authorize?id=11214&permissions=268288&client_id=L1CBDfziwUZWykMC&redirect_uri=&scope=bot)"
                      }
                    ]
                  }
                ]
              }
            ]
            """;
}
