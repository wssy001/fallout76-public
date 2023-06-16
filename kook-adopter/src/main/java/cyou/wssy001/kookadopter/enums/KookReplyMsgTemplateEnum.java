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
                "theme": "primary",
                "size": "sm",
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
                "theme": "info",
                "size": "sm",
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
                        "src": "https://img.kookapp.cn/attachments/2023-04/14/QVqh6zbPCZ1hc0tw.png"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "\\t    (font)A点：    (font)[success]\\t\\t%s\\n\\t    (font)B点：    (font)[success]\\t\\t%s\\n\\t    (font)C点：    (font)[success]\\t\\t%s\\n"
                    }
                  },
                  {
                    "type": "countdown",
                    "mode": "day",
                    "endTime": %s
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
                "theme": "primary",
                "size": "sm",
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

    PITT_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "远征匹兹堡奖励清单"
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
                        "src": "%s"
                      }
                    ]
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
    GOLD_VENDOR_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "米诺瓦日程表"
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
                        "src": "%s"
                      }
                    ]
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
    SEASON_CALENDAR_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "社区日程表"
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
                        "src": "%s"
                      }
                    ]
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
    BOBBLE_HEAD_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "娃娃效果图"
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
                        "src": "%s"
                      }
                    ]
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
    MOTH_MAN_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "天蛾人春分季节性攻略指南"
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
                        "src": "%s"
                      }
                    ]
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
    DAILY_OPS_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "日常行动独特奖励速览"
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
                        "src": "%s"
                      }
                    ]
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
    TREASURE_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "挖宝奖励图"
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
                        "src": "%s"
                      }
                    ]
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
    COOKBOOK_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "常见食物图"
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
                        "src": "%s"
                      },
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
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
    WEEKLY_OFFERS_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "原子商店特惠预览"
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
                        "src": "%s"
                      }
                    ]
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
    TREASURE_HUNTER_OFFERS_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "鼹鼠旷工寻宝猎人奖励清单"
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
                        "src": "%s"
                      }
                    ]
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
    EXPERIENCE_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "堆智力"
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
                        "src": "%s"
                      }
                    ]
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
    BLUE_MOON_CARD("""
            [
              {
                "type": "card",
                "theme": "primary",
                "size": "sm",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "蓝月当空主题奖励清单"
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
                        "src": "%s"
                      }
                    ]
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
