package LWJ.dhlserver.custom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Crawling {

    private Document web;
    private Document mobile;

    public Crawling() throws IOException {
        this.web = Jsoup.connect("https://dhlottery.co.kr/common.do?method=main").get();
        this.mobile = Jsoup.connect("https://m.dhlottery.co.kr/common.do?method=main").get();
    }

    public Document getWeb() {
        return web;
    }

    public Document getMobile() {
        return mobile;
    }
}
