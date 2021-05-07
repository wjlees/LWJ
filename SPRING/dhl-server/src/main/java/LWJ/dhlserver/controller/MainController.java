package LWJ.dhlserver.controller;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final HistoryService historyService;

    @Autowired
    public MainController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/")
    public String homeControl() {
        return "home";
    }

    @GetMapping("/crawl")
    @ResponseBody
    public Crawling crawling() throws IOException {
        System.out.println("test_print");
        return historyService.crawling();
    }

    @GetMapping("/crawl/web")
    @ResponseBody
    public String crawling_web() throws IOException {
        if (historyService.getCrawling() == null) {
            crawling();
        }
        return historyService.getCrawling().getWeb().toString();
    }

    @GetMapping("/crawl/mobile")
    @ResponseBody
    public String crawling_mobile() throws IOException {
        if (historyService.getCrawling() == null) {
            crawling();
        }
        return historyService.getCrawling().getMobile().toString();
    }

    @GetMapping("/update")
    @ResponseBody
    public Long update() throws IOException {
        Long lastRound = historyService.getLastRound(historyService.getCrawling());
        Long repositoryLastRound = historyService.getHistoryRepository().getDataSize();
        if (lastRound != repositoryLastRound) {
            for (long i = repositoryLastRound + 1L; i <= lastRound; i++) {
                Winning winning = historyService.crawlOneRound(i);
                historyService.save(winning);
            }
            return lastRound - repositoryLastRound;
        }
        return 0L;
    }

    @GetMapping("/test")
    @ResponseBody
    public List<Long> winning() {
        return historyService.findRoundNumbers(99L);
    }

    @GetMapping("/numbers")
    @ResponseBody
    public List<List<Long>> findNumbersString(@RequestParam(value="find", required = false) List<Long> numbers) {
        return findNumbersCommon(numbers);
    }


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
    public List<List<Long>> findNumbersCommon(List<Long> numbers) {
        List<List<Long>> numbersList = historyService.findAllNumbers();
        if (numbers == null) return numbersList;
        for (Long number: numbers) {
            List<List<Long>> result = numbersList.stream().filter(numList -> numList.contains(number)).collect(Collectors.toList());
            numbersList = result;
        }
        return numbersList;
    }

}
