package com.mojang.realmsclient.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;

public class RealmsDefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private final Logger logger;

    public RealmsDefaultUncaughtExceptionHandler(Logger pLogger) {
        this.logger = pLogger;
    }

    @Override
    public void uncaughtException(Thread pThread, Throwable pThrowable) {
        this.logger.error("Caught previously unhandled exception", pThrowable);
    }
}