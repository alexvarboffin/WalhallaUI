package tk.alexapp.freestuffandcoupons.domain;

import java.io.Serializable;

public class ListItemInfo implements Serializable {

    private String title;
    private String thumbUrl;
    private String link;

    public ListItemInfo() {}

    public ListItemInfo(String title, String thumbUrl, String link) {
        this.title = title;
        this.thumbUrl = thumbUrl;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ListItemInfo{" +
                "title='" + title + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
