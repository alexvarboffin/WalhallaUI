package tk.alexapp.freestuffandcoupons.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TabsInfo {

    private Map<Integer, TabInfo> tabsInfo = new ConcurrentHashMap<>();

    public void addTabInfo(int number, TabInfo tabInfo) {
        tabsInfo.put(number, tabInfo);
    }

    public TabInfo getTabInfo(int number) {
        return tabsInfo.get(number);
    }

    @Override
    public String toString() {
        return "TabsInfo{" +
                "tabsInfo=" + tabsInfo +
                '}';
    }
}
