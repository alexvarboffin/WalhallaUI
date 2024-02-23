package org.apache.cordova.domen;

import org.apache.cordova.ScreenType;

// Класс для пользовательских интерфейсов
public class UIVisibleDataset {

    private final Dataset dataset;

    public UIVisibleDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public UIVisibleDataset() {
        this.dataset = new Dataset();
    }

    public UIVisibleDataset(ScreenType type, String url) {
        this.dataset = new Dataset();
        this.dataset.screenType = type;
        this.dataset.setUrl(url);
    }

    public UIVisibleDataset(Boolean isEnabled, String successUrl, Boolean isDemo, String whitelist) {
        this.dataset = new Dataset();
        this.dataset.enabled = isEnabled;
        this.dataset.setUrl(successUrl);
        this.dataset.setDemo(isDemo);
        this.dataset.setWhitelist(whitelist);
    }


    // Остальные методы для работы с UI...

    // Внутренний класс Dataset доступен только внутри UIVisibleDataset
//    private class Dataset {
//        // Поля и методы класса Dataset...
//    }

    // Методы для доступа к полям класса Dataset через прослойку

    public ScreenType getScreenType() {
        return dataset.screenType;
    }

    public void setScreenType(ScreenType screenType) {
        dataset.screenType = screenType;
    }

    public Boolean getEnabled() {
        return dataset.enabled;
    }

    public void setEnabled(Boolean enabled) {
        dataset.enabled = enabled;
    }

    public Integer getOrientation() {
        return dataset.orientation;
    }

    public void setOrientation(Integer orientation) {
        dataset.orientation = orientation;
    }

    public boolean isWebview_external() {
        return dataset.webview_external;
    }

    public void setWebview_external(boolean webview_external) {
        dataset.webview_external = webview_external;
    }

    public String getViewType() {
        return dataset.viewType;
    }

    public void setViewType(String viewType) {
        dataset.viewType = viewType;
    }

    public boolean isDemo() {
        return dataset.isDemo();
    }

    public void setDemo(boolean demo) {
        dataset.setDemo(demo);
    }

    public String getWhitelist() {
        return dataset.getWhitelist();
    }

//    public void setWhitelist(String whitelist) {
//        dataset.set = whitelist;
//    }

    public String getUrl() {
        return dataset.getUrl();
    }

    public void setUrl(String url) {
        dataset.setUrl(url);
    }

    public boolean isEnabled() {
        return dataset.isEnabled();
    }

    // Дополнительные методы, если необходимо
}