package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.MemoryHistoryRepository645;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

class HistoryService645Test {
    private MemoryHistoryRepository645 historyRepository = new MemoryHistoryRepository645();
    private MemoryHistoryRepository645 mockitoRepository;
    private HistoryService645 historyService645;
    private HistoryService645 mockitoService645;

    Information info = new Information();

    @BeforeEach
    void setUp() throws IOException {
        historyRepository = new MemoryHistoryRepository645();
        historyService645 = new HistoryService645(historyRepository);

        Winning winning = new Winning();
        winning.setRound(1L);
        winning.setNumbers(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        info.setWinning(winning);
        List<Long> rn = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        info.setRecommend(List.of(rn, rn, rn, rn, rn));
        info.setWinningMoneyTotalStr("100원");
        info.setWinningMoneyGameStr("10원");
        historyRepository.add(info);


        mockitoRepository = Mockito.mock(historyRepository.getClass());
        mockitoService645 = new HistoryService645(mockitoRepository);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void update() throws IOException {
        // given
        Mockito.when(mockitoRepository.getDataSize()).thenReturn(963L);

        // when
        boolean result = mockitoService645.update();

        // then
        Assertions.assertThat(result);
    }

    @Test
    void getOneRound() throws IOException {
        // given

        // when
        Information info = historyService645.getOneRound(1L);

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(1L);

        // ...???
    }

    @Test
    void getLastInfo() {
        // given
        // when
        Information info = historyService645.getLastInfo();

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(1L);
    }

    @Test
    void getNextInfo() {
        // given
        // when
        Information info = historyService645.getNextInfo();

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(2L);
    }

    @Test
    void getRecommendNumbers() {
        // given
        // when
        List<List<Long>> recommendNumbers = historyService645.getRecommendNumbers(null);

        // then
        Assertions.assertThat(recommendNumbers.size()).isEqualTo(5);
    }
}