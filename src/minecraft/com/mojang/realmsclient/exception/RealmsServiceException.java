package com.mojang.realmsclient.exception;

import com.mojang.realmsclient.client.RealmsError;

public class RealmsServiceException extends Exception {
    public final RealmsError realmsError;

    public RealmsServiceException(RealmsError pRealmsError) {
        this.realmsError = pRealmsError;
    }

    @Override
    public String getMessage() {
        return this.realmsError.logMessage();
    }
}