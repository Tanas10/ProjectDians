package mk.finki.ukim.mk.dasproject.service.implementation;
import mk.finki.ukim.mk.dasproject.model.StockData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TechnicalIndicatorService {

    public Map<String, String> generateSignalsForPeriod(List<Double> prices, int period) {
        Map<String, String> signals = new LinkedHashMap<>();
        signals.put("RSI", generateRSISignal(prices, period));
        signals.put("MACD", generateMACDSignal(prices));
        signals.put("Stochastic Oscillator", generateStochasticSignal(prices, period));
        signals.put("ATR", generateATRSignal(prices, period));
        signals.put("CCI", generateCCISignal(prices, period));
        signals.put("SMA", generateSMASignal(prices, period));
        signals.put("EMA", generateEMASignal(prices, period));
        signals.put("WMA", generateWMASignal(prices, period));
        signals.put("HMA", generateHMASignal(prices, period));
        signals.put("AMA", generateAMASignal(prices, period));
        return signals;
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
