package LWJ.dhlserver.custom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Crawling {

    private Document web;
    private Document mobile;

    public Crawling(Document web, Document mobile) {
        this.web = web;
        this.mobile = mobile;
    }

    public Document getWeb() {
        return web;
    }

    public Document getMobile() {
        return mobile;
    }
}
