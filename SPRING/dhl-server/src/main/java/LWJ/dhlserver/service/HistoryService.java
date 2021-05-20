package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.HistoryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private Crawling crawling;
    private List<Document> winningHistory;
    private Long lastRound;

    public Crawling getCrawling() {
        return crawling;
    }

    @Autowired
    public HistoryService(HistoryRepository historyRepository) throws IOException {
        this.historyRepository = historyRepository;
        crawling();
    }

    public HistoryRepository getHistoryRepository() {
        return historyRepository;
    }

    public Crawling crawling() throws IOException {
        Document crawlWeb = Jsoup.connect("https://dhlottery.co.kr/common.do?method=main").get();
        Document crawlMobile = Jsoup.connect("https://m.dhlottery.co.kr/common.do?method=main").get();
        this.crawling = new Crawling(crawlWeb, crawlMobile);

        if (historyRepository.getDataSize() == 0) {
            initHistory(crawling);
        } else {
            System.out.println("already crawl history");
        }

        return crawling;
    }

    private boolean initHistory(Crawling crawling) throws IOException {
        /*
        * History에 넣는걸로 바꾸고, controller 에서도 crawl에 포함하지말고, crawlHistory 따로 만들던가 할 것.
        * 서버 부팅 후 처음에 하기는 하지만, 켜질 때 말고, 특정 시점에 하도록? 주소 접근하면?
        * 켜질 때 처음에 하는게 낫나?
        * */
        String winningUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";

        //for (int i = 1; i <= getLastRound(); i++) {
        for (int i = 1; i <= 50; i++) {
            Winning winning = crawlOneRound((long)i);
            historyRepository.add(winning);
            //winningHistory.add(winning);
        }
        return true;
    }

    public Winning crawlOneRound(Long round) throws IOException {
        String winningUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";

        Document crawlWinning = Jsoup.connect(winningUrl + String.valueOf(round)).get();
        ArrayList<Long> numbers = new ArrayList<>();
        System.out.println("crawling " + round + " round...");
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(0).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(1).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(2).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(3).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(4).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num win").select("p span").get(5).text()));
        numbers.add(Long.valueOf(crawlWinning.getElementsByClass("num bonus").select("p span").get(0).text()));
        Winning winning = new Winning();
        winning.setRound((long) round);
        winning.setNumbers(numbers);
        return winning;
    }

    public Long getLastRound() {
        return Long.valueOf(crawling.getWeb().getElementById("lottoDrwNo").text());
    }
    public Winning getLastWinning() {
        Winning winning = new Winning();
        winning.setRound(getLastRound());
        winning.setNumbers(findRoundNumbers(winning.getRound()));
        return winning;
    }

    public boolean save(Winning winning) {
        historyRepository.add(winning);
        return true;
    }

    public List<Long> findRoundNumbers(Long round) {
        return historyRepository.findNumbersByRound(round).get();
    }
    public List<List<Long>> findWinningNumbers(Long findNumber) {
        //for (Long findNumber: findNumbers) {
        return historyRepository.findNumbersByNumber(findNumber).get();
        //}
        //return historyRepository.findNumbersByRound(round).get();
    }
    public List<List<Long>> findAllNumbers() {
        return historyRepository.findAll();
    }
    public List<List<Long>> findNumbersCommon(List<Long> numbers) {
        List<List<Long>> numbersList = findAllNumbers();
        if (numbers == null) return numbersList;
        for (Long number: numbers) {
            List<List<Long>> result = numbersList.stream().filter(numList -> numList.contains(number)).collect(Collectors.toList());
            numbersList = result;
        }
        return numbersList;
    }

    public List<List<Long>> recommendNumbersList(List<Long> numbers) {
        List<List<Long>> findNumbers = findNumbersCommon(numbers);
        List<Long> recommendNumbers = new ArrayList<>();
        List<List<Long>> recommendNumbersList = new ArrayList<>();
        List<Long> finishNumber = new ArrayList<>();
        Long[] count = new Long[50];
        for (int i = 1; i <= 45; i++) {
            count[i] = 0L;
        }

        for (List<Long> nums: findNumbers) {
            for (Long n: nums) {
                count[Math.toIntExact(n)]++;
            }
        }
        if (numbers == null) numbers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Long max = -1L;
            Long max_number = 0L;
            for (Long num: numbers) {
                recommendNumbers.add(num);
            }
            for (int j = 1; j <= 45; j++) {
                if (max < count[j] && !recommendNumbers.contains((long)j) && !finishNumber.contains((long)j)) {
                    max = count[j];
                    max_number = (long)j;
                }
            }
            System.out.println(max_number);
            finishNumber.add(max_number);
            recommendNumbers.add(max_number);
            recommendNumbers.sort(Long::compareTo);
            System.out.println(recommendNumbers);
            recommendNumbersList.add(recommendNumbers(recommendNumbers));
            recommendNumbers.clear();

        }

        return recommendNumbersList;
    }

    private List<Long> recommendNumbers(List<Long> numbers) {
        List<List<Long>> findNumbers = findNumbersCommon(numbers);
        List<Long> recommendNumbers = new ArrayList<>();
        Long[] count = new Long[50];
        for (int i = 1; i <= 45; i++) {
            count[i] = 0L;
        }

        for (List<Long> nums: findNumbers) {
            for (Long n: nums) {
                count[Math.toIntExact(n)]++;
            }
        }

        for (int i = 0; i < 6; i++) {
            Long max = -1L;
            Long max_number = 0L;
            for (int j = 1; j <= 45; j++) {
                if (max < count[j] && !recommendNumbers.contains((long)j)) {
                    max = count[j];
                    max_number = (long)j;
                }
            }
            recommendNumbers.add(max_number);
        }
        recommendNumbers.sort(Long::compareTo);

        return recommendNumbers;
    }
}
