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
    NUKACODE_MSG_TEMPLATE("""
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
    ;
    private final String msg;
}
