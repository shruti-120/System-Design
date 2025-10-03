package lld_practice.url_shortner;

import java.util.HashMap;
import java.util.Map;

class UrlShortner {
    static Long id = 0L;
    String myUrl = "https://short.ly/";
    Map<String, String> urlMap = new HashMap<>();

    public String shortenUrl(String url) {
        id = ++id;
        String shortUrlKey = convertToBase62(id);
        String shortUrl = myUrl+shortUrlKey;
        urlMap.put(shortUrl, url);
        return shortUrl;
    }
    public String convertToBase62(Long number) {
//        long num = id;
//        StringBuilder key = new StringBuilder();
//        HashMap<Integer, String> map = new HashMap<>();
//        map.put(10, "a"); map.put(11, "b"); map.put(12, "c"); map.put(13, "d"); map.put(14, "e");map.put(15, "f");
//        map.put(16, "A"); map.put(17, "B"); map.put(18, "C"); map.put(19, "D"); map.put(20, "E");map.put(21, "F");
//        while(num > 0) {
//            int remainder = (int)(num % 22);
//            if(remainder <= 9) key.append(String.valueOf(remainder));
//            else key.append(map.get(remainder));
//            num = num / 22;
//        }
//        return key.toString();
        final String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        while(number > 0) {
            sb.append(characters.charAt((int) (number % characters.length())));
            number /= characters.length();
        }
        return sb.reverse().toString();
    }
    public String expandUrl(String shortUrl) {
        if(!urlMap.containsKey(shortUrl)) System.out.println("URL not found!");
        return urlMap.get(shortUrl);
    }
}
public class Main {
    public static void main(String[] args) {
        UrlShortner u = new UrlShortner();
        String shortUrl = u.shortenUrl("https://www.example.com/articles/2025/09/03/how-to-build-a-url-shortener");
        System.out.println("shortend URL: "+shortUrl);
        shortUrl = u.shortenUrl("https://www.example.com/articles/2025/09/03/check-my-page");
        System.out.println("shortend URL: "+shortUrl);
        String originalUrl = u.expandUrl("https://short.ly/1");
        System.out.println("Original URL: "+originalUrl);
    }
}
