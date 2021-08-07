/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.feed;

import adsstest.func.Callback;

/**
 *
 * @author Chukwudi Alimele
 * @param <T>
 */
public interface IStorage<T> {
    
    /**
     * Initialize the store with default region
     */
    void init();
    
    /**
     * Initialize the store with specified region
     * 
     * @param region
     */
    void init(String region);
    
    /**
     * Store the element with the specified key
     * 
     * @param key
     * @param value 
     */
    void store(String key, T value);
    
    /**
     * Store the element in the specified group using the key
     * 
     * @param key
     * @param groupName
     * @param value 
     */
    void store(String key, String groupName, T value);
    
    /**
     * Get the element stored using the key
     * 
     * @param key
     * @return 
     */
    T get(String key);
    
    /**
     * Get the element stored in the specified group using the key
     * 
     * @param groupName
     * @param key
     * @return 
     */
    T get(String key, String groupName);
    
    /**
     * Iterate through the elements in the specified group
     * 
     * @param groupName
     * @param callback
     */
    void eachInGroup(String groupName, Callback callback);
    
    /**
     * Remove the element with the specified key
     * 
     * @param key
     */
    void remove(String key);
    
    /**
     * Remove the element with the specified key from the group
     * 
     * @param groupName
     * @param key
     */
    void remove(String key, String groupName);
    
    /**
     * Clear everything
     * 
     */
    void clear();

    /**
     * Clear everything in the specified group
     * 
     * @param groupName
     */
    void clear(String groupName);

    /**
     * Shutdown the store and do some cleanup
     */
    public void shutdown();
    
}
