package cyou.wssy001.kookadopter.handler;

import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BasePrivateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description: Kook获取用户ID事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class GetUserIdKookEventHandler implements BasePrivateHandler {
    @Override
    public PlatformEnum platform() {
        return PlatformEnum.Kook;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/获取ID", "/获取id");
    }

    @Override
    public String description() {
        return "获取您在Kook的Open Id，可用于添加Kook管理员列表（指令仅限私聊）";
    }

    @Override
    public void consume(BaseEvent baseEvent) {

    }

//    @Override
//    public Set<String> getKeys() {
//        return Set.of("/获取ID", "/获取id");
//    }
//
//    @Override
//    public String description() {
//        return "获取您在Kook的Open Id，可用于添加Kook管理员列表（指令仅限私聊）";
//    }
//
//    @Override
//    public String handle(String eventKey, KookEvent kookEvent) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("        ");
//        JSONObject author = kookEvent.getJsonObject()
//                .getJSONObject("author");
//        stringBuilder.append(author.getString("username"))
//                .append("#")
//                .append(author.getString("identify_num"))
//                .append("    您好：\\n您在 Kook 的 Open ID 为：")
//                .append(kookEvent.getAuthorId());
//        return String.format(KookReplyMsgTemplateEnum.TEXT_MSG.getMsg(), kookEvent.getAuthorId(), stringBuilder);
//    }
//
//    @Override
//    public void consume(String eventKey, KookEvent kookEvent) {
//
//    }

}
