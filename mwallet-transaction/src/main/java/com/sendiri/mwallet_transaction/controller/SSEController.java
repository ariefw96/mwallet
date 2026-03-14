package com.sendiri.mwallet_transaction.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse")
public class SSEController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/listen")
    public SseEmitter streamTransaction(@RequestParam("trxId") String trxId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(trxId, emitter);

        emitter.onCompletion(() -> emitters.remove(trxId));
        emitter.onTimeout(() -> emitters.remove(trxId));

        return emitter;
    }

    public void pushTransactionEvent(String trxId, Object data) {
        SseEmitter emitter = emitters.get(trxId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("transaction-finish")
                        .data(data));
                emitter.complete();
            } catch (IOException e) {
                emitters.remove(trxId);
            }
        }
    }
}