package LWJ.dhlserver.service;

import LWJ.dhlserver.repository.MemoryHistoryRepository645;

import java.io.IOException;

class HistoryServiceTest {
    MemoryHistoryRepository645 historyRepository = new MemoryHistoryRepository645();
    HistoryService645 historyService645 = new HistoryService645(historyRepository);

    HistoryServiceTest() throws IOException {
    }


//
//    @BeforeEach
//    void beforeEach() throws IOException {
//    }
//    @AfterEach
//    void afterEach() {
//
//    }
/*
    @Test
    void crawling() throws IOException {
        Crawling crawling = historyService.crawling();
        Assertions.assertThat(crawling).isNotNull();
    }

    @Test
    void crawlOneRound() throws IOException {
        Winning winning = historyService.crawlOneRound(20L);
        Assertions.assertThat(winning.getRound()).isEqualTo(20L);
    }

    @Test
    void getLastRound() {
        Long lastRound = historyService.getLastRound();
        Assertions.assertThat(lastRound).isEqualTo(962);
    }

    @Test
    void getLastWinning() {
        Winning winning = historyService.getLastWinning();
        Assertions.assertThat(winning.getRound()).isEqualTo(historyService.getLastRound());
    }

    @Test
    void save() {
        Long beforeSize = historyRepository.getDataSize();
        Winning winning = new Winning();
        winning.setRound(9999L);
        winning.setNumbers(new ArrayList<>());
        boolean result = historyService.save(winning);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void findRoundNumbers() {
        List<Long> result = historyService.findRoundNumbers(1L);
        Assertions.assertThat(result.size()).isEqualTo(7);
    }

    @Test
    void findWinningNumbers() {
        Long num = 10L;
        List<List<Long>> result = historyService.findWinningNumbers(num);
        for (List<Long> numbers: result) {
            Assertions.assertThat(numbers.contains(num)).isTrue();
        }
    }

    @Test
    void findAllNumbers() {
        List<List<Long>> result = historyService.findAllNumbers();
        Assertions.assertThat((long)result.size()).isEqualTo(historyRepository.getDataSize());
    }
 */
}