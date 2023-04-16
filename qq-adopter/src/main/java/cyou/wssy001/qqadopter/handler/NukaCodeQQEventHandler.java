package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.NukaCodeService;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description: QQ核弹密码事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NukaCodeQQEventHandler implements BaseHandler {
    private final NukaCodeService nukaCodeService;


    @Override
    public PlatformEnum platform() {
        return PlatformEnum.QQ;
    }

    @Override
    public EventEnum eventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/核弹密码");
    }

    @Override
    public String description() {
        return "获取目前最新的核弹密码";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            Long groupId = qqEventDTO.getGroupId();
            NukaCode nukaCode = nukaCodeService.getNukaCode();
            String replyMsg;
            if (nukaCode == null) {
                log.error("******NukaCodeQQEventHandler.consume：nukaCode获取失败");
                String format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "nukaCode获取失败，请联系管理员");
                replyMsg = String.format(QQReplyMsgTemplateEnum.GROUP_TEXT_MSG.getMsg(), groupId, format);
            } else {
                String expireTime = LocalDateTimeUtil.format(nukaCode.getExpireTime(), "yyyy年MM月dd日 HH点mm分");
                String format = String.format(QQReplyMsgTemplateEnum.NUKACODE_MSG_TEMPLATE.getMsg(), nukaCode.getAlpha(), nukaCode.getBravo(), nukaCode.getCharlie(), expireTime);
                replyMsg = String.format(QQReplyMsgTemplateEnum.GROUP_TEXT_MSG.getMsg(), groupId, format);
            }

            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_group_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
