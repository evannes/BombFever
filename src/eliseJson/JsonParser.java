package eliseJson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for parsing JSON.
 * Created by Elise Haram Vannes on 17.07.2017.
 */
public class JsonParser {

    // private standard constructor that prevents objects being created of this class
    private JsonParser(){}

    /**
     * Creates a JSON-array in a String.
     * @param list  the 2D-list that will be converted
     * @return      the String containing the JSON-array
     */
    public static String create2DJsonArray(List<List<String>> list){
        StringBuilder jsonArray = new StringBuilder();

        for(int i = 0; i < list.get(0).size(); i++){
            jsonArray.append("{");
            for(int j = 0; j < list.size(); j++){

                jsonArray.append("\"").append(list.get(j).get(i)).append("\"");

                if(j  == list.size()-1) {
                    jsonArray.append("}");
                }

                if(!(i == list.get(0).size()-1 && j == list.size()-1)){
                    jsonArray.append(",");
                }
            }

        }
        return jsonArray.toString();
    }

    /**
     * Reads a JSON-array of this particular type, and converts it to a 2D-List containing Strings.
     * @param fullText  the text containing the whole JSON-array
     * @return          returns the text converted to a 2D-List
     */
    public static List<List<String>> readJsonArray(String fullText){

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