package cn.com.ql.wiseBeijing.user.frontBean;

/**
 * Created by Administrator on 2017/11/15 0015.
 */
public class FanData {
    private Integer totalPeople;
    private Integer numberOfBoys;
    private Integer numberOfGirls;
    private Integer numberOfUnder15YearsOld;
    private Integer numberOf16_25earsOld;
    private Integer numberOf26_40earsOld;
    private Integer numberOfOver40YearsOld;

    public Integer getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Integer totalPeople) {
        this.totalPeople = totalPeople;
    }

    public Integer getNumberOfOver40YearsOld() {
        return numberOfOver40YearsOld;
    }

    public void setNumberOfOver40YearsOld(Integer numberOfOver40YearsOld) {
        this.numberOfOver40YearsOld = numberOfOver40YearsOld;
    }

    public Integer getNumberOf26_40earsOld() {
        return numberOf26_40earsOld;
    }

    public void setNumberOf26_40earsOld(Integer numberOf26_40earsOld) {
        this.numberOf26_40earsOld = numberOf26_40earsOld;
    }

    public Integer getNumberOf16_25earsOld() {
        return numberOf16_25earsOld;
    }

    public void setNumberOf16_25earsOld(Integer numberOf16_25earsOld) {
        this.numberOf16_25earsOld = numberOf16_25earsOld;
    }

    public Integer getNumberOfUnder15YearsOld() {
        return numberOfUnder15YearsOld;
    }

    public void setNumberOfUnder15YearsOld(Integer numberOfUnder15YearsOld) {
        this.numberOfUnder15YearsOld = numberOfUnder15YearsOld;
    }

    public Integer getNumberOfGirls() {
        return numberOfGirls;
    }

    public void setNumberOfGirls(Integer numberOfGirls) {
        this.numberOfGirls = numberOfGirls;
    }

    public Integer getNumberOfBoys() {
        return numberOfBoys;
    }

    public void setNumberOfBoys(Integer numberOfBoys) {
        this.numberOfBoys = numberOfBoys;
    }
}
