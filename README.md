<h1 align="center">JHentai</h1>
<h6 align="center">NSFW</h6>

<p align="center">
  <img width="300" height="300" src="https://github.com/Sagiri721/JHentai/blob/master/res/logo.png">
</p>

NHentai API that uses Jsoup to get data from the NHentai website

## Information
<p style="color: rgb(255, 102, 102)">This library uses www.nhentai.to instead of www.nhentai.net
which means that a big part of the most recent doujinshis won't be available</p>

## Installation
You need to download the jar file from the version you desire to use: https://github.com/Sagiri721/JHentai/releases
It's recomended the use of Maven.

Add the file to your project files and add the dependency to your pom.xml
```xml
<dependency>
    <groupId>com.target</groupId>
    <artifactId>target</artifactId>
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/src/main/resources/JHentai.jar</systemPath>
</dependency>
```
## Features

### Data types
The preview of a book<br>
__Visible Doujinshi__
* id;
* title;
* cover;
<br><br>

The full book<br>
__Doujinshi__
* id;
* url;
* uploadDate;
* title;
* originalTitle;
* tags;
* artists;
* languages;
* categories;
* characters;
* parodies;
* groups;
* cover;
* images;  
* pages;

### Functions
Get the information of a specific doujinshi
```java
package com.mycompany.app;
import java.io.IOException;
import com.mycompany.app.format.Doujinshi;

public class App 
{
    public static void main( String[] args )
    {
        try {

            Doujinshi doujin = Reader.getDoujinshiFromId(177013);

            System.out.println(doujin.id);  //177013
            System.out.println(doujin.title);   //[ShindoLA] METAMORPHOSIS (Complete) [English]
            System.out.println(doujin.pages);   //225

        } catch (IOException e) {e.printStackTrace();}
    }
}
```
Get the Visible doujinshis from a search.<br>
The arguments call for: "Keyword", number_of_results (clamped between 0 and 50), number_of_the_page
```java
package com.mycompany.app;
import java.io.IOException;
import java.util.List;
import com.mycompany.app.format.VisibleDoujin;

public class App 
{
    public static void main( String[] args )
    {
        try {

            List<VisibleDoujin> doujin = Reader.getSearchResults("isekai", 50, 2);

        } catch (IOException e) {e.printStackTrace();}
    }
}
```

The other functions are rather intuitive

## Save a doujinshi has a JSON
The class has a function that returns a JSONObject of the class, that you can write to a file if needed.
```java
Doujinshi doujin = Reader.getRandomDoujinshi();

JSONObject obj = doujin.saveDoujinshiAsJson();

try (FileWriter file = new FileWriter("fileName.json")) {

    file.write(obj.toString()); // data is a JSON string here
    file.flush();

  } catch (IOException e) {
    e.printStackTrace();
  }
```
Output:
```json
{
    "original-title":"[ポン貴花田] にーづまお背中流します 1 第1-8話 [英訳]",
    "images":(...)
        "languages":["english","translated"],
        "upload-date":"2020-07-30T06:12",
        "groups":[],
        "title":"[Pon Takahanada] Niizuma Osenaka Nagashimasu 1 Ch. 1-8 [English] [HappyMerchants]",
        "url":"https://www.nhentai.to/g/146997",
        "tags":["condom","dilf","old","man","full","censorship","story","arc","big","breasts","bikini","dark","skin","kimono","milf","netorare","prostitution","swimsuit","incest","gyaru","mother","inseki"],
        "cover":"https://cdn.nload.xyz/galleries/864378/cover.jpg",
        "characters":[],
        "parodies":[],
        "pages":174,
        "artists":["pon","takahanada"],
        "id":146997,
        "categories":["manga"]
}
```

## Images
All the pages of the book are saved in the List of strings images.
The cover is saved seperately in the variable cover.

What is saved is not a BufferedImage but a url.

To display the cover on a Javax.Swing app you can do it, for example, with a JLabel, with  the following code:
```java
URL url = new URL(doujin.cover);
image = ImageIO.read(url);
label.setIcon(new ImageIcon(image));
```
