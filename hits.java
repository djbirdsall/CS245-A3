import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.util.*;

public class hits{

    public static void main(String args[]) throws IOException{
        Scanner myScan = new Scanner(System.in);
        ArrayList<Artist> artists = new ArrayList<Artist>();
        Document document = Jsoup.connect("https://top40weekly.com/all-us-top-40-singles-for-2020/#").get();
        Elements section = document.select("div.x-text");
        List<String> sList = new ArrayList<String>();
        String breaker = "–•– ";
        for(Element e : section.select("p")){
            String temp = e.toString();
            while (temp.contains(breaker)){
                String subber = temp.substring(temp.indexOf(breaker) + 4, temp.indexOf(" (",temp.indexOf(breaker)+4)); //this are is creating a sub string of just the artists
                temp = temp.substring(temp.indexOf(breaker)+4,temp.lastIndexOf("</p>")+4);
                if(subber.contains("&")||subber.contains("with")||subber.contains("featuring")){
                    if(subber.contains("His Orchestra")) //issue being addressed
                        subber = subber.replace("His Orchestra","");
                    sList.add(subber);
                }
            }
        }
        List<String> binList = new ArrayList<>(new HashSet<>(sList)); //to get rid of duplicates of artist collaborations
        for (int i = 0; i < binList.size(); i++) {
            String[] aColl = binList.get(i).split(" featuring | with |, | &amp; | Featuring ");
            for (int j = 0; j < aColl.length; j++) { //this is manually correcting some typos on the website
                if(aColl[j].contains("Justin Bieber"))
                    aColl[j] = "Justin Bieber";
                else if(aColl[j].contains("Roddy Ricch"))
                    aColl[j] = "Roddy Ricch";
                else if(aColl[j].contains("Jeremih"))
                    aColl[j] = "Jeremih";
                else if(aColl[j].contains("Gunna"))
                    aColl[j] = "Gunna";
                else if(aColl[j].contains("Tyga"))
                    aColl[j] = "Tyga";
                else if(aColl[j].contains("DaBaby"))
                    aColl[j] = "DaBaby";
                else if(aColl[j].contains("YoungBoy Never Broke Again"))
                    aColl[j] = "YoungBoy Never Broke Again";
                else if(aColl[j].contains("Drake"))
                    aColl[j] = "Drake";
                else if(aColl[j].contains("Blake Shelton"))
                    aColl[j] = "Blake Shelton";
                else if(aColl[j].contains("Cardi B"))
                    aColl[j] = "Cardi B";
            }
            for (int j = 0; j < aColl.length; j++) { //creating the artist as an object and giving it its ID / name.
                if(Artist.aExists(aColl[j], artists)==-1){
                    artists.add(new Artist(artists.size(), aColl[j]));
                }
            }
        }
        int[][] artistIDs = new int[artists.size()][artists.size()]; //creating graph with length of amount of artists that have features to compare.
        for (int i = 0; i < binList.size(); i++) {
            String[] aColl = binList.get(i).split(" featuring | with |, | &amp; | Featuring "); //separating featured artists
            for (int j = 0; j < aColl.length; j++) { //this is manually correcting some typos on the website
                if(aColl[j].contains("Justin Bieber"))
                    aColl[j] = "Justin Bieber";
                else if(aColl[j].contains("Roddy Ricch"))
                    aColl[j] = "Roddy Ricch";
                else if(aColl[j].contains("Jeremih"))
                    aColl[j] = "Jeremih";
                else if(aColl[j].contains("Gunna"))
                    aColl[j] = "Gunna";
                else if(aColl[j].contains("Tyga"))
                    aColl[j] = "Tyga";
                else if(aColl[j].contains("DaBaby"))
                    aColl[j] = "DaBaby";
                else if(aColl[j].contains("YoungBoy Never Broke Again"))
                    aColl[j] = "YoungBoy Never Broke Again";
                else if(aColl[j].contains("Drake"))
                    aColl[j] = "Drake";
                else if(aColl[j].contains("Blake Shelton"))
                    aColl[j] = "Blake Shelton";
                else if(aColl[j].contains("Cardi B"))
                    aColl[j] = "Cardi B";
            }
            for (int j = 0; j < aColl.length; j++) { //marking that artists have collaborated
                if(Artist.aExists(aColl[j], artists)!=-1){
                    for (int k = 0; k < aColl.length; k++) {
                        if(k==j);
                        else if(Artist.aExists(aColl[k],artists)!=-1) {
                            artistIDs[Artist.aExists(aColl[j], artists)][Artist.aExists(aColl[k],artists)] = 1;
                        }
                    }
                }
            }
        }
        boolean pickFake = true;
        String userPick = null;
        while(pickFake) { //getting user input
            System.out.print("Enter an Artist: ");
            userPick = myScan.nextLine();
            if (Artist.aExists(userPick, artists) == -1)
                System.out.println("Artist does not exist.");
            else
                pickFake = false;
        }
        HashSet<String> iMatches = new HashSet<String>(); //for no duplicates
        HashSet<String> nMatches = new HashSet<>(iMatches); //for no duplicates with original list on collaborated artists' lists
        for (int i = 0; i < artistIDs[Artist.aExists(userPick, artists)].length; i++) {
            if(artistIDs[Artist.aExists(userPick, artists)][i]==1){
                iMatches.add(artists.get(i).getName());
            }
        }
        Object[] binMatches = iMatches.toArray();
        System.out.println(userPick+" collaborated with: ");
        for (int i = 0; i < binMatches.length; i++) {
            System.out.println(" ● "+binMatches[i]);
        }
        for (int i = 0; i < binMatches.length; i++) {
            for(int j=0;j<artistIDs[Artist.aExists((String) binMatches[i], artists)].length;j++){
                if(artistIDs[Artist.aExists((String) binMatches[i],artists)][j]==1) {
                    nMatches.add(artists.get(j).getName());
                }
                else{
                    nMatches.remove(artists.get(j).getName());
                }
                nMatches.remove(userPick);
            }
            Object[] noobMatches = nMatches.toArray();
            if(nMatches.size()>0) {
                System.out.println("... as well as collaborators with " + binMatches[i] + ":");
                for (int k = 0; k < noobMatches.length; k++) {
                    System.out.println(" ● " + noobMatches[k]);
                }
            }
            nMatches = new HashSet<>(iMatches);;
        }
    }
    static class Artist{
        int ID = 0;
        String name = null;
        public Artist(int id, String Name) {
            ID = id;
            name = Name;
        }
        public String getName(){
            return this.name;
        }
        public static int aExists(String Name, ArrayList<Artist> artists) { //checking if artist exists in array list and returns where they exist.
            for (int i = 0; i < artists.size(); i++) {
                if(artists.get(i).getName().equals(Name))
                    return i;
            }
            return -1;
        }
    }
}