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

/**
 *
 * @author 1
 */
public class IndexHolder 
{
    
    private HashMap<String, List<Integer>> map;
    
    public IndexHolder()
    {
        map = new HashMap<String, List<Integer>>();
    }
   
    public void AddIndex(String word, int charPos)
    {
        if(map.containsKey(word))
        {
            List<Integer> intg = map.get(word);
            intg.add(charPos);
            //map.put(word, intg);
        }
        else{
            List<Integer> intg = new ArrayList<Integer>();
            intg.add(charPos);
            map.put(word, intg);
        }
    }
    
    ///Checks for the phrase if exists
    public List<Object> CheckForPhrase(String phrase)
    {
        String[] s = phrase.split(" "); 
        
        List<Integer> continousWordsFound = new ArrayList<Integer>();
        List<Integer> toremoveFromFoundIndex = new ArrayList<Integer>();
        
        if(s.length == 0)
            return Arrays.asList(false);
        else if(s.length == 1)
            if(map.containsKey(s[0]))
            {
                ArrayList tempArr = new ArrayList();
                tempArr.add(true); //Returns phrase as found with all the indexes where it was found
                List<Integer> mappedValues = map.get(s[0]);
                for(int  i = 0; i < mappedValues.size(); i++)
                    tempArr.add(mappedValues.get(i));
                
                return tempArr;
            }
            else
                return Arrays.asList(false);
        else if(!map.containsKey(s[0]) || !map.containsKey(s[1]))
            return Arrays.asList(false);
        else
        {
            //TODO calculcate index
            List<Integer> val = map.get(s[0]);
            List<Integer> val1 = map.get(s[1]);
            
            for(int j = 0; j < val.size(); j++)//match the first word and second word indexes and get the list of values where they are ascending order
            {
                for(int k = 0; k < val1.size(); k++)
                {
                    if(val.get(j)+1 == val1.get(k))
                    {
                        continousWordsFound.add(j);
                    }
                }
            }
            
            //use the ascending order values from above to determine which index has 
            //ascending order  by looping through indexs of words in phrase other then the above two
            //first words indexes are kept which has ascending order for words till the final phrase where all the words are iterated
            
            for(int i = 2; i < s.length; i++)//check if words exist in the maps
            {
                if(!map.containsKey(s[i]))  return Arrays.asList(false);
                else if(continousWordsFound.size() == 0) return Arrays.asList(false);
                else
                {
                    val1 = map.get(s[i]);

                    for(int j = 0; j < continousWordsFound.size(); j++)
                    {
                        int valueToTest = continousWordsFound.get(j);
                        boolean matched = false;
                        
                        for(int k = 0; k < val1.size(); k++)
                        {
                            if(val.get(valueToTest)+i == val1.get(k))
                            {
                                matched = true;
                            }
                        }
                        
                        if(!matched)
                        {
                            toremoveFromFoundIndex.add(valueToTest);
                        }
                    }

                    if(!toremoveFromFoundIndex.isEmpty())//remove the values from index which are not in ascending order
                    {
                        for(int j = 0; j < toremoveFromFoundIndex.size(); j++)
                        {
                            continousWordsFound.remove(toremoveFromFoundIndex.get(j));
                        }
                    }
                    toremoveFromFoundIndex.clear();//clear remove index
                }
            }
        }
        
        ArrayList tempArr = new ArrayList();
        tempArr.add(true); //Returns phrase as found with all the indexes where it was found
        for(int  i = 0; i < continousWordsFound.size(); i++)
        {
            tempArr.add(map.get(s[0]).get(continousWordsFound.get(i)));
        }
        
        return tempArr;
    }
}