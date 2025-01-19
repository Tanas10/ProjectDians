package mk.finki.ukim.mk.dasproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockData {
    private String date;
    private double priceOfLastTransaction;
    private double max;
    private double min;
    private double averagePrice;
    private double percentProm;
    private double quantity;
    private int bestTurnoverInDenars;
    private int totalTurnoverInDenars;




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPriceOfLastTransaction() {
        return priceOfLastTransaction;
    }

    public void setPriceOfLastTransaction(double priceOfLastTransaction) {
        this.priceOfLastTransaction = priceOfLastTransaction;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getPercentProm() {
        return percentProm;
    }

    public void setPercentProm(double percentProm) {
        this.percentProm = percentProm;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getBestTurnoverInDenars() {
        return bestTurnoverInDenars;
    }

    public void setBestTurnoverInDenars(int bestTurnoverInDenars) {
        this.bestTurnoverInDenars = bestTurnoverInDenars;
    }

    public int getTotalTurnoverInDenars() {
        return totalTurnoverInDenars;
    }

    public void setTotalTurnoverInDenars(int totalTurnoverInDenars) {
        this.totalTurnoverInDenars = totalTurnoverInDenars;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "date='" + date + '\'' +
                ", priceOfLastTransaction='" + priceOfLastTransaction + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", averagePrice='" + averagePrice + '\'' +
                ", percentProm='" + percentProm + '\'' +
                ", quantity=" + quantity +
                ", bestTurnoverInDenars='" + bestTurnoverInDenars + '\'' +
                ", totalTurnoverInDenars='" + totalTurnoverInDenars + '\'' +
                '}';
    }
}