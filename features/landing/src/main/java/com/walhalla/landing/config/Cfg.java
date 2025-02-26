package com.walhalla.landing.config;

public class Cfg {

    private final boolean toolbarEnabled;
    private final long counterTimeMs;
    private final boolean splashScreenEnabled;

    public void setCheckConnection(boolean checkConnection) {
        this.checkConnection = checkConnection;
    }

    private boolean checkConnection;
    private final boolean swipeEnabled;
    private final boolean progressEnabled;

    private Cfg(Builder builder) {
        this.toolbarEnabled = builder.toolbarEnabled;
        this.counterTimeMs = builder.counterTimeMs;
        this.splashScreenEnabled = builder.splashScreenEnabled;
        this.checkConnection = builder.checkConnection;
        this.swipeEnabled = builder.swipeEnabled;
        this.progressEnabled = builder.progressEnabled;
    }

    public boolean isToolbarEnabled() {
        return toolbarEnabled;
    }

    public long getCounterTimeMs() {
        return counterTimeMs;
    }

    public boolean isSplashScreenEnabled() {
        return splashScreenEnabled;
    }

    public boolean isCheckConnection() {
        return checkConnection;
    }

    public boolean isSwipeEnabled() {
        return swipeEnabled;
    }

    public boolean isProgressEnabled() {
        return progressEnabled;
    }

    public static class Builder {

        private boolean toolbarEnabled;
        private long counterTimeMs;
        private boolean splashScreenEnabled;
        private boolean checkConnection;
        private boolean swipeEnabled;
        private boolean progressEnabled;

        public Builder() {
            // Установка значений по умолчанию
            this.toolbarEnabled = false;
            this.counterTimeMs = 2400;
            this.splashScreenEnabled = true;
            this.checkConnection = true;
            this.swipeEnabled = false;
            this.progressEnabled = true;
        }

        public Builder setToolbarEnabled(boolean toolbarEnabled) {
            this.toolbarEnabled = toolbarEnabled;
            return this;
        }

        public Builder setCounterTimeMs(long counterTimeMs) {
            this.counterTimeMs = counterTimeMs;
            return this;
        }

        public Builder setSplashScreenEnabled(boolean splashScreenEnabled) {
            this.splashScreenEnabled = splashScreenEnabled;
            return this;
        }

        public Builder setCheckConnection(boolean checkConnection) {
            this.checkConnection = checkConnection;
            return this;
        }

        public Builder setSwipeEnabled(boolean swipeEnabled) {
            this.swipeEnabled = swipeEnabled;
            return this;
        }

        public Builder setProgressEnabled(boolean progressEnabled) {
            this.progressEnabled = progressEnabled;
            return this;
        }

        public Cfg build() {
            return new Cfg(this);
        }
    }
}

