package cyou.wssy001.kookadopter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: Kook消息回复模板枚举类
 * @Author: Tyler
 * @Date: 2023/3/16 11:07
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum KookReplyMsgTemplateEnum {

    // 卡片消息结构体
    CARD_MSG("""
            {
                "type": 10,
                "target_id": "%s",
                "content": "%s"
            }
            """),

    // 文本消息结构体
    TEXT_MSG("""
            {
                "type": 1,
                "target_id": "%s",
                "content": "%s"
            }
            """),

    // 错误消息结构体
    ERROR_MSG("""
            {
                "type": 10,
                "target_id": "%s",
                "content": "%s"
            }
            """),

    // 卡片结构（错误消息）
    ERROR_MSG_CARD("""
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
            """),

    // 卡片结构（核弹密码消息）
    NUKA_CODE_CARD("""
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
                      "content": "核弹密码"
                    }
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://s1.ax1x.com/2022/11/22/z1p5Jf.png"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "\\tA点：\\t\\t%s\\n\\tB点：\\t\\t%s\\n\\tC点：\\t\\t%s\\n"
                    }
                  },
                  {
                    "type": "countdown",
                    "mode": "day",
                    "endTime": %d
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
            """),

    // 卡片结构（帮助消息）
    HELP_MSG_CARD("""
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
                      "content": "指令列表"
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
            """),
    ;

    private final String msg;
}
