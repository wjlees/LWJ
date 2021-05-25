package LWJ.dhlserver.service;

import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;
import LWJ.dhlserver.repository.MemoryHistoryRepository645;
import LWJ.dhlserver.repository.MemoryHistoryRepository720;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryService720Test {
    private MemoryHistoryRepository720 historyRepository = new MemoryHistoryRepository720();
    private MemoryHistoryRepository720 mockitoRepository;
    private HistoryService720 historyService720;
    private HistoryService720 mockitoService720;

    Information info = new Information();

    @BeforeEach
    void setUp() throws IOException {
        historyRepository = new MemoryHistoryRepository720();
        historyService720 = new HistoryService720(historyRepository);

        Winning winning = new Winning();
        winning.setRound(1L);
        winning.setNumbers(List.of(1L, 0L, 1L, 2L, 3L, 4L, 5L));
        info.setWinning(winning);
        List<Long> rn = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        info.setRecommend(List.of(rn));
        info.setWinningMoneyTotalStr("100원");
        info.setWinningMoneyGameStr("10원");
        historyRepository.add(info);


        mockitoRepository = Mockito.mock(historyRepository.getClass());
        mockitoService720 = new HistoryService720(mockitoRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void update() throws IOException {
        // given
        Mockito.when(mockitoRepository.getDataSize()).thenReturn(54L);

        // when
        boolean result = mockitoService720.update();

        // then
        Assertions.assertThat(result);
    }

    @Test
    void getOneRound() throws IOException {
        // given

        // when
        Information info = historyService720.getOneRound(1L);

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(1L);
    }

    @Test
    void getLastInfo() {
        // given
        // when
        Information info = historyService720.getLastInfo();

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(1L);
    }

    @Test
    void getNextInfo() {
        // given
        // when
        Information info = historyService720.getNextInfo();

        // then
        Assertions.assertThat(info.getWinning().getRound()).isEqualTo(2L);
    }

    @Test
    void getRecommendNumbers() {
        // given
        // when
        List<List<Long>> recommendNumbers = historyService720.getRecommendNumbers(null);

        // then
        Assertions.assertThat(recommendNumbers.size()).isEqualTo(1);
    }
}