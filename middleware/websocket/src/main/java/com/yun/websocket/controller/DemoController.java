package com.yun.websocket.controller;

import com.yun.websocket.config.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * WebSocketController
 *
 * @author wxf
 */
@RestController
public class DemoController {

    @GetMapping("index")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("page")
    public ModelAndView page() {
        return new ModelAndView("websocket");
    }

    /**
     * 单发
     *
     * @param message
     * @param toUserId
     * @return
     * @throws IOException
     */
    @GetMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    /**
     * 群发
     *
     * @param message
     * @return
     * @throws IOException
     */
    @GetMapping("/pushs")
    public ResponseEntity<String> pushToWebs(String message) throws IOException {
        WebSocketServer.sendInfos(message, null);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
