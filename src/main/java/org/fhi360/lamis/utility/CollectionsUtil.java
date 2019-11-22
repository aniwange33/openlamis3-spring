/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author user1
 */
public class CollectionsUtil {
    
    public static ArrayList<Map<String, String>> addList(ArrayList<Map<String, String>> list1, ArrayList<Map<String, String>> list2) {
        Set<Map<String, String>>newSet = new HashSet<Map<String, String>>(list1);
        newSet.addAll(list2);
        ArrayList<Map<String, String>> newList = new ArrayList<Map<String, String>>(newSet);        
        return newList;
    }   
}
