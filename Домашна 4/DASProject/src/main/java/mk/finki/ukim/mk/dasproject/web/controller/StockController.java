package mk.finki.ukim.mk.dasproject.web.controller;

import mk.finki.ukim.mk.dasproject.model.StockData;
import mk.finki.ukim.mk.dasproject.service.implementation.FileService;
import mk.finki.ukim.mk.dasproject.service.implementation.StockService;
import mk.finki.ukim.mk.dasproject.service.implementation.TechnicalIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StockController {

    @Autowired
    private final StockService stockService;
    private final FileService fileService;
    private final TechnicalIndicatorService technicalIndicatorService;
    private final ResourceLoader resourceLoader;

    public StockController(StockService stockService, FileService fileService, TechnicalIndicatorService technicalIndicatorService, ResourceLoader resourceLoader) {
        this.stockService = stockService;
        this.fileService = fileService;
        this.technicalIndicatorService = technicalIndicatorService;

        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/stocks")
    public String displayStocks(@RequestParam(defaultValue = "ALK") String code, @RequestParam(required = false) String toYear, @RequestParam(required = false) String fromYear, Model model) {


        String filePath = "classpath:python/database/" + code + ".csv";

        try {

            Resource resource = resourceLoader.getResource(filePath);

            if (resource.exists()) {

                List<StockData> stocks = stockService.readStockData(resource.getFile().getAbsolutePath());


                List<Double> prices = stocks.stream()
                        .map(StockData::getPriceOfLastTransaction)
                        .collect(Collectors.toList());


                double maxPrice = stockService.findMaximumPrice(stocks);
                double minPrice = stockService.findMinimumPrice(stocks);


                List<String> fileNames = fileService.getFileNames();
                model.addAttribute("fileNames", fileNames);
                model.addAttribute("maxPrice", maxPrice);
                model.addAttribute("minPrice", minPrice);


//1day
                model.addAttribute("RSId", "RSI: " + technicalIndicatorService.generateRSISignal(prices, 1));
                model.addAttribute("MACDd", "MACD: " + technicalIndicatorService.generateMACDSignal(prices));
                model.addAttribute("Stochastic_Oscillatord", "Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 1));
                model.addAttribute("ATRd", "ATR: " + technicalIndicatorService.generateATRSignal(prices, 1));
                model.addAttribute("CCId", "CCI: " + technicalIndicatorService.generateCCISignal(prices, 1));

                model.addAttribute("SMAd", "SMA: " + technicalIndicatorService.generateSMASignal(prices, 1));
                model.addAttribute("EMAd", "EMA: " + technicalIndicatorService.generateEMASignal(prices, 1));
                model.addAttribute("WMAd", "WMA: " + technicalIndicatorService.generateWMASignal(prices, 1));
                model.addAttribute("HMAd", "HMA: " + technicalIndicatorService.generateHMASignal(prices, 1));
                model.addAttribute("AMAd", "AMA: " + technicalIndicatorService.generateAMASignal(prices, 1));
                //1week


                model.addAttribute("RSIw", "RSI: " + technicalIndicatorService.generateRSISignal(prices, 7));
                model.addAttribute("MACDw", "MACD: " + technicalIndicatorService.generateMACDSignal(prices));
                model.addAttribute("Stochastic_Oscillatorw", "Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 7));
                model.addAttribute("ATRw", "ATR: " + technicalIndicatorService.generateATRSignal(prices, 7));
                model.addAttribute("CCIw", "CCI: " + technicalIndicatorService.generateCCISignal(prices, 7));

                model.addAttribute("SMAw", "SMA: " + technicalIndicatorService.generateSMASignal(prices, 7));
                model.addAttribute("EMAw", "EMA: " + technicalIndicatorService.generateEMASignal(prices, 7));
                model.addAttribute("WMAw", "WMA: " + technicalIndicatorService.generateWMASignal(prices, 7));
                model.addAttribute("HMAw", "HMA: " + technicalIndicatorService.generateHMASignal(prices, 7));
                model.addAttribute("AMAw", "AMA: " + technicalIndicatorService.generateAMASignal(prices, 7));

                //1week


                model.addAttribute("RSIm", "RSI: " + technicalIndicatorService.generateRSISignal(prices, 30));
                model.addAttribute("MACDm", "MACD: " + technicalIndicatorService.generateMACDSignal(prices));
                model.addAttribute("Stochastic_Oscillatorm", "Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 30));
                model.addAttribute("ATRm", "ATR: " + technicalIndicatorService.generateATRSignal(prices, 30));
                model.addAttribute("CCIm", "CCI: " + technicalIndicatorService.generateCCISignal(prices, 30));

                model.addAttribute("SMAm", "SMA: " + technicalIndicatorService.generateSMASignal(prices, 30));
                model.addAttribute("EMAm", "EMA: " + technicalIndicatorService.generateEMASignal(prices, 30));
                model.addAttribute("WMAm", "WMA: " + technicalIndicatorService.generateWMASignal(prices, 30));
                model.addAttribute("HMAm", "HMA: " + technicalIndicatorService.generateHMASignal(prices, 30));
                model.addAttribute("AMAm", "AMA: " + technicalIndicatorService.generateAMASignal(prices, 30));


            } else {
                System.out.println("File not found: " + filePath);
                model.addAttribute("error", "File not found for stock code: " + code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error reading stock data for code: " + code);
        }

        return "dashboard";
    }

}