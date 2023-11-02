package com.yandex.metrica;

public class YandexMetricaConfig {
    public static Builder newConfigBuilder(String app_metrica_key) {
        return new Builder();
    }

    public static class Builder {
        public YandexMetricaConfig build() {
            return new YandexMetricaConfig();
        }
    }
}
