package main.util;

import main.model.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ScrapingUtil {

    public static void main(String[] args) throws IOException {
        System.out.println(scrapMovieInfo("harry potter"));
    }

    public static Movie scrapMovieInfo(String movieName) throws IOException {
        Movie movie = new Movie();
        Document doc = Jsoup
                .connect("https://en.wikipedia.org/w/index.php?search=" + movieName + "+film")
                .get();
        Elements div = doc.select("div.mw-search-result-heading");
//        System.out.println(div.get(0).siblingElements().get(0));
        String link;
        try {
            link = "https://en.wikipedia.org" + div.select("a[href]").get(0).attr("href");
        } catch (Exception e) {
            return movie;
        }
        Document doc2 = Jsoup
                .connect(link)
                .get();
        StringBuilder temp;
        try {  //director
            temp = new StringBuilder();
            String comma;
            for (Element e : doc2.selectFirst("th:contains(directed by)").nextElementSibling().select("li, a")) {
                comma = temp.length()==0 ? "" : ", ";
                temp.append(comma).append(e.text());
            }
        } catch (Exception e) {
            temp = new StringBuilder("/");
        }
        movie.setDirector(temp.toString());

        try { //actors
            temp.delete(0,temp.length());
            String comma;
            for (Element e : doc2.selectFirst("th:contains(starring)").nextElementSibling().select("li, a")) {
                comma = temp.length()==0 ? "" : ", ";
                temp.append(comma).append(e.text());
            }
        } catch (Exception e) {
            temp = new StringBuilder("/");
        }
        movie.setMainActors(temp.toString());

        try { //Language
            temp = new StringBuilder(doc2.selectFirst("th:contains(Language)").nextElementSibling().text());
        } catch (Exception e) {
            temp = new StringBuilder("/");
        }
        movie.setLanguage(temp.toString());

        try { //description
            temp = new StringBuilder(doc2.selectFirst("div.mw-parser-output").selectFirst("p").text());
        } catch (Exception e) {
            temp = new StringBuilder("/");
        }
        if (temp.length() > 400) {
            temp = new StringBuilder(temp.substring(0, 397) + "...");
        }
        movie.setDescription(temp.toString());
        return movie;
    }
}
