package org.apache.mvp.presenter;

public class DeviceCheck {

    private final boolean checkEmulator;
    private final boolean rooted;
    private final boolean packageSignature;

    private DeviceCheck(Builder builder) {
        this.checkEmulator = builder.checkEmulator;
        this.rooted = builder.rooted;
        this.packageSignature = builder.signature;

    }

    public boolean isCheckEmulator() {
        return checkEmulator;
    }

    public boolean isPackageSignature() {
        return packageSignature;
    }


    public boolean isCheckRoot() {
        return rooted;
    }

    public static class Builder {
        private boolean checkEmulator;
        private boolean rooted;
        private boolean signature;

        public Builder setCheckEmulator(boolean checkEmulator) {
            this.checkEmulator = checkEmulator;
            return this;
        }

        public Builder setRooted(boolean rooted) {
            this.rooted = rooted;
            return this;
        }

        public Builder setSignature(boolean signature) {
            this.signature = signature;
            return this;
        }

        public DeviceCheck build() {
            return new DeviceCheck(this);
        }
    }

    // Другие методы класса
}
