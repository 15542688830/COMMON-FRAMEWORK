package com.keywordstone.framework.common.basic.utils;

import org.springframework.stereotype.Component;

@Component
public class AsyncThread implements Runnable {

    private String token;
    private String userId;
    private Runnable runnable;

    public AsyncThread(Runnable runnable) {
        this.token = TokenUtils.getToken();
        this.userId = TokenUtils.getUserId();
        this.runnable = runnable;
    }

    @Override
    public void run() {
        TokenUtils.tokenReset(this.token);
        TokenUtils.userIdReset(this.userId);
        if (null != runnable) {
            runnable.run();
        }
    }
}
