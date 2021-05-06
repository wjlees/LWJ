package LWJ.dhlserver.controller;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final HistoryService historyService;
    Crawling crawling;

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
        this.crawling = historyService.crawling();
        return this.crawling;
    }

    @GetMapping("/crawl/web")
    @ResponseBody
    public String crawling_web() throws IOException {
        if (this.crawling == null) {
            crawling();
        }
        return this.crawling.getWeb().toString();
    }

    @GetMapping("/crawl/mobile")
    @ResponseBody
    public String crawling_mobile() throws IOException {
        if (this.crawling == null) {
            crawling();
        }
        return this.crawling.getMobile().toString();
    }

    @GetMapping("/test")
    @ResponseBody
    public List<Long> winning() {
        return historyService.findRoundNumbers(960L);
    }

}
