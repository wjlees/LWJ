package LWJ.dhlserver.repository;

import LWJ.dhlserver.custom.Winning;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryHistoryRepository implements HistoryRepository {

    private Map<Long, List<Long>> repository = new HashMap<>();

    public Map<Long, List<Long>> getRepository() {
        return repository;
    }

    @Override
    public Long getDataSize() {
        return (long)repository.size();
    }

    @Override
    public Winning add(Winning winning) {
        repository.put(winning.getRound(), winning.getNumbers());
        return winning;
    }

    @Override
    public Optional<List<Long>> findNumbersByRound(Long round) {
        return Optional.ofNullable(repository.get(round));
    }

    @Override
    public Optional<List<List<Long>>> findNumbersByNumber(Long number) {
        List<List<Long>> result = repository.values().stream().filter(numbers -> numbers.contains(number)).collect(Collectors.toList());
        return Optional.ofNullable(result);
    }

    @Override
    public List<List<Long>> findAll() {
        List<List<Long>> result = repository.values().stream().collect(Collectors.toList());
        return result;
    }
}
