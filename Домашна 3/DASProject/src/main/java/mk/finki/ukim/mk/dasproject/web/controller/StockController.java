package mk.finki.ukim.mk.dasproject.web.controller;
import mk.finki.ukim.mk.dasproject.model.StockData;
import mk.finki.ukim.mk.dasproject.service.implementation.FileService;
import mk.finki.ukim.mk.dasproject.service.implementation.NewsService;
import mk.finki.ukim.mk.dasproject.service.implementation.StockService;
import mk.finki.ukim.mk.dasproject.service.implementation.TechnicalIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StockController {

    @Autowired
    private final StockService stockService;
    private final FileService fileService;
    private final TechnicalIndicatorService technicalIndicatorService;
    private final NewsService newsService;

    public StockController(StockService stockService, FileService fileService, TechnicalIndicatorService technicalIndicatorService, NewsService newsService) {
        this.stockService = stockService;
        this.fileService = fileService;
        this.technicalIndicatorService = technicalIndicatorService;
        this.newsService = newsService;
    }

    @GetMapping("/stocks")
    public String displayStocks(@RequestParam(defaultValue = "ALK") String code,@RequestParam(required = false) String toYear,@RequestParam(required = false) String fromYear , Model model) {
        String folderPath = "/home/atanas/Desktop/dianshw3/DASProject/src/main/java/mk/finki/ukim/mk/dasproject/python/database/";
        List<StockData> Stocks = stockService.readStockData(folderPath + code + ".csv");


        List<Double> prices = Stocks.stream().map(StockData::getPriceOfLastTransaction).collect(Collectors.toList());

        double maxPrice = stockService.findMaximumPrice(Stocks);
        double minPrice = stockService.findMinimumPrice(Stocks);

        List<String> fileNames = fileService.getFileNames();
        model.addAttribute("fileNames", fileNames);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minPrice", minPrice);

//1day
        model.addAttribute("RSId","RSI: " + technicalIndicatorService.generateRSISignal(prices, 1));
        model.addAttribute("MACDd","MACD: " + technicalIndicatorService.generateMACDSignal(prices));
        model.addAttribute("Stochastic_Oscillatord","Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 1));
        model.addAttribute("ATRd","ATR: " + technicalIndicatorService.generateATRSignal(prices, 1));
        model.addAttribute("CCId","CCI: " + technicalIndicatorService.generateCCISignal(prices, 1));

        model.addAttribute("SMAd","SMA: " + technicalIndicatorService.generateSMASignal(prices, 1));
        model.addAttribute("EMAd","EMA: " + technicalIndicatorService.generateEMASignal(prices, 1));
        model.addAttribute("WMAd","WMA: " + technicalIndicatorService.generateWMASignal(prices, 1));
        model.addAttribute("HMAd","HMA: " + technicalIndicatorService.generateHMASignal(prices, 1));
        model.addAttribute("AMAd","AMA: " + technicalIndicatorService.generateAMASignal(prices, 1));
        //1week


        model.addAttribute("RSIw","RSI: " + technicalIndicatorService.generateRSISignal(prices, 7));
        model.addAttribute("MACDw","MACD: " + technicalIndicatorService.generateMACDSignal(prices));
        model.addAttribute("Stochastic_Oscillatorw","Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 7));
        model.addAttribute("ATRw","ATR: " + technicalIndicatorService.generateATRSignal(prices, 7));
        model.addAttribute("CCIw","CCI: " + technicalIndicatorService.generateCCISignal(prices, 7));

        model.addAttribute("SMAw","SMA: " + technicalIndicatorService.generateSMASignal(prices, 7));
        model.addAttribute("EMAw","EMA: " + technicalIndicatorService.generateEMASignal(prices, 7));
        model.addAttribute("WMAw","WMA: " + technicalIndicatorService.generateWMASignal(prices, 7));
        model.addAttribute("HMAw","HMA: " + technicalIndicatorService.generateHMASignal(prices, 7));
        model.addAttribute("AMAw","AMA: " + technicalIndicatorService.generateAMASignal(prices, 7));

        //1week


        model.addAttribute("RSIm","RSI: " + technicalIndicatorService.generateRSISignal(prices, 30));
        model.addAttribute("MACDm","MACD: " + technicalIndicatorService.generateMACDSignal(prices));
        model.addAttribute("Stochastic_Oscillatorm","Stochastic Oscillator: " + technicalIndicatorService.generateStochasticSignal(prices, 30));
        model.addAttribute("ATRm","ATR: " + technicalIndicatorService.generateATRSignal(prices, 30));
        model.addAttribute("CCIm","CCI: " + technicalIndicatorService.generateCCISignal(prices, 30));

        model.addAttribute("SMAm","SMA: " + technicalIndicatorService.generateSMASignal(prices, 30));
        model.addAttribute("EMAm","EMA: " + technicalIndicatorService.generateEMASignal(prices, 30));
        model.addAttribute("WMAm","WMA: " + technicalIndicatorService.generateWMASignal(prices, 30));
        model.addAttribute("HMAm","HMA: " + technicalIndicatorService.generateHMASignal(prices, 30));
        model.addAttribute("AMAm","AMA: " + technicalIndicatorService.generateAMASignal(prices, 30));




        System.out.println(newsService.fetchNews().toString());


        newsService.fetchNews().forEach(news -> System.out.println(newsService.analyzeSentiment(news)));
        return "dashboard";
    }

}