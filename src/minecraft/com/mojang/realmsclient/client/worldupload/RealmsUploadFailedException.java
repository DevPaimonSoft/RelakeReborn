package com.mojang.realmsclient.client.worldupload;

import net.minecraft.network.chat.Component;

public class RealmsUploadFailedException extends RealmsUploadException {
    private final Component errorMessage;

    public RealmsUploadFailedException(Component pErrorMessage) {
        this.errorMessage = pErrorMessage;
    }

    public RealmsUploadFailedException(String pErrorMessage) {
        this(Component.literal(pErrorMessage));
    }

    @Override
    public Component getStatusMessage() {
        return Component.translatable("mco.upload.failed", this.errorMessage);
    }
}