package LWJ.dhlserver.repository;

import LWJ.dhlserver.custom.Information;
import LWJ.dhlserver.custom.Winning;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    Long getDataSize();
    Long add(Information information);
    Information findInformationByRound(Long round);
    List<Information> findInformationListByNumber(Long number);
    List<Information> findAll();
}
