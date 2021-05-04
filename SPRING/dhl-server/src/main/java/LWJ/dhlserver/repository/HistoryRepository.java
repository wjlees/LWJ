package LWJ.dhlserver.repository;

import LWJ.dhlserver.custom.Winning;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    Winning add(Long round, List<Long> numbers);
    Optional<Winning> find(Long round);
    Optional<List<Winning>> findByNumber(List<Long> numberList);
    List<Winning> findAll();
}
