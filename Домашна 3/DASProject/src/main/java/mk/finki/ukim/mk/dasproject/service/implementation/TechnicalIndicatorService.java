package mk.finki.ukim.mk.dasproject.service.implementation;
import mk.finki.ukim.mk.dasproject.model.StockData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicalIndicatorService {




    public void generateSignals(List<Double> prices, int period) {
        System.out.println("Осцилатори");
        System.out.println("RSI: " + generateRSISignal(prices, period));
        System.out.println("MACD: " + generateMACDSignal(prices));
        System.out.println("Stochastic Oscillator: " + generateStochasticSignal(prices, period));
        System.out.println("ATR: " + generateATRSignal(prices, period));
        System.out.println("CCI: " + generateCCISignal(prices, period));
        System.out.println("Moving Averages");
        System.out.println("SMA: " + generateSMASignal(prices, period));
        System.out.println("EMA: " + generateEMASignal(prices, period));
        System.out.println("WMA: " + generateWMASignal(prices, period));
        System.out.println("HMA: " + generateHMASignal(prices, period));
        System.out.println("AMA: " + generateAMASignal(prices, period));
    }


    public String generateRSISignal(List<Double> prices, int period) {
        List<Double> rsi = calculateRSI(prices, period);
        double lastRSI = rsi.get(rsi.size() - 1);
        if (lastRSI > 70) return "SELL";
        if (lastRSI < 30) return "BUY";
        return "HOLD";
    }

    public String generateMACDSignal(List<Double> prices) {
        List<Double> shortEMA = calculateEMA(prices, 12);
        List<Double> longEMA = calculateEMA(prices, 26);
        if (shortEMA.size() < longEMA.size()) return "HOLD";

        List<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < longEMA.size(); i++) {
            macdLine.add(shortEMA.get(i + (shortEMA.size() - longEMA.size())) - longEMA.get(i));
        }

        List<Double> signalLine = calculateEMA(macdLine, 9);
        double lastMACD = macdLine.get(macdLine.size() - 1);
        double lastSignal = signalLine.get(signalLine.size() - 1);

        if (lastMACD > lastSignal) return "BUY";
        if (lastMACD < lastSignal) return "SELL";
        return "HOLD";
    }

    public String generateStochasticSignal(List<Double> prices, int period) {
        double lastStochastic = calculateStochasticOscillator(prices, period);
        if (lastStochastic > 80) return "SELL";
        if (lastStochastic < 20) return "BUY";
        return "HOLD";
    }

    public String generateATRSignal(List<Double> prices, int period) {
        double lastATR = calculateATR(prices, period);


        double highVolatilityThreshold = 1.5;
        double lowVolatilityThreshold = 0.5;

        if (lastATR > highVolatilityThreshold) {
            return "SELL";
        } else if (lastATR < lowVolatilityThreshold) {
            return "BUY";
        }
        return "HOLD";
    }


    public String generateCCISignal(List<Double> prices, int period) {
        double lastCCI = calculateCCI(prices, period);
        if (lastCCI > 100) return "SELL";
        if (lastCCI < -100) return "BUY";
        return "HOLD";
    }

    public String generateSMASignal(List<Double> prices, int period) {
        List<Double> sma = calculateSMA(prices, period);
        return comparePriceToIndicator(prices, sma);
    }

    public String generateEMASignal(List<Double> prices, int period) {
        List<Double> ema = calculateEMA(prices, period);
        return comparePriceToIndicator(prices, ema);
    }

    public String generateWMASignal(List<Double> prices, int period) {
        List<Double> wma = calculateWMA(prices, period);
        return comparePriceToIndicator(prices, wma);
    }

    public String generateHMASignal(List<Double> prices, int period) {
        List<Double> hma = calculateHMA(prices, period);
        return comparePriceToIndicator(prices, hma);
    }

    public String generateAMASignal(List<Double> prices, int period) {
        List<Double> ama = calculateAMA(prices, period);
        return comparePriceToIndicator(prices, ama);
    }


    public List<Double> calculateSMA(List<Double> prices, int period) {
        List<Double> sma = new ArrayList<>();
        for (int i = 0; i <= prices.size() - period; i++) {
            List<Double> subList = prices.subList(i, i + period);
            double average = subList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            sma.add(average);
        }
        return sma;
    }

    public List<Double> calculateEMA(List<Double> prices, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);
        double prevEMA = prices.get(0);
        ema.add(prevEMA);

        for (int i = 1; i < prices.size(); i++) {
            double currentEMA = (prices.get(i) - prevEMA) * multiplier + prevEMA;
            ema.add(currentEMA);
            prevEMA = currentEMA;
        }
        return ema;
    }

    public List<Double> calculateWMA(List<Double> prices, int period) {
        List<Double> wma = new ArrayList<>();
        for (int i = 0; i <= prices.size() - period; i++) {
            double weightedSum = 0.0;
            int weightSum = 0;
            for (int j = 0; j < period; j++) {
                weightedSum += prices.get(i + j) * (period - j);
                weightSum += (period - j);
            }
            wma.add(weightedSum / weightSum);
        }
        return wma;
    }


    public List<Double> calculateHMA(List<Double> prices, int period) {
        if (prices.size() < period) return new ArrayList<>();
        List<Double> halfWMA = calculateWMA(prices, period / 2);
        List<Double> fullWMA = calculateWMA(prices, period);

        List<Double> diffWMA = new ArrayList<>();
        for (int i = 0; i < fullWMA.size(); i++) {
            diffWMA.add(2 * halfWMA.get(i) - fullWMA.get(i));
        }

        return calculateWMA(diffWMA, (int) Math.sqrt(period));
    }


    public List<Double> calculateAMA(List<Double> prices, int period) {
        List<Double> ama = new ArrayList<>();
        if (prices.size() < period) return ama;

        double prevAMA = prices.get(0);
        ama.add(prevAMA);

        for (int i = 1; i < prices.size(); i++) {
            double price = prices.get(i);
            double smoothingConstant = 2.0 / (period + 1);
            double currentAMA = prevAMA + smoothingConstant * (price - prevAMA);
            ama.add(currentAMA);
            prevAMA = currentAMA;
        }
        return ama;
    }


    public List<Double> calculateRSI(List<Double> prices, int period) {
        List<Double> rsi = new ArrayList<>();
        if (prices.size() < period) return rsi;

        for (int i = period; i < prices.size(); i++) {
            double gain = 0, loss = 0;
            for (int j = i - period; j < i; j++) {
                double change = prices.get(j + 1) - prices.get(j);
                if (change > 0) gain += change;
                else loss -= change;
            }
            double avgGain = gain / period;
            double avgLoss = loss / period;
            double rs = avgLoss == 0 ? 0 : avgGain / avgLoss;
            rsi.add(100 - (100 / (1 + rs)));
        }
        return rsi;
    }


    public double calculateStochasticOscillator(List<Double> prices, int period) {
        if (prices.size() < period) return 50.0;

        double currentClose = prices.get(prices.size() - 1);
        double low = Double.MAX_VALUE, high = Double.MIN_VALUE;

        for (int i = prices.size() - period; i < prices.size(); i++) {
            low = Math.min(low, prices.get(i));
            high = Math.max(high, prices.get(i));
        }

        return ((currentClose - low) / (high - low)) * 100;
    }


    public double calculateATR(List<Double> prices, int period) {
        if (prices.size() < period) return 0.0;

        List<Double> trueRanges = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            double currentHigh = prices.get(i);
            double currentLow = prices.get(i);
            double prevClose = prices.get(i - 1);

            double tr1 = currentHigh - currentLow;
            double tr2 = Math.abs(currentHigh - prevClose);
            double tr3 = Math.abs(currentLow - prevClose);

            trueRanges.add(Math.max(tr1, Math.max(tr2, tr3)));
        }

        double atr = trueRanges.subList(0, period).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        for (int i = period; i < trueRanges.size(); i++) {
            atr = ((atr * (period - 1)) + trueRanges.get(i)) / period;
        }
        return atr;
    }


    public double calculateCCI(List<Double> prices, int period) {
        if (prices.size() < period) return 0.0;

        List<Double> typicalPrices = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            double typicalPrice = prices.get(i); // Assuming close prices
            typicalPrices.add(typicalPrice);
        }

        List<Double> sma = calculateSMA(typicalPrices, period);
        double lastSMA = sma.get(sma.size() - 1);
        double meanDeviation = 0.0;

        for (int i = prices.size() - period; i < prices.size(); i++) {
            meanDeviation += Math.abs(typicalPrices.get(i) - lastSMA);
        }
        meanDeviation /= period;

        double lastTypicalPrice = typicalPrices.get(typicalPrices.size() - 1);
        return (lastTypicalPrice - lastSMA) / (0.015 * meanDeviation);
    }


    private static String comparePriceToIndicator(List<Double> prices, List<Double> indicator) {
        double lastPrice = prices.get(prices.size() - 1);
        double lastIndicator = indicator.get(indicator.size() - 1);
        if (lastPrice > lastIndicator) return "BUY";
        if (lastPrice < lastIndicator) return "SELL";
        return "HOLD";
    }
}






























//    public double calculateRSI(List<StockData> stockData, int period) {
//        double gain = 0;
//        double loss = 0;
//
//        for (int i = 1; i < period; i++) {
//            double change = stockData.get(i).getPriceOfLastTransaction() - stockData.get(i - 1).getPriceOfLastTransaction();
//            if (change > 0) {
//                gain += change;
//            } else {
//                loss -= change; // loss is negative so we subtract it
//            }
//        }
//
//        gain /= period;
//        loss /= period;
//
//        double rs = gain / Math.abs(loss);
//        return 100 - (100 / (1 + rs));
//    }
//
//    public double calculateStochasticOscillator(List<StockData> stockData, int period) {
//        if (stockData.size() < period) {
//            throw new IllegalArgumentException("Not enough data to calculate Stochastic Oscillator");
//        }
//
//        double currentClose = stockData.get(stockData.size() - 1).getPriceOfLastTransaction();
//
//        // Find lowest low and highest high over the period
//        double lowestLow = Double.MAX_VALUE;
//        double highestHigh = Double.MIN_VALUE;
//
//        for (int i = stockData.size() - period; i < stockData.size(); i++) {
//            double low = stockData.get(i).getMin();
//            double high = stockData.get(i).getMax();
//
//            if (low < lowestLow) {
//                lowestLow = low;
//            }
//            if (high > highestHigh) {
//                highestHigh = high;
//            }
//        }
//
//        // Calculate Stochastic
//        return ((currentClose - lowestLow) / (highestHigh - lowestLow)) * 100;
//    }
//
//    public double[] calculateMACD(List<StockData> stockData) {
//        if (stockData.size() < 26) {
//            throw new IllegalArgumentException("Not enough data to calculate MACD");
//        }
//
//        // Calculate EMAs
//        double ema12 = calculateEMA(stockData, 12);
//        double ema26 = calculateEMA(stockData, 26);
//
//        // Calculate MACD Line
//        double macdLine = ema12 - ema26;
//
//        // To calculate Signal Line, we need to keep track of MACD values
//        List<Double> macdValues = new ArrayList<>();
//
//        for (int i = 0; i < stockData.size(); i++) {
//            double closePrice = stockData.get(i).getPriceOfLastTransaction();
//            macdValues.add(closePrice); // Store closing prices for further EMA calculations
//        }
//
//        // Calculate Signal Line (9-day EMA of MACD values)
//        double signalLine = calculateEMA(stockData, 9);
//
//        return new double[]{macdLine, signalLine};
//    }
//
//    private double calculateEMA(List<StockData> stockData, int period) {
//        double multiplier = 2.0 / (period + 1);
//
//        // Start with the first closing price as the initial EMA value
//        double ema = stockData.get(0).getPriceOfLastTransaction();
//
//        for (int i = 1; i < stockData.size(); i++) {
//            double closePrice = stockData.get(i).getPriceOfLastTransaction();
//            ema = ((closePrice - ema) * multiplier) + ema;
//        }
//
//        return ema;
//    }
//
//
//    // Add methods for other indicators...
//
//    public String generateSignals(List<StockData> stockData, int stochasticPeriod) {
//        if (stockData.size() < 2) {
//            System.out.println("Not enough data to generate signals.");
//            return null;
//        }
//
//        // Calculate Stochastic Oscillator and MACD
//
//        double stochasticValue = calculateStochasticOscillator(stockData, stochasticPeriod);
//        double[] macdValues = calculateMACD(stockData);
//        double macdLine = macdValues[0];
//        double signalLine = macdValues[1];
//
//        // Generate signals based on conditions
//        String signal;
//
//        // Example thresholds for Stochastic
//        double overboughtThreshold = 80;
//        double oversoldThreshold = 20;
//
//        // Conditions for Buy/Sell signals
//        if (stochasticValue < oversoldThreshold && macdLine > signalLine) {
//            signal = "Buy";
//        } else if (stochasticValue > overboughtThreshold && macdLine < signalLine) {
//            signal = "Sell";
//        } else {
//            signal = "Hold";
//        }
//
//        // Output the generated signal
//        System.out.println("Generated Signal: " + signal);
//        return "Generated Signal: " + signal;
//    }
//    ///
//
//
//
//    public static List<Double> calculateSMA(List<StockData> stocks, int period) {
//        List<Double> prices = stocks.stream().map(StockData::getPriceOfLastTransaction).collect(Collectors.toList());
//        List<Double> sma = new ArrayList<>();
//        for (int i = 0; i <= prices.size() - period; i++) {
//            List<Double> subList = prices.subList(i, i + period);
//            double average = subList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
//            sma.add(average);
//        }
//        return sma;
//    }
//
//    public static List<Double> calculateEMA1(List<StockData> stocks, int period) {
//        List<Double> prices = stocks.stream().map(StockData::getPriceOfLastTransaction).collect(Collectors.toList());
//        List<Double> ema = new ArrayList<>();
//        double multiplier = 2.0 / (period + 1);
//        double prevEMA = prices.get(0);
//        ema.add(prevEMA);
//
//        for (int i = 1; i < prices.size(); i++) {
//            double currentEMA = (prices.get(i) - prevEMA) * multiplier + prevEMA;
//            ema.add(currentEMA);
//            prevEMA = currentEMA;
//        }
//        return ema;
//    }
//    public static List<Double> calculateRSI1(List<StockData> stocks, int period) {
//        List<Double> prices = stocks.stream().map(StockData::getPriceOfLastTransaction).collect(Collectors.toList());
//        List<Double> rsi = new ArrayList<>();
//        for (int i = 0; i <= prices.size() - period; i++) {
//            List<Double> subList = prices.subList(i, i + period);
//            double gain = 0.0, loss = 0.0;
//            for (int j = 1; j < subList.size(); j++) {
//                double change = subList.get(j) - subList.get(j - 1);
//                if (change > 0) gain += change;
//                else loss -= change;
//            }
//            double avgGain = gain / period;
//            double avgLoss = loss / period;
//            double rs = avgLoss == 0 ? 0 : avgGain / avgLoss;
//            rsi.add(100 - (100 / (1 + rs)));
//        }
//        return rsi;
//    }
//
//    public static String generateSignal(List<StockData> stocks, int period) {
//        List<Double> prices = stocks.stream().map(StockData::getPriceOfLastTransaction).collect(Collectors.toList());
//        List<Double> sma = calculateSMA(stocks, period);
//        List<Double> ema = calculateEMA1(stocks, period);
//        List<Double> rsi = calculateRSI1(stocks, period);
//            String result ="";
//        if (!sma.isEmpty()) {
//            double lastSMA = sma.get(sma.size() - 1);
//            double lastPrice = prices.get(prices.size() - 1);
//            if (lastPrice > lastSMA) {
//                result = "Signal: BUY (Price is above SMA)";
//            } else if (lastPrice < lastSMA) {
//                result = "Signal: SELL (Price is below SMA)";
//            } else {
//                result = "Signal: HOLD (Price is at SMA)";
//            }
//        }
//
//        if (!rsi.isEmpty()) {
//            double lastRSI = rsi.get(rsi.size() - 1);
//            if (lastRSI > 70) {
//                System.out.println("RSI Signal: SELL (Overbought)");
//            } else if (lastRSI < 30) {
//                System.out.println("RSI Signal: BUY (Oversold)");
//            } else {
//                System.out.println("RSI Signal: HOLD (Neutral RSI)");
//            }
//        }
//
//        System.out.println("Last SMA: " + (sma.isEmpty() ? "N/A" : sma.get(sma.size() - 1)));
//        System.out.println("Last EMA: " + (ema.isEmpty() ? "N/A" : ema.get(ema.size() - 1)));
//        System.out.println("Last RSI: " + (rsi.isEmpty() ? "N/A" : rsi.get(rsi.size() - 1)));
//        return result;
//    }
//}