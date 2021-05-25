package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.HistoryRepository;
import LWJ.dhlserver.repository.MemoryHistoryRepository645;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryService645 implements HistoryService {

    private String winningUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";
    private List<String> urls = List.of(
            "https://dhlottery.co.kr/common.do?method=main",
            "https://m.dhlottery.co.kr/common.do?method=main"
    );
    private final HistoryRepository historyRepository;
    private Crawling crawling; // getter
    private List<Document> winningHistory;
    private Long lastRound;

    public Crawling getCrawling() {
        return crawling;
    }

    // constructor
    @Autowired
    public HistoryService645(MemoryHistoryRepository645 historyRepository) throws IOException {
        this.historyRepository = historyRepository;
        this.crawling = new Crawling(urls);
    }

    public HistoryRepository getHistoryRepository() {
        return historyRepository;
    }


    @Override
    public boolean update() throws IOException {
        /*
        * History에 넣는걸로 바꾸고, controller 에서도 crawl에 포함하지말고, crawlHistory 따로 만들던가 할 것.
        * 서버 부팅 후 처음에 하기는 하지만, 켜질 때 말고, 특정 시점에 하도록? 주소 접근하면?
        * 켜질 때 처음에 하는게 낫나?
        * */
        int size = historyRepository.getDataSize().intValue();

        crawling.newCrawling(urls);
        for (int i = size + 1; i <= getDocumentLastRound(crawling); i++) {
            Information information = crawlOneRound((long)i);
            historyRepository.add(information);
        }
        return true;
    }

    @Override
    public Information getOneRound(Long round) throws IOException {
        return historyRepository.findInformationByRound(round);
    }


    private Long getDocumentLastRound(Crawling crawling) {
        return Long.valueOf(crawling.getDocuments().get(0).getElementById("lottoDrwNo").text());
    }

    private List<List<Long>> findNumbersCommon(List<Long> numbers) {
        List<Information> informationList = historyRepository.findAll();
        List<List<Long>> numbersList = new ArrayList<>();
        for (Information info : informationList) {
            numbersList.add(info.getWinning().getNumbers());
        }

        if (numbers == null) return numbersList;
        for (Long number: numbers) {
            List<List<Long>> result = numbersList.stream().filter(numList -> numList.contains(number)).collect(Collectors.toList());
            numbersList = result;
        }
        return numbersList;
    }

    @Override
    public Information getLastInfo() { // repository 에서 가져옴
        // 공식 서버에서 가져올건지, Repository에서 가져올건지 확실하게 정하기.
        // Repository를 Last로 최대한 update 항상 하는게 맞고, 이걸 기반으로 할 것.
        // => Repository에서 가져오자.
        // Money 정보는 Repository에 없는데?
        // 1) update가 안된경우 달라지는거고, 이 경우는 어쨌든 비정상인 경우임. Money만 공식 서버에서 가져오자.
        //    => update가 안된경우 Info 자체를 null 로 넣자.
        //      => 이걸 판단하는 기준은 공식서버 최신 round와 repository의 최신 round가 다르면?
        //      => 아니면 repository가 그냥 비어있으면?
        // 2) Money도 Repository에 매 시간 update 하고 이걸 사용하는 방법.

        // 일단 update는 scheduled 로 무조건 매시간 0분, 1분에 호출할거.
        // 1번이든, 2번이든, 어차피 crawl 매번 새로 받아야 Money 도 update 되는데
        // 어차피 동작 해야하는거라면 Repository에도 매번 update 하자!

        Long repositoryLastRound = historyRepository.getDataSize();

        return historyRepository.findInformationByRound(repositoryLastRound);
    }

    @Override
    public Information getNextInfo() { // next 는 Money, recommend 밖에 가져올게 없음
        // next 는 historyRepository에서 가져올 수가 없음.
        // 공식 서버에서 가져와야 함.
        // RecommendRepository를 만들어야하나
        Information nextInfo = new Information();
        Winning nextWinning = new Winning();

        Long repositoryLastRound = historyRepository.getDataSize();
        Long nextRound = repositoryLastRound + 1L;
        String nextMoneyStr = crawling.getDocuments().get(1).
                select("span.expect strong").text().toString();

        nextWinning.setRound(nextRound);
        nextWinning.setNumbers(null);

        nextInfo.setWinning(nextWinning);
        nextInfo.setRecommend(getRecommendNumbers(null)); // 기본 추천
        nextInfo.setWinningMoneyTotalStr(nextMoneyStr);
        nextInfo.setWinningMoneyGameStr(null);
        return nextInfo;
    }

    @Override
    public List<List<Long>> getRecommendNumbers(List<Long> numbers) {
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


    private Information crawlOneRound(Long round) throws IOException {
        Information information = new Information();
        Crawling crawlWinning = new Crawling(List.of(winningUrl + round.toString()));
        Document documentWinning = crawlWinning.getDocuments().get(0);

        ArrayList<Long> numbers = new ArrayList<>();
        System.out.println("crawling " + round + " round...");
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(0).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(1).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(2).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(3).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(4).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num win").select("p span").get(5).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("num bonus").select("p span").get(0).text()));
        Winning winning = new Winning();
        winning.setRound((long) round);
        winning.setNumbers(numbers);

        String totalMoney = documentWinning.getElementsByClass("tbl_data tbl_data_col").select("tbody tr").get(0).
                getElementsByClass("tar").get(0).text();
        String gameMoney = documentWinning.getElementsByClass("tbl_data tbl_data_col").select("tbody tr").get(0).
                getElementsByClass("tar").get(1).text();
        //System.out.println("Money: " + totalMoney + ", " + gameMoney);

        information.setWinning(winning);
        information.setRecommend(null);
        information.setWinningMoneyTotalStr(totalMoney);
        information.setWinningMoneyGameStr(gameMoney);


        return information;
    }
}
