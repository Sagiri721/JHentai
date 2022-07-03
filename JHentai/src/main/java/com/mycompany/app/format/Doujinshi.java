package com.mycompany.app.format;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

public class Doujinshi {
    
    //Doujinshi data
    public Integer id;
    public String url;

    public LocalDateTime uploadDate;

    public String title = "", originalTitle = "";

    public String[] tags;
    public String[] artists;
    public String[] languages;
    public String[] categories;
    public String[] characters;
    public String[] parodies;
    public String[] groups;

    public String cover = null;
    public List<String> images = new ArrayList<String>();  

    public int pages = 0;

    public Doujinshi() {}

    public void setUrl(){

        this.url = id==null ? null : "https://www.nhentai.to/g/" + this.id;
    }

    public long daysSinceRelease(){

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(uploadDate, now);

        return duration.toDays();
    }

    public JSONObject saveDoujinshiAsJson(){

        JSONObject object = new JSONObject();

        object.put("id", id);
        object.put("url", url);
        object.put("title", title);
        object.put("original-title", originalTitle);
        object.put("upload-date", uploadDate.toString());
        object.put("cover", cover);
        object.put("pages", pages);

        object.put("tags", tags);
        object.put("artists", artists);
        object.put("languages", languages);
        object.put("characters", characters);
        object.put("categories", categories);
        object.put("parodies", parodies);
        object.put("groups", groups);

        object.put("images", images);

        return object;
    }
}
