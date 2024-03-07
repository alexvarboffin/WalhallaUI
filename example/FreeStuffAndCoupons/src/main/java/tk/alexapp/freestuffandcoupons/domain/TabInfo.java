package tk.alexapp.freestuffandcoupons.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TabInfo implements Serializable {

    private List<ListItemInfo> items = new LinkedList<>();

    public List<ListItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ListItemInfo> items) {
        this.items = items;
    }

    public void addItemInfo(ListItemInfo itemInfo) {
        items.add(itemInfo);
    }

    public ListItemInfo getItemInfo(int number) {
        return getItems().get(number);
    }

    public ListItemInfo[] getItemsAsArray() {
        return getItems().toArray(new ListItemInfo[getItems().size()]);
    }
    @Override
    public String toString() {
        return "TabInfo{" +
                "items=" + items +
                '}';
    }
}
