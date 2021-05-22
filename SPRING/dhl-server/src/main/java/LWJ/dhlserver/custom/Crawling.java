package LWJ.dhlserver.custom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawling {

    private List<String> urls;
    private List<Document> documents = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public Crawling(List<String> urls) throws IOException {
        this.urls = urls;
        this.documents.clear();
        for (String url: urls) {
            this.documents.add(Jsoup.connect(url).get());
        }
    }

    public void newCrawling(List<String> urls) throws IOException {
        this.urls = urls;
        this.documents.clear();
        for (String url: urls) {
            this.documents.add(Jsoup.connect(url).get());
        }
    }

}
