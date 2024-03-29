package cyou.wssy001.qqadopter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: QQ消息回复模板枚举类
 * @Author: Tyler
 * @Date: 2023/3/16 11:07
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum QQReplyMsgTemplateEnum {

    // 文本消息结构体
    GROUP_TEXT_MSG("""
            {
                "group_id": %d,
                "message": %s
            }
            """),
    GUILD_TEXT_MSG("""
            {
                "guild_id":"%s",
                "channel_id": "%s",
                "message": %s
            }
            """),

    PRIVATE_TEXT_MSG("""
            {
                "user_id": %d,
                "message": %s
            }
            """),

    TEXT_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "%s"
                    }
                }
            ]
            """),
    NUKA_CODE_MSG_TEMPLATE("""
            [
                {
                    "type": "image",
                    "data": {
                        "file": "https://s1.ax1x.com/2022/11/22/z1p5Jf.png"
                    }
                },
                {
                    "type": "text",
                    "data": {
                        "text": "\\t\\tA点：\\t\\t%s\\n"
                    }
                },
                {
                    "type": "text",
                    "data": {
                        "text": "\\t\\tB点：\\t\\t%s\\n"
                    }
                },
                {
                    "type": "text",
                    "data": {
                        "text": "\\t\\tC点：\\t\\t%s\\n"
                    }
                },
                {
                    "type": "text",
                    "data": {
                        "text": "过期时间（北京时间）：%s"
                    }
                }
            ]
            """),
    NUKA_CODE_PHOTO_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "核弹密码"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    HELP_PHOTO_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "帮助"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    GOLD_VENDOR_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "米诺瓦日程表"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    PITT_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "远征匹兹堡奖励清单"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    SEASON_CALENDAR_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "社区日程表"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    BOBBLE_HEAD_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "娃娃效果图"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    MOTH_MAN_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "天蛾人春分季节性攻略指南"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    DAILY_OPS_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "日常行动独特奖励速览"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    TREASURE_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "挖宝奖励图"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    COOKBOOK_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "常见食物图"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    WEEKLY_OFFERS_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "原子商店特惠预览"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    TREASURE_HUNTER_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "鼹鼠旷工寻宝猎人奖励清单"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    EXPERIENCE_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "堆智力"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    BLUE_MOON_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "蓝月当空主题奖励清单"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    COAST_REWARDS_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "科斯塔的业务系列任务流程和奖励图"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    INVADERS_FROM_BEYOND_MSG_TEMPLATE("""
            [
                {
                    "type": "text",
                    "data": {
                        "text": "外来入侵者事件攻略指南图"
                    }
                },
                {
                    "type": "image",
                    "data": {
                        "file": "%s"
                    }
                }
            ]
            """),
    ;
    private final String msg;
}
