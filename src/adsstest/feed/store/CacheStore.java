/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.feed.store;

import adsstest.feed.IStorage;
import adsstest.func.Callback;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.jcs3.JCS;
import org.apache.commons.jcs3.access.CacheAccess;
import org.apache.commons.jcs3.access.GroupCacheAccess;
import org.apache.commons.jcs3.access.exception.CacheException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is a wrapper for JCS 
 * But additionally it is thread safe
 * 
 * @author Chukwudi Alimele
 */
public class CacheStore implements IStorage {

    private CacheAccess<Object, Object> cache;
    private GroupCacheAccess<Object, Object> groupCache;
    private String region = "default";
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final String CACHE_CONFIG_FILE = "/adsstest/resources/cache.ccf";
    private static final Logger LOGGER = LogManager.getLogger(CacheStore.class.getName());
    
    private void init0(String region){
        this.region = region;
        JCS.setConfigFilename(CACHE_CONFIG_FILE);
        try {
            cache = JCS.getInstance(region);
            groupCache = JCS.getGroupCacheInstance(region);
        } catch (CacheException e) {
            LOGGER.debug(String.format("Problem initializing cache: %s", e.getMessage()));
        }
    }
    
    @Override
    public void init() {
        init0(region);
    }

    @Override
    public void init(String region) {
        init0(region);
    }

    
    /**
     * Store the element with the specified key
     * 
     * This method is thread safe
     * 
     * @param key
     * @param value 
     */
    @Override
    public void store(String key, Object value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Store the element in the specified group using the key
     * 
     * This method is thread safe
     * 
     * @param key
     * @param groupName
     * @param value 
     */
    @Override
    public void store(String key, String groupName, Object value) {
        lock.writeLock().lock();
        try {
            groupCache.putInGroup(key, groupName, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Get the element stored using the key
     * 
     * This method is thread safe
     * 
     * @param key
     * @return 
     */
    @Override
    public Object get(String key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Iterate through the elements in the specified group
     * 
     * This method is thread safe
     * 
     * @param groupName
     * @param callback
     */
    @Override
    public void eachInGroup(String groupName, Callback callback) {
        lock.readLock().lock();
        try {
            Set<Object> keys = groupCache.getGroupKeys(groupName);
            keys.forEach((key) -> {
                callback.call(get0(key.toString(), groupName));
            });
        } finally {
            lock.readLock().unlock();
        }
    }

    private Object get0(String key, String groupName) {
        return groupCache.getFromGroup(key, groupName);
    }

    /**
     * Get the element stored in the specified group using the key
     * 
     * This method is thread safe
     * 
     * @param groupName
     * @param key
     * @return 
     */
    @Override
    public Object get(String key, String groupName) {
        lock.readLock().lock();
        try {
            return get0(key, groupName);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Remove the element with the specified key
     * 
     * This method is thread safe
     * 
     * @param key
     */
    @Override
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Remove the element with the specified key from the group
     * 
     * This method is thread safe
     * 
     * @param groupName
     * @param key
     */
    @Override
    public void remove(String key, String groupName) {
        lock.writeLock().lock();
        try {
            groupCache.removeFromGroup(key, groupName);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Clear everything
     * 
     * This method is thread safe
     * 
     */
    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Clear everything in the specified group
     * 
     * This method is thread safe
     * 
     * @param groupName
     */
    @Override
    public void clear(String groupName) {
        lock.writeLock().lock();
        try {
            groupCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void shutdown() {
        cache.dispose();
        groupCache.dispose();
    }

}
