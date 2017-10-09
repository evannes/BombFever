package eliseJson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for parsing JSON.
 * Created by Elise Haram Vannes on 17.07.2017.
 */
public class JsonParser { // Statiske metoder? Kun kalle på JsonParser.create2DJsonArray()
                            // istedenfor å opprette objekt?

    // privat standard-konstrukør hindrer opprettelse av objekt av denne klassen
    private JsonParser(){}

    /*
    Programmeringsregel:  En metode som kan utføre oppgaven sin ved hjelp av den informasjonen
    den får gjennom sine parameterverdier (og ikke noe annet), kan og skal settes opp (deklareres)
    som statisk. Den er «seg selv nok».
     */

    public static String create2DJsonArray(List<List<String>> list){

        String jsonArray = "";

        for(int i = 0; i < list.get(0).size(); i++){
            jsonArray += "{";
            for(int j = 0; j < list.size(); j++){

                jsonArray += "\"" + list.get(j).get(i) + "\"";

                if(j  == list.size()-1) {
                    jsonArray += "}";
                }

                if(!(i == list.get(0).size()-1 && j == list.size()-1)){
                    jsonArray += ",";
                }
            }

        }

        //System.out.println(jsonArray);
        return jsonArray;
    }

    public static List<List<String>> readJsonArray(String fullText){ // legge inn int itemsPerBolk ?
        // regEx: \{(.*?)\} henter ut alt (unntatt newlines) mellom { og }
        // \{([^]+)\}   som over, men med new lines

        // \"([^,{}"]+)\"    plukker ut det som er mellom "" men uten komma eller {}, så "hei","hade" blir delt i to
        // bruker deretter ([^,{}"]+)   for å hente ut teksten UTEN ""
        // finnes en annen lettere måte  String.split("); ??

        ArrayList<String> firstDivider = new ArrayList<>();

        Pattern regexDivideObjects = Pattern.compile("\\{(.*?)\\}",Pattern.DOTALL);
        Matcher regexMatcher = regexDivideObjects.matcher(fullText);

        while(regexMatcher.find()){
            firstDivider.add(regexMatcher.group());
        }

        List<List<String>> secondDivider = new ArrayList<>();

        Pattern regexDivideItems = Pattern.compile("\\\"([^,{}\"]+)\\\"");

        for(int i = 0; i < firstDivider.size(); i++) {
            String currentObject = firstDivider.get(i);
            Matcher regexItemsMatcher = regexDivideItems.matcher(currentObject);
            int itemsPerPost = 0;
            while(regexItemsMatcher.find()){
                if(i == 0) {
                    secondDivider.add(new ArrayList<>());
                }
                secondDivider.get(itemsPerPost).add(regexItemsMatcher.group());
                itemsPerPost++;
            }
        }

        for(int i = 0; i < secondDivider.get(0).size(); i++){
            for(int j = 0; j < secondDivider.size(); j++){

                String s = secondDivider.get(j).get(i);
                String[] t = s.split("\"");
                secondDivider.get(j).set(i,t[1]);
            }
        }
        return secondDivider;
    }
}