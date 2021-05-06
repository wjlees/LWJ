package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.HistoryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private Crawling crawling;
    private List<Document> winningHistory;
    private Long lastRound;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) throws IOException {
        this.historyRepository = historyRepository;
        crawling();
    }

    public Crawling crawling() throws IOException {
        Document crawlWeb = Jsoup.connect("https://dhlottery.co.kr/common.do?method=main").get();
        Document crawlMobile = Jsoup.connect("https://m.dhlottery.co.kr/common.do?method=main").get();
        crawling = new Crawling(crawlWeb, crawlMobile);

        if (historyRepository.getDataSize() == 0) {
            getHistory(crawling);
        } else {
            System.out.println("already crawl history");
        }

        return crawling;
    }

    private boolean getHistory(Crawling crawling) throws IOException {
        /*
        * History에 넣는걸로 바꾸고, controller 에서도 crawl에 포함하지말고, crawlHistory 따로 만들던가 할 것.
        * 서버 부팅 후 처음에 하기는 하지만, 켜질 때 말고, 특정 시점에 하도록? 주소 접근하면?
        * 켜질 때 처음에 하는게 낫나?
        * */
        String winningUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";

        for (int i = 1; i <= getLastRound(crawling); i++) {
            Document crawlWinning = Jsoup.connect(winningUrl + String.valueOf(i)).get();
            ArrayList<Long> numbers = new ArrayList<>();
            System.out.println("crawling " + i + " round...");
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(0).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(1).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(2).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(3).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(4).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(5).text()));
            numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num bonus").select("p span").get(0).text()));
            Winning winning = new Winning();
            winning.setRound((long) i);
            winning.setNumbers(numbers);

            historyRepository.add(winning);
            //winningHistory.add(winning);
        }
        return true;
    }

    public Long getLastRound(Crawling crawling) {
        return Long.valueOf(crawling.getWeb().getElementById("lottoDrwNo").text());
    }

    public boolean save(Winning winning) {
        historyRepository.add(winning);
        return true;
    }

    public List<Long> findRoundNumbers(Long round) {
        return historyRepository.findNumbersByRound(round).get();
    }
}
