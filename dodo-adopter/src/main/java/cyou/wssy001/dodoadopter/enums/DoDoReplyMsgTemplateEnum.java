package cyou.wssy001.dodoadopter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: DoDo消息回复模板枚举类
 * @Author: Tyler
 * @Date: 2023/3/16 11:07
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum DoDoReplyMsgTemplateEnum {

    CHANNEL_CARD_MSG("""
            {
                 "channelId": "%s",
                 "messageType": 6,
                 "messageBody": %s
            }
            """),
    PRIVATE_TEXT_MSG("""
            {
                 "islandSourceId": "%s",
                 "dodoSourceId": "%s",
                 "messageType": 1,
                 "messageBody": %s
            }
            """),
    TEXT_MSG_TEMPLATE("""
            {
                "content": "%s"
            }
            """),
    ERROR_MSG_TEMPLATE("""
            {
              "card": {
                "type": "card",
                "components": [
                  {
                    "type": "section",
                    "text": {
                      "type": "plain-text",
                      "content": "%s"
                    }
                  },
                  { "type": "divider" },
                  {
                    "type": "remark",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                      },
                      {
                        "type": "dodo-md",
                        "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                      }
                    ]
                  }
                ],
                "theme": "default",
                "title": "%s"
              }
            }
            """),
    HELP_MSG_TEMPLATE("""
            {
              "card": {
                "type": "card",
                "components": [
                  {
                    "type": "section",
                    "text": {
                      "type": "dodo-md",
                      "content": "%s"
                    }
                  },
                  { "type": "divider" },
                  {
                    "type": "remark",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                      },
                      {
                        "type": "dodo-md",
                        "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                      }
                    ]
                  }
                ],
                "theme": "default",
                "title": "%s"
              }
            }
            """),
    NUKA_CODE_CARD("""
            {
               "card": {
                 "type": "card",
                 "components": [
                   {
                     "type": "image-group",
                     "elements": [
                       {
                         "type": "image",
                         "src": "https://img.imdodo.com/openapitest/upload/cdn/F44A194EE68AC2EC565F23B59A48B588_1681608046894.png"
                       }
                     ]
                   },
                   {
                     "type": "section",
                     "text": {
                       "type": "paragraph",
                       "cols": 3,
                       "fields": [
                         { "type": "dodo-md", "content": "\\n\\n" },
                         { "type": "dodo-md", "content": "A点\\nB点\\nC点" },
                         { "type": "dodo-md", "content": "%s\\n%s\\n%s" }
                       ]
                     }
                   },
                   {
                     "type": "countdown",
                     "title": "过期时间",
                     "style": "day",
                     "endTime": %s
                   },
                   { "type": "divider" },
                   {
                     "type": "remark",
                     "elements": [
                       {
                         "type": "image",
                         "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                       },
                       {
                         "type": "dodo-md",
                         "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                       }
                     ]
                   }
                 ],
                 "theme": "default",
                 "title": "%s"
               }
            }
            """),

    SEASON_CALENDAR_CARD("""
            {
              "card": {
                "type": "card",
                "components": [
                  {
                    "type": "image-group",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  { "type": "divider" },
                  {
                    "type": "remark",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                      },
                      {
                        "type": "dodo-md",
                        "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                      }
                    ]
                  }
                ],
                "theme": "default",
                "title": "社区日程表"
              }
            }
            """),
    GOLD_VENDOR_CARD("""
            {
              "card": {
                "type": "card",
                "components": [
                  {
                    "type": "image-group",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  { "type": "divider" },
                  {
                    "type": "remark",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                      },
                      {
                        "type": "dodo-md",
                        "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                      }
                    ]
                  }
                ],
                "theme": "default",
                "title": "米诺瓦日程表"
              }
            }
            """),
    PITT_CARD("""
            {
              "card": {
                "type": "card",
                "components": [
                  {
                    "type": "image-group",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  { "type": "divider" },
                  {
                    "type": "remark",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.imdodo.com/upload/cdn/9DB1149051A02E7E25A690C8D7C11BBF_1671671945280.png?x-oss-process=image/resize,m_lfit,h_80,w_80/format,jpeg"
                      },
                      {
                        "type": "dodo-md",
                        "content": "[辐射76小助手](https://imdodo.com/bot/u/3246195)"
                      }
                    ]
                  }
                ],
                "theme": "default",
                "title": "远征匹兹堡奖励清单"
              }
            }
            """),
    ;

    private final String msg;
}
