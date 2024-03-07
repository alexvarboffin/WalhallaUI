package tk.alexapp.freestuffandcoupons.task;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import tk.alexapp.freestuffandcoupons.domain.ListItemInfo;
import tk.alexapp.freestuffandcoupons.domain.TabInfo;
import tk.alexapp.freestuffandcoupons.domain.TabsInfo;

public class DownloadMainXmlTask extends AsyncTask<String, Void, TabsInfo> {

    private static final String TAG = "DownloadMainXmlTask";
    private ResultListener<TabsInfo> listener;

    public DownloadMainXmlTask(ResultListener<TabsInfo> listener) {
        this.listener = listener;
    }

    @Override
    protected TabsInfo doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            TabsInfo tabsInfo = parseXml(is);
            Log.v(TAG, tabsInfo.toString());
            return tabsInfo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tryCloseConnection(urlConnection, is);
        }
        return null;
    }

    private void tryCloseConnection(HttpURLConnection urlConnection, InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

    private TabsInfo parseXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        TabsInfo tabsInfo = new TabsInfo();
        for (int tabsCount = 1; tabsCount <= 3; tabsCount++) {
            NodeList nList = doc.getElementsByTagName("tab" + tabsCount);
            TabInfo tabInfo = new TabInfo();
            for (int itemsCount = 0; itemsCount < nList.getLength(); itemsCount++) {
                Node nNode = nList.item(itemsCount);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    initTabInfo(tabInfo, (Element) nNode);
                }
            }
            tabsInfo.addTabInfo(tabsCount - 1, tabInfo);
        }
        return tabsInfo;
    }

    private void initTabInfo(TabInfo tabInfo, Element nNode) {
        Element eElement = nNode;
        Log.v(TAG, "id: " + eElement.getAttribute("id"));
        String title = eElement.getElementsByTagName("title").item(0).getTextContent();
        Log.v(TAG, "title: " + title);
        String link = eElement.getElementsByTagName("link").item(0).getTextContent();
        Log.v(TAG, "link: " + link);
        String thumbUrl = eElement.getElementsByTagName("thumb_url").item(0).getTextContent();
        Log.v(TAG, "thumb_url : " + thumbUrl);
        ListItemInfo itemInfo = new ListItemInfo(title, thumbUrl.replaceAll(" ", "%20"), link);
        tabInfo.addItemInfo(itemInfo);
    }

    @Override
    protected void onPostExecute(TabsInfo result) {
        if (result == null) {
            listener.error();
        } else {
            listener.success(result);
        }
    }
}
