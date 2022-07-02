package com.mycompany.app;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mycompany.app.format.Doujinshi;
import com.mycompany.app.format.VisibleDoujin;

final class Reader {
    
    private static final String url = "https://www.nhentai.to";
    static Document document;

    //Single doujinshi related functions

    public static Doujinshi getDoujinshiFromId(int id) throws IOException {

        if(!doujinshiExists(id)) return null;

        //The requested doujin
        Doujinshi doujin = new Doujinshi();
        doujin.id = id;
        doujin.setUrl();

        document = Jsoup.connect(url + "/g/" + id).get();

        //Title and original title
        doujin.title = document.select("h1").get(1).text();
        doujin.originalTitle = document.selectFirst("h2").text();

        Elements infoblock = document.select(".tag-container");
        doujin.parodies = trimmToArray(infoblock.get(0).text());
        doujin.characters = trimmToArray(infoblock.get(1).text());
        doujin.tags = trimmToArray(infoblock.get(2).text());
        doujin.artists = trimmToArray(infoblock.get(3).text());
        doujin.groups = trimmToArray(infoblock.get(4).text());
        doujin.languages = trimmToArray(infoblock.get(5).text());
        doujin.categories = trimmToArray(infoblock.get(6).text());

        //Upload date
        Elements time = document.select("time");
        doujin.uploadDate = convertToDate(time.first().text());

        //Find the pages
        Elements page = document.select("div");
        for(Element e : page)
            try{ 
                if(e.text().split(" ")[1].equals("pages")) doujin.pages = Integer.valueOf(e.text().split(" ")[0]);
            }catch(Exception ignored){}
        
        //Get cover and images

        Elements images = document.select("img");
        
        for(Element e : images){

            String imgUrl = e.attr("src");

            if(doujin.cover==null && imgUrl.contains("cover")) doujin.cover=imgUrl;

            if(doujin.cover!=null && imgUrl!="" && !imgUrl.contains("cover") && !imgUrl.contains(".gif")){

                //Add the images
                doujin.images.add(imgUrl);
            }
        }

        return doujin;
    }

    public static Doujinshi getRandomDoujinshi(){

        try {

            Random random = new Random();
            int num = random.nextInt(getLastDoujinNumber());

            if(doujinshiExists(num))
                return getDoujinshiFromId(num);
            else 
                return null;

        } catch (IOException e) { e.printStackTrace(); return null; }
    }

    public static int getLastDoujinNumber() throws IOException{
        
        return getRecentVisibleDoujins(1).get(0).id;
    }

    public static List<VisibleDoujin> getRecentVisibleDoujins(int bound) throws IOException{

        if(bound<0||bound>50)   return null;

        List<VisibleDoujin> list = new ArrayList<VisibleDoujin>();

        document = Jsoup.connect(url).get();

        Elements links = document.select("a");

        int count = 0;
        for(Element e : links){

            String linkAttribute = e.attr("href");
            if(linkAttribute.contains("/g/")){

                if(count==bound) break;

                //It's a doujin
                VisibleDoujin doujin = new VisibleDoujin();

                String id = linkAttribute.split("/")[2];
                doujin.id = Integer.valueOf(id);
                
                list.add(doujin);
                count++;
            
                //Get the cover
                doujin.cover = e.select("img").attr("src");

                //Get title
                doujin.title = e.select("div").text();
            }
        }

        return list;
    }

    public static boolean doujinshiExists(int id){

        try {

            Jsoup.connect(url + "/g/" + id).timeout(10000).execute();
            return true;

        } catch (IOException e) {

            return false;
        }
    }

    private static String[] trimmToArray(String text){

        String[] original = text.split(" ");
        String[] arr = new String[original.length-1];

        for(int i = 0; i < arr.length; i++){ arr[i] = original[i+1]; }

        return arr;
    }

    private static LocalDateTime convertToDate(String base){

        String[] separtion = base.split(" ");

        String[] portions = separtion[1].split(":");
        String[] aux = separtion[0].split("/");

        int hour = Integer.valueOf(portions[0]);
        if(separtion[2].equals("PM")) hour+=12;

        LocalDateTime date = LocalDateTime.of(Integer.valueOf(aux[2]), Integer.valueOf(aux[1]), Integer.valueOf(aux[0]), hour, Integer.valueOf(portions[1]));

        return date;
    }

    //Search results related functions

    public static List<VisibleDoujin> getSearchResults(String search, int bound){
        
        if(bound<0 || bound>50) return null;

        return null;
    }
}
