package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.HistoryRepository;
import LWJ.dhlserver.repository.MemoryHistoryRepository720;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService720 implements HistoryService {

    private String winningUrl = "https://dhlottery.co.kr/gameResult.do?method=win720&Round=";
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
    public HistoryService720(MemoryHistoryRepository720 historyRepository) throws IOException {
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
        return Long.valueOf(crawling.getDocuments().get(0).getElementById("drwNo720").text());
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
        String winningMoneyStr = crawling.getDocuments().get(0)
                .getElementById("number720add").select("div.win_num_wrap dl.win_money")
                .get(0).select("dd span").text();
        String bonusMoneyStr = crawling.getDocuments().get(0)
                .getElementById("number720add").select("div.win_num_wrap dl.win_money")
                .get(2).select("dd span").text();

        nextWinning.setRound(nextRound);
        nextWinning.setNumbers(null);

        nextInfo.setWinning(nextWinning);
        nextInfo.setRecommend(getRecommendNumbers(null)); // 기본 추천
        nextInfo.setWinningMoneyTotalStr(winningMoneyStr);
        nextInfo.setWinningMoneyGameStr(bonusMoneyStr);
        return nextInfo;
    }

    @Override
    public List<List<Long>> getRecommendNumbers(List<Long> numbers) {
        List<List<Long>> findNumbers = findNumbersCommon(numbers);
        List<Long> recommendNumbers = new ArrayList<>();
        List<List<Long>> recommendNumbersList = new ArrayList<>();
        List<Long> finishNumber = new ArrayList<>();
        Long[][] count = new Long[7][10];
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 9; j++){
                count[i][j] = 0L;
            }
        }

        for (List<Long> nums: findNumbers) {
            System.out.println(nums);
            for (int i = 0; i <= 6; i++) {
                count[i][nums.get(i).intValue()]++;
            }
        }

        for (int i = 1; i <= 6; i++){
            Long max = -1L;
            int max_num = -1;
            for (int j = 0; j <= 9; j++){
                if (max < count[i][j]){
                    max = count[i][j];
                    max_num = j;
                }
            }
            recommendNumbers.add((long)max_num);
        }
        recommendNumbersList.add(recommendNumbers);

        return recommendNumbersList;
    }

    private Information crawlOneRound(Long round) throws IOException {
        Information information = new Information();
        Crawling crawlWinning = new Crawling(List.of(winningUrl + round.toString()));
        Document documentWinning = crawlWinning.getDocuments().get(0);

        ArrayList<Long> numbers = new ArrayList<>();
        ArrayList<Long> numbersBonus = new ArrayList<>();
        System.out.println("crawling " + round + " round...");
        //System.out.println(documentWinning.getElementsByClass("win720_num").get(0));
        //System.out.println(documentWinning.getElementsByClass("win720_num").get(1));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("div.group span span").text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(1).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(2).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(3).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(4).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(5).text()));
        numbers.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(0).select("span span").get(6).text()));
        Winning winning = new Winning();
        winning.setRound((long) round);
        winning.setNumbers(numbers);

        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(1).text()));
        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(2).text()));
        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(3).text()));
        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(4).text()));
        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(5).text()));
        numbersBonus.add(Long.valueOf(documentWinning.getElementsByClass("win720_num").get(1).select("span span").get(6).text()));
        Winning winningBonus = new Winning();
        winningBonus.setRound((long) round);
        winningBonus.setNumbers(numbersBonus);

        String winningMoneyStr = crawling.getDocuments().get(0)
                .getElementById("number720add").select("div.win_num_wrap dl.win_money")
                .get(0).select("dd span").text();
        String bonusMoneyStr = crawling.getDocuments().get(0)
                .getElementById("number720add").select("div.win_num_wrap dl.win_money")
                .get(2).select("dd span").text();

        /*String totalMoney = documentWinning.getElementsByClass("tbl_data tbl_data_col").select("tbody tr").get(0).
                getElementsByClass("tar").get(0).text();
        String gameMoney = documentWinning.getElementsByClass("tbl_data tbl_data_col").select("tbody tr").get(0).
                getElementsByClass("tar").get(1).text();*/
        //System.out.println("Money: " + totalMoney + ", " + gameMoney);

        information.setWinning(winning);
        information.setBonusWinning(winningBonus);
        information.setRecommend(null);
        information.setWinningMoneyTotalStr(winningMoneyStr);
        information.setWinningMoneyGameStr(bonusMoneyStr);


        return information;
    }
}
