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
        String temp;
        try {  //director
            temp = null;
            String comma;
            for (Element e : doc2.selectFirst("th:contains(directed by)").nextElementSibling().select("li, a")) {
                comma = temp.equals("") ? "" : ", ";
                temp = temp + comma + e.text();
            }
        } catch (Exception e) {
            temp = "/";
        }
        movie.setDirector(temp);

        try { //actors
            temp = "";
            String comma;
            for (Element e : doc2.selectFirst("th:contains(starring)").nextElementSibling().select("li, a")) {
                comma = temp.equals("") ? "" : ", ";
                temp = temp + comma + e.text();
            }
        } catch (Exception e) {
            temp = "/";
        }
        movie.setMainActors(temp);

        try { // Release date
            temp = doc2.selectFirst("th:contains(Release date)").nextElementSibling().selectFirst("td").text();
        } catch (Exception e) {
            temp = "/";
        }
        // not used

        try { //Language
            temp = doc2.selectFirst("th:contains(Language)").nextElementSibling().text();
//            temp = "";
//            String comma;
//            for (Element e : doc2.selectFirst("th:contains(Language)").nextElementSibling().select("a")){
//                comma = temp.equals("" )? "" : ", ";
//                temp = temp + comma + e.text();}
        } catch (Exception e) {
            temp = "/";
        }
        movie.setLanguage(temp);

        try { //description
            temp = doc2.selectFirst("div.mw-parser-output").selectFirst("p").text();
        } catch (Exception e) {
            temp = "/";
        }
        if (temp.length() > 400) {
            temp = temp.substring(0, 397) + "...";
        }
        movie.setDescription(temp);
//        for (Element t : div) {
//            Elements rows = t.select("tbody tr");
//            for (Element r : rows) {
//                Elements cells = r.select("td");
//                if (cells.size() > 0) {
//                    Elements year = r.select("th");
//                    System.out.print(year.get(0).text() + ",");
//                    System.out.println("\"" + cells.get(0).text() + "\"");
//                }
//            }
//        }
        return movie;
    }
}
