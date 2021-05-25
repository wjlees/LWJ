package LWJ.dhlserver.controller;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.service.HistoryService645;
import LWJ.dhlserver.service.HistoryService720;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {
    private final HistoryService645 historyService645;
    private final HistoryService720 historyService720;

    @Autowired
    public MainController(HistoryService645 historyService645, HistoryService720 historyService720) {
        this.historyService645 = historyService645;
        this.historyService720 = historyService720;
    }

    @GetMapping("/")
    public String homeControl() {
        return "home";
    }

    @GetMapping("/crawl")
    @ResponseBody
    public Crawling crawling() throws IOException {
        return historyService645.getCrawling(); //????
    }

    @GetMapping("/crawl/web")
    @ResponseBody
    public String crawling_web() throws IOException {
        if (historyService645.getCrawling() == null) {
            crawling();
        }
        return historyService645.getCrawling().getDocuments().get(0).toString();
    }

    @GetMapping("/crawl/mobile")
    @ResponseBody
    public String crawling_mobile() throws IOException {
        if (historyService645.getCrawling() == null) {
            crawling();
        }
        return historyService645.getCrawling().getDocuments().get(1).toString();
    }

    @GetMapping("/update") // crawling + update
    @Scheduled(cron = "0 0,1 * * * *")
    @ResponseBody
    public Long update() throws IOException {
        historyService720.update();
        historyService645.update();

        return 0L;
    }


/*    @GetMapping("/645/numbers") // "/numbers?find=1,2,3,4,5,6,7"
    @ResponseBody
    public List<List<Long>> findNumbersString(@RequestParam(value="find", required = false) List<Long> numbers) {
        return historyService645.findNumbersCommon(numbers);
    }*/
    // numbers는 알 필요 없음
    // 만약 알고싶으면 해당 번호에 분포가 어떻고 그런거일거고,
    // 그러면 recommendService 에서 분포나 개수나 그런거 제공해주면 그걸 가져다가 보여주면 될 것.

    @GetMapping("/{num:[0-9]+}/lastInfo")
    @ResponseBody
    public Information lastInfo(@PathVariable("num") Long num) {
        if (num.equals(645L))
            return historyService645.getLastInfo();
        else if (num.equals(720L))
            return historyService720.getLastInfo();

        return null;
    }

    @GetMapping("/{num:[0-9]+}/nextInfo")
    @ResponseBody
    public Information nowInfo(@PathVariable("num") Long num) {
        if (num.equals(645L))
            return historyService645.getNextInfo();
        if (num.equals(720L))
            return historyService720.getNextInfo();

        return null;
    }

    @GetMapping("/{num:[0-9]+}/recommend") // "/645/recommend?numbers=1,2,3"
    // find 사용하는 용도로 있는 것. 기본 추천은 nextInfo에 있음.
    @ResponseBody
    public List<List<Long>> recommendNumbersString(@PathVariable("num") Long num, @RequestParam(value="numbers", required = false) List<Long> numbers) {
        if (num.equals(645L))
            return historyService645.getRecommendNumbers(numbers);
        if (num.equals(720L))
            return historyService720.getRecommendNumbers(numbers);

        return null;
    }

/*
    @GetMapping("/test")
    @ResponseBody
    public List<Long> winning() {
        return historyService.findRoundNumbers(99L);
    }*/
/*
 * num 1개에서 6개 => 뭔가 한번에 할 수 있는 방법이 있을것같은데....
 * */

/*
    @GetMapping("/test/numbers/")
    @ResponseBody
    public List<List<Long>> findNumbers0() {
        System.out.println("Empty");
        return findNumbersCommon(new ArrayList<>());

    }
    @GetMapping("/test/numbers/{num1:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers1(@PathVariable("num1") Long num1) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        return findNumbersCommon(numbers);
    }
    @GetMapping("/test/numbers/{num1:[0-9]+}/{num2:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers2(@PathVariable("num1") Long num1,
                                         @PathVariable("num2") Long num2) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        numbers.add(num2);
        return findNumbersCommon(numbers);
    }
    @GetMapping("/test/numbers/{num1:[0-9]+}/{num2:[0-9]+}/{num3:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers3(@PathVariable("num1") Long num1,
                                         @PathVariable("num2") Long num2,
                                         @PathVariable("num3") Long num3) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        numbers.add(num2);
        numbers.add(num3);
        return findNumbersCommon(numbers);
    }
    @GetMapping("/test/numbers/{num1:[0-9]+}/{num2:[0-9]+}/{num3:[0-9]+}/{num4:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers4(@PathVariable("num1") Long num1,
                                         @PathVariable("num2") Long num2,
                                         @PathVariable("num3") Long num3,
                                         @PathVariable("num4") Long num4) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        numbers.add(num2);
        numbers.add(num3);
        numbers.add(num4);
        return findNumbersCommon(numbers);
    }
    @GetMapping("/test/numbers/{num1:[0-9]+}/{num2:[0-9]+}/{num3:[0-9]+}/{num4:[0-9]+}/{num5:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers5(@PathVariable("num1") Long num1,
                                         @PathVariable("num2") Long num2,
                                         @PathVariable("num3") Long num3,
                                         @PathVariable("num4") Long num4,
                                         @PathVariable("num5") Long num5) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        numbers.add(num2);
        numbers.add(num3);
        numbers.add(num4);
        numbers.add(num5);
        return findNumbersCommon(numbers);
    }
    @GetMapping("/test/numbers/{num1:[0-9]+}/{num2:[0-9]+}/{num3:[0-9]+}/{num4:[0-9]+}/{num5:[0-9]+}/{num6:[0-9]+}")
    @ResponseBody
    public List<List<Long>> findNumbers6(@PathVariable("num1") Long num1,
                                         @PathVariable("num2") Long num2,
                                         @PathVariable("num3") Long num3,
                                         @PathVariable("num4") Long num4,
                                         @PathVariable("num5") Long num5,
                                         @PathVariable("num6") Long num6) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(num1);
        numbers.add(num2);
        numbers.add(num3);
        numbers.add(num4);
        numbers.add(num5);
        numbers.add(num6);
        return findNumbersCommon(numbers);
    }
*/

}
