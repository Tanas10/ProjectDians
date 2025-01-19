package mk.finki.ukim.mk.dasproject.service.implementation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import mk.finki.ukim.mk.dasproject.model.StockData;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
    private final ResourceLoader resourceLoader;


    public StockService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public List<StockData> readStockData(String filePath) {
        List<StockData> stocks = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            reader.skip(1); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                StockData stock = new StockData();
                if (nextLine[0] != null && !nextLine[0].trim().isEmpty()) {
                    stock.setDate(nextLine[0]);
                } else {
                    stock.setDate("");
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[1] != null && !nextLine[1].trim().isEmpty()) {
                    stock.setPriceOfLastTransaction(Double.parseDouble(nextLine[1].replace(".", "").replace(",", ".")));
                } else {
                    stock.setPriceOfLastTransaction(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[2] != null && !nextLine[2].trim().isEmpty()) {
                    stock.setMax(Double.parseDouble(nextLine[2].replace(".", "").replace(",", ".")));
                } else {
                   stock.setMax(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[3] != null && !nextLine[3].trim().isEmpty()) {
                    stock.setMin(Double.parseDouble(nextLine[3].replace(".", "").replace(",", ".")));
                } else {
                    stock.setMin(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[4] != null && !nextLine[4].trim().isEmpty()) {
                    stock.setAveragePrice(Double.parseDouble(nextLine[4].replace(".", "").replace(",", ".")));
                } else {
                    stock.setAveragePrice(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[5] != null && !nextLine[5].trim().isEmpty()) {
                    stock.setPercentProm(Double.parseDouble(nextLine[5].replace(".", "").replace(",", ".")));
                } else {
                    stock.setPercentProm(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[6] != null && !nextLine[6].trim().isEmpty()) {
                    stock.setQuantity(Double.parseDouble(nextLine[6]));
                } else {
                    stock.setQuantity(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[7] != null && !nextLine[7].trim().isEmpty()) {
                    stock.setBestTurnoverInDenars(Integer.parseInt(nextLine[7].replace(".", "")));
                } else {
                    stock.setBestTurnoverInDenars(0);
                    //  continue; //if you want to skip it compliantly
                }
                if (nextLine[8] != null && !nextLine[8].trim().isEmpty()) {
                    stock.setTotalTurnoverInDenars(Integer.parseInt(nextLine[8].replace(".", "")));
                } else {
                    stock.setTotalTurnoverInDenars(0);
                    //  continue; //if you want to skip it compliantly
                }



                stocks.add(stock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }



        public double findMaximumPrice(List<StockData> stocks) {
            if (stocks == null || stocks.isEmpty()) {
                throw new IllegalArgumentException("The stock data list cannot be null or empty.");
            }

            double maxPrice = stocks.get(0).getMax();

            for (StockData stock : stocks) {
                if (stock.getMax() > maxPrice) {
                    maxPrice = stock.getMax();
                }
            }

            return maxPrice;
        }
        public double findMinimumPrice(List<StockData> stocks) {
            if (stocks == null || stocks.isEmpty()) {
                throw new IllegalArgumentException("The stock data list cannot be null or empty.");
            }

            double minPrice = stocks.get(0).getMin();

            for (StockData stock : stocks) {
                if (stock.getMin() > minPrice) {
                    minPrice = stock.getMin();
                }
            }

            return minPrice;
        }


    public List<StockData> readStockDatabyFileName(String fileName) {
        List<StockData> stocks = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:python/database/" + fileName + ".csv");

        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            String[] nextLine;
            reader.skip(1); // Skip the header line

            while ((nextLine = reader.readNext()) != null) {
                StockData stock = new StockData();

                stock.setDate(nextLine[0] != null ? nextLine[0] : "");
                stock.setPriceOfLastTransaction(parseDouble(nextLine[1]));
                stock.setMax(parseDouble(nextLine[2]));
                stock.setMin(parseDouble(nextLine[3]));
                stock.setAveragePrice(parseDouble(nextLine[4]));
                stock.setPercentProm(parseDouble(nextLine[5]));
                stock.setQuantity(parseDouble(nextLine[6]));
                stock.setBestTurnoverInDenars(parseInteger(nextLine[7]));
                stock.setTotalTurnoverInDenars(parseInteger(nextLine[8]));

                stocks.add(stock);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return stocks;
    }

    private double parseDouble(String value) {
        if (value != null && !value.trim().isEmpty()) {
            return Double.parseDouble(value.replace(".", "").replace(",", "."));
        }
        return 0.0;
    }

    private int parseInteger(String value) {
        if (value != null && !value.trim().isEmpty()) {
            return Integer.parseInt(value.replace(".", ""));
        }
        return 0;
    }

}
