package com.lvhuong.myproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WebScraper {

   public static void main(String[] args) throws IOException {
      Document doc = Jsoup.connect("https://fategrandorder.fandom.com/wiki/Servant_List_by_ID").get();
      WebScraper dba = new WebScraper();
      ArrayList<Integer> starList = new ArrayList<>(240);
      ArrayList<String> nameList = new ArrayList<>(240);

      starList = dba.getStarList(doc);
      nameList = dba.getNameList(doc);

/*
      for(int i=0;i<starList.size();i++){
         System.out.println(i+". "+starList.get(i)+" star: "+nameList.get(i));
      }*/

      InsertData dbc = new InsertData();
      //dbc.insertCardTable();
      dbc.insertServantTable(nameList, starList);
   }
   //Made to download specific images from a url, specific element is chosen with doc.select(...)
   public void downloadImages(Document doc) throws IOException {
      /*
      Go to https://jsoup.org/cookbook/ -> Extracting Data for documentation
       */
      Elements links = doc.select("td > a.image.image-thumbnail.link-internal > img");
      int count =0;
      String filePath;
      for(Element url: links) {
         String string = url.attr("abs:src");
         if(string.equals(""))string = url.attr("data-src");
         if(!url.attr("data-image-key").equals("Gift_Icon.png")) {// remove cases like Limited, Story locked... icons
            count++;
            if(count == 82){
               count--;
               filePath = "E:\\fgosimres\\Summon_cards\\ServantCard_"+count+"-5.png";
            } else
               filePath = "E:\\fgosimres\\Summon_cards\\ServantCard_"+count+".png";
            //Open a URL Stream
            Connection.Response resultImageResponse = Jsoup.connect(string)
                    .ignoreContentType(true).execute();
            // output here
            FileOutputStream out = (new FileOutputStream(new java.io.File(filePath)));
            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
            out.close();
         }
      }
   }
   public ArrayList<String>  getNameList(Document doc){
      /*
      Go to https://jsoup.org/cookbook/ -> Extracting Data for documentation
       */
      Elements sName = doc.select("table > tbody > tr > td + td > a:first-child");
      int count =0, nameInd=0;
      boolean flag = false;
      ArrayList<String> nameList = new ArrayList<>(240);
      for(Element url: sName){
         if(nameInd == 80 && flag == false) {
            flag = true;
         }else{
            String name = url.attr("title");
            nameList.add(name);
            nameInd++;
         }
      }
      return nameList;
   }

   public ArrayList<Integer> getStarList(Document doc){
      /*
      Go to https://jsoup.org/cookbook/ -> Extracting Data for documentation
       */
      Elements links = doc.select("table > tbody > tr > td:nth-child(3)");
      int count=0, starInd =0;
      boolean flag = false;
      ArrayList<Integer> starList = new ArrayList<>(240);
      for(Element url: links) {
         count++;
         if(starInd == 80 && flag == false) {
            flag = true;
         }else {
            String string = url.text();
            starList.add(string.replace(" ", "").length());
            starInd++;
         }
      }
      return starList;
   }
}
