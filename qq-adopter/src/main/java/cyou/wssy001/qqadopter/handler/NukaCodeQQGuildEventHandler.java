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
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.common.util.PathUtil;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;

/**
 * @Description: QQ频道核弹密码事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NukaCodeQQGuildEventHandler implements BaseHandler {
    private final PhotoService photoService;
    private final NukaCodeService nukaCodeService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ_GUILD;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/核弹密码");
    }

    @Override
    public String getDescription() {
        return "获取目前最新的核弹密码";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQChannelEventDTO qqChannelEventDTO) {
            String guildId = qqChannelEventDTO.getGuildId();
            String channelId = qqChannelEventDTO.getChannelId();
            NukaCode nukaCode = nukaCodeService.getNukaCode();

            String format;
            String replyMsg;
            if (nukaCode == null) {
                log.error("******NukaCodeQQGuildEventHandler.consume：nukaCode获取失败");
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "nukaCode获取失败，请联系管理员");
            } else {
                File file = new File(PathUtil.getJarPath() + "/config/nukaCode.png");
                if (file.exists()) {
                    format = String.format(QQReplyMsgTemplateEnum.NUKA_CODE_PHOTO_MSG_TEMPLATE.getMsg(), file.toURI());
                } else {
                    Thread.ofVirtual()
                            .start(() -> photoService.createNukaCodePhoto("nukaCode.png", nukaCode));
                    String expireTime = LocalDateTimeUtil.format(nukaCode.getExpireTime(), "yyyy年MM月dd日 HH点mm分");
                    format = String.format(QQReplyMsgTemplateEnum.NUKA_CODE_MSG_TEMPLATE.getMsg(), nukaCode.getAlpha(), nukaCode.getBravo(), nukaCode.getCharlie(), expireTime);
                }
            }

            replyMsg = String.format(QQReplyMsgTemplateEnum.GUILD_TEXT_MSG.getMsg(), guildId, channelId, format);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_guild_channel_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
