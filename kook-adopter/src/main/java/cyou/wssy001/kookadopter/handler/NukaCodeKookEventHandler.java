package cyou.wssy001.kookadopter.handler;

import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.NukaCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description: Kook核弹密码事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NukaCodeKookEventHandler implements BaseHandler {
    private final NukaCodeService nukaCodeService;


    @Override
    public PlatformEnum platform() {
        return PlatformEnum.Kook;
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
    public void consume(BaseEvent baseEvent) {

    }


//
//    @Override
//    public String handle(String eventKey, KookEvent kookEvent) {
//        String targetId = kookEvent.getTargetId();
//        NukaCode nukaCode = nukaCodeService.getNukaCode();
//        if (nukaCode == null) {
//            log.error("******NukaCodeKookEventHandler.handle：nukaCode获取失败");
//            String format = String.format(KookReplyMsgTemplateEnum.ERROR_MSG_CARD.getMsg(), "nukaCode获取失败，请联系管理员");
//            return String.format(KookReplyMsgTemplateEnum.ERROR_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
//        }
//
//        String format = String.format(KookReplyMsgTemplateEnum.NUKA_CODE_CARD.getMsg(), nukaCode.getAlpha(), nukaCode.getBravo(), nukaCode.getCharlie(), nukaCode.getExpireTime().getTime());
//        return String.format(KookReplyMsgTemplateEnum.CARD_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
//    }
//
//    @Override
//    public void consume(String eventKey, KookEvent kookEvent) {
//
//    }

}
