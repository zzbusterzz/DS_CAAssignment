/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexingprogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 1
 */
public class IndexHolder {
    
    private HashMap<String, List<Integer>> map;
    
    public IndexHolder(){
        map = new HashMap<String, List<Integer>>();
    }
    
    public void AddIndex(String word, int charPos)
    {
        if(map.containsKey(map))
        {
            List<Integer> intg = map.get(word);
            intg.add(charPos);
        }
        else{
            List<Integer> intg = new ArrayList<Integer>();
            intg.add(charPos);
            map.put(word, intg);
        }
    }
    
    public boolean CheckForPhrase(String phrase)
    {
        String[] s = phrase.split(" ");
        boolean wordsExist = true;
        for(int i = 0; i < s.length; i++)//check if words exist in the maps
        {
            if(!map.containsKey(s[i])){
                return false;
            }else
            {
             //TODO calculcate index   
            }
        }
        
        
        for(int i = 0; i < s.length; i++)//check if words exist in the maps
        {
            if(!map.containsKey(s[i])){
                return false;
            }
        }
        
        return false;
    }
}
