package mk.finki.ukim.mk.dasproject.service.implementation;



import mk.finki.ukim.mk.dasproject.model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {


    public List<News> fetchNews() {
        List<News> newsList = new ArrayList<>();
        try {

            Document doc = Jsoup.connect("https://www.mse.mk/mk/news/latest").get();


            Element newsContent = doc.getElementById("news-content");


            Elements rows = newsContent.select("div.row");


            for (int i = 0; i < rows.size(); i += 2) {
                if (i + 1 < rows.size()) {
                    String title = rows.get(i).text();
                    String content = rows.get(i + 1).text();
                    newsList.add(new News(title, content));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    // Метод за анализа на сентиментот
    public String analyzeSentiment(News news) {
        if (news.getContent().toLowerCase().contains("increase") || news.getContent().toLowerCase().contains("positive")) {
            return "Позитивно";
        } else if (news.getContent().toLowerCase().contains("decrease") || news.getContent().toLowerCase().contains("negative")) {
            return "Негативно";
        }
        return "Неутрално";
    }
}
