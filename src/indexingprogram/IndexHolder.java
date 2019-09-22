/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexingprogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.security.ssl.Debug;

/**
 *
 * @author 1
 */
public class IndexHolder {

    private HashMap<String, HashMap<String, List<WordDetail>>> totalFileMap;

    // private HashMap<String, List<WordDetail>> currentFileMap;
    public IndexHolder() {
        totalFileMap = new HashMap<String, HashMap<String, List<WordDetail>>>();
    }

    public void AddIndex(String fileName, String word, WordDetail wordPosDetails) {
        HashMap<String, List<WordDetail>> currentFileMap;
        if (totalFileMap.containsKey(fileName)) {
            currentFileMap = totalFileMap.get(fileName);
        } else {
            currentFileMap = new HashMap<String, List<WordDetail>>();
            totalFileMap.put(fileName, currentFileMap);
        }

        if (currentFileMap.containsKey(word)) {
            List<WordDetail> intg = currentFileMap.get(word);//get the assigned integer list
            intg.add(wordPosDetails);
        } else {
            List<WordDetail> intg = new ArrayList<WordDetail>();
            intg.add(wordPosDetails);
            currentFileMap.put(word, intg);
        }
    }

    HashMap<String, List<WordDetail>> foundKeyWordPlaces = new HashMap<String, List<WordDetail>>();//Stores file name with 

    ///Checks for the phrase if exists
    public List<Object> CheckForPhrase(String phrase) {
        foundKeyWordPlaces.clear();

        HashMap<String, List<WordDetail>> currentFileMap;
        List<WordDetail> indexofFileWhereFoundContValues;

        String fileNameKey;
        String[] s = phrase.split(" ");

        List<Integer> continousWordsFound = new ArrayList<Integer>();
        List<Integer> toremoveFromFoundIndex = new ArrayList<Integer>();
        
        List<WordDetail> val;
        List<WordDetail> val1;
                
        ArrayList tempArr = null;
        
        if (s == null || s.length == 0) {
            return Arrays.asList(false);
        } else if (s.length == 1) {
            for (Map.Entry<String, HashMap<String, List<WordDetail>>> entry : totalFileMap.entrySet()) {//iterate through parent hasmap table
                fileNameKey = entry.getKey();
                currentFileMap = entry.getValue();
                if (currentFileMap.containsKey(s[0])) {
                    tempArr = new ArrayList();
                    tempArr.add(true); //Returns phrase as found with all the indexes where it was found
                    List<WordDetail> mappedValues = currentFileMap.get(s[0]);

                    indexofFileWhereFoundContValues = new ArrayList();
                    for (int i = 0; i < mappedValues.size(); i++) {
                        indexofFileWhereFoundContValues.add(mappedValues.get(i));
                    }
                    foundKeyWordPlaces.put(fileNameKey, indexofFileWhereFoundContValues);
                    tempArr.add(foundKeyWordPlaces);

                }
            }
            if(tempArr != null) return tempArr;
            return Arrays.asList(false);
        } else {
            tempArr = new ArrayList();
            tempArr.add(true); //Returns phrase as found with all the indexes where it was found

            for (Map.Entry<String, HashMap<String, List<WordDetail>>> entry : totalFileMap.entrySet()) {
                fileNameKey = entry.getKey();
                currentFileMap = entry.getValue();
                //for a word to be consecutive current index should be in ascending order
                //ie current index + 1 = nextindex value
                //and should go on till the then of the phrase
                if (currentFileMap.containsKey(s[0]) && currentFileMap.containsKey(s[1])) {//get first and second word index count
                    //if they do then proceed to find next word match
                    val = currentFileMap.get(s[0]);
                    val1 = currentFileMap.get(s[1]);

                    for (int j = 0; j < val.size(); j++) {//match the first word and second word indexes and get the list of values where they are ascending order
                        for (int k = 0; k < val1.size(); k++) {
                            if (val.get(j).getIndex() + 1 == val1.get(k).getIndex()) {
                                continousWordsFound.add(j);
                            }
                        }
                    }

                    //use the ascending order values from above to determine which index has 
                    //ascending order  by looping through indexs of words in phrase other then the above two
                    //first words indexes are kept which has ascending order for words till the final phrase where all the words are iterated
                    for (int i = 2; i < s.length; i++) {//check if words exist in the maps
                        if (!currentFileMap.containsKey(s[i])) {
                            return Arrays.asList(false);
                        } else if (continousWordsFound.size() == 0) {
                            return Arrays.asList(false);
                        } else {
                            val1 = currentFileMap.get(s[i]);

                            for (int j = 0; j < continousWordsFound.size(); j++) {
                                int valueToTest = continousWordsFound.get(j);
                                boolean matched = false;

                                for (int k = 0; k < val1.size(); k++) {
                                    if (val.get(valueToTest).getIndex() + i == val1.get(k).getIndex()) {
                                        matched = true;
                                    }
                                }

                                if (!matched) {
                                    toremoveFromFoundIndex.add(valueToTest);
                                }
                            }

                            if (!toremoveFromFoundIndex.isEmpty()) {//remove the values from index which are not in ascending order
                                for (int j = 0; j < toremoveFromFoundIndex.size(); j++) {
                                    continousWordsFound.remove(toremoveFromFoundIndex.get(j));
                                }
                            }
                            toremoveFromFoundIndex.clear();//clear remove index
                        }
                    }
                    
                    if(continousWordsFound.size() > 0) {
                        indexofFileWhereFoundContValues = new ArrayList();
                        for (int i = 0; i < continousWordsFound.size(); i++) {
                            indexofFileWhereFoundContValues.add(currentFileMap.get(s[0]).get(continousWordsFound.get(i)));
                        }
                        Debug.println("fileNameKey " + fileNameKey, indexofFileWhereFoundContValues.size() + " : " + continousWordsFound.size());
                        foundKeyWordPlaces.put(fileNameKey, indexofFileWhereFoundContValues);//put multiple files input   
                    }
                    
                    continousWordsFound.clear();
                    toremoveFromFoundIndex.clear();
                 
                    val.clear();
                    val1.clear();
                }
            }
            tempArr.add(foundKeyWordPlaces);
            return tempArr;
        }
    }
}
