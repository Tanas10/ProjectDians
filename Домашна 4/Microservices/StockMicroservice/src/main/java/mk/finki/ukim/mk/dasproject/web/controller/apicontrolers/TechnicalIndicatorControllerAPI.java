package mk.finki.ukim.mk.dasproject.web.controller.apicontrolers;

import mk.finki.ukim.mk.dasproject.model.StockData;
import mk.finki.ukim.mk.dasproject.service.implementation.StockService;
import mk.finki.ukim.mk.dasproject.service.implementation.TechnicalIndicatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/indicators")
public class TechnicalIndicatorControllerAPI {

    private final StockService stockDataService;
    private final TechnicalIndicatorService technicalIndicatorService;

    public TechnicalIndicatorControllerAPI(StockService stockDataService, TechnicalIndicatorService technicalIndicatorService) {
        this.stockDataService = stockDataService;
        this.technicalIndicatorService = technicalIndicatorService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Map<String, Object>> calculateIndicators(
            @PathVariable String fileName) {
        try {
            List<StockData> stockDataList = stockDataService.readStockDatabyFileName(fileName);
            if (stockDataList.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No data available in the file."));
            }

            List<Double> prices = stockDataList.stream()
                    .map(StockData::getPriceOfLastTransaction)
                    .collect(Collectors.toList());

            if (prices.size() < 30) {
                return ResponseEntity.badRequest().body(Map.of("error", "Insufficient data for 1-month calculations."));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("1day", technicalIndicatorService.generateSignalsForPeriod(prices, 1));
            response.put("1week", technicalIndicatorService.generateSignalsForPeriod(prices, 7));
            response.put("1month", technicalIndicatorService.generateSignalsForPeriod(prices, 30));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
