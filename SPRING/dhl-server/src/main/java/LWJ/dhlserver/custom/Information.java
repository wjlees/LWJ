package LWJ.dhlserver.custom;

import java.util.Date;
import java.util.List;

public class Information {

    private Winning winning;
    private Winning bonusWinning;
    private List<List<Long>> recommend;
    private String winningMoneyTotalStr;
    private String winningMoneyGameStr;

    public Winning getWinning() {
        return winning;
    }
    public void setWinning(Winning winning) {
        this.winning = winning;
    }
    public List<List<Long>> getRecommend() {
        return recommend;
    }
    public void setRecommend(List<List<Long>> recommend) {
        this.recommend = recommend;
    }
    public String getWinningMoneyTotalStr() {
        return winningMoneyTotalStr;
    }
    public void setWinningMoneyTotalStr(String winningMoneyTotalStr) {
        this.winningMoneyTotalStr = winningMoneyTotalStr;
    }
    public String getWinningMoneyGameStr() {
        return winningMoneyGameStr;
    }
    public void setWinningMoneyGameStr(String winningMoneyGameStr) {
        this.winningMoneyGameStr = winningMoneyGameStr;
    }
    public Winning getBonusWinning() {
        return bonusWinning;
    }
    public void setBonusWinning(Winning bonusWinning) {
        this.bonusWinning = bonusWinning;
    }
}
