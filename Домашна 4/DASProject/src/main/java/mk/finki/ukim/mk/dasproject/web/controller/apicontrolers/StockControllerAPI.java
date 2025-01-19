package mk.finki.ukim.mk.dasproject.web.controller.apicontrolers;



import mk.finki.ukim.mk.dasproject.model.StockData;

import mk.finki.ukim.mk.dasproject.service.implementation.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/stocks")
public class StockControllerAPI {

    private final StockService stockService;

    @Autowired
    public StockControllerAPI(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{fileName}")
    public List<StockData> getStockData(@PathVariable String fileName) {
        return stockService.readStockDatabyFileName(fileName);
    }
}
