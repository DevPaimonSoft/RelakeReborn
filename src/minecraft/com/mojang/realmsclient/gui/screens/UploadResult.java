package com.mojang.realmsclient.gui.screens;

import javax.annotation.Nullable;

public class UploadResult {
    public final int statusCode;
    @Nullable
    public final String errorMessage;

    UploadResult(int pStatusCode, String pErrorMessage) {
        this.statusCode = pStatusCode;
        this.errorMessage = pErrorMessage;
    }

    @Nullable
    public String getSimplifiedErrorMessage() {
        if (this.statusCode >= 200 && this.statusCode < 300) {
            return null;
        } else {
            return this.statusCode == 400 && this.errorMessage != null ? this.errorMessage : String.valueOf(this.statusCode);
        }
    }

        public static class Builder {
        private int statusCode = -1;
        private String errorMessage;

        public UploadResult.Builder withStatusCode(int pStatusCode) {
            this.statusCode = pStatusCode;
            return this;
        }

        public UploadResult.Builder withErrorMessage(@Nullable String pErrorMessage) {
            this.errorMessage = pErrorMessage;
            return this;
        }

        public UploadResult build() {
            return new UploadResult(this.statusCode, this.errorMessage);
        }
    }
}