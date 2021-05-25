package LWJ.dhlserver.repository;

import LWJ.dhlserver.custom.Information;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MemoryHistoryRepository720 implements HistoryRepository {

    /************************* Memory ***************************/
    private Map<Long, Information> repository = new HashMap<>();
    public Map<Long, Information> getRepository() {
        return repository;
    }
    /************************************************************/

    @Override
    public Long getDataSize() {
        return (long)repository.size();
    }

    @Override
    public Long add(Information information) {
        Long key = information.getWinning().getRound();
        repository.put(key, information);
        return key;
    }

    @Override
    public Information findInformationByRound(Long round) { // 없으면?
        Information information = repository.get(round); // 없으면 null

        return information; // 없으면 null return
    }

    @Override
    public List<Information> findInformationListByNumber(Long number) {
        List<Information> result = repository.values().stream().filter(
                info -> info.getWinning().getNumbers().contains(number)
        ).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Information> findAll() {
        List<Information> result = repository.values().stream().collect(Collectors.toList());
        return result;
    }
}
