package de.aaa.al_shajara.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A map which holds a list of values for every key
 * @author mat
 *
 * @param <K> the key Type
 * @param <V> the type of the elements of the lists
 */
public class ListMap<K,V> extends HashMap<K,List<V>> {

  private boolean acceptNullValues = false;
  
  public List<V> putList(K key, V value){
    if (value==null && !acceptNullValues){
      return null;
    }
    List<V> list = get(key);
    if (list==null){
      list = new LinkedList<V>();
      this.put(key, list);
    }
    list.add(value);
    return list;
  }
  
  public void putList(K key, Collection<V> values){
    for(V value: values){
      putList(key,value);
    }
  }

  public boolean isAcceptNullValues() {
    return acceptNullValues;
  }

  public void setAcceptNullValues(boolean acceptNullValues) {
    this.acceptNullValues = acceptNullValues;
  }
}