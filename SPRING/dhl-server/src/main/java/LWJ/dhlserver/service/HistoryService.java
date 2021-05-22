package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Crawling;
import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;

import java.io.IOException;
import java.util.List;

public interface HistoryService {
    boolean update() throws IOException;
    Information getOneRound(Long round) throws IOException;
    Information getLastInfo();
    Information getNextInfo();
    List<List<Long>> getRecommendNumbers(List<Long> numbers);
}
