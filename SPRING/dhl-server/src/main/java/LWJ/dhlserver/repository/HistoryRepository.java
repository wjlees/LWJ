package LWJ.dhlserver.repository;

import LWJ.dhlserver.custom.Winning;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    Long getDataSize();
    Winning add(Winning winning);
    Optional<List<Long>> findNumbersByRound(Long round);
    Optional<List<List<Long>>> findNumbersByNumber(Long number);
    List<Winning> findAll();
}
