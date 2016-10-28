/**
 * @author nrapopor - Nick Rapoport
 * @copyright Copyright 2016 ( Oct 27, 2016 ) Nick Rapoport all rights reserved.
 */
package com.parasoft.sa.virtualize.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>A singleton pattern <code>Caffeine</code> cache implementation for Virtualize. to allow PVA to hold transient
 * transaction information.
 * <p>
 * This implementation provides two singleton caches:
 * <LI>One implementing a String key and XMLHashMap&lt;String,String&gt; value</LI>
 * <LI>Another providing a generic String key and Object value</LI>
 * </p>
 * </DD>
 *
 * @param <V>
 *            the value type to be used in the cache (the key is always String)
 *
 *            <DT>Date:</DT>
 *            <DD>Oct 27, 2016</DD>
 *            </DL>
 *
 * @see com.github.benmanes.caffeine.cache.Caffeine
 * @see com.parasoft.sa.virtualize.cache.HashMapXML
 * @author nrapopor - Nick Rapoport
 *
 */
public class VirtualizeCache<V> implements Serializable {
    /**
     * <DL>
     * <DT>DEFAULT_CACHE_SIZE</DT>
     * <DD>used only if the configuration element <code>VirtualizeCache.cache.size</code> cannot be found in
     * <Code>CacheConfig.properties</code></DD>
     * </DL>
     */
    public static final long DEFAULT_CACHE_SIZE = 10000L;

    /**
     * <DL>
     * <DT>DEFAULT_EVICTION_MSG</DT>
     * <DD>used only if the configuration element <code>VirtualizeCache.cache.eviction.msg</code> cannot be found in
     * <Code>CacheConfig.properties</code></DD>
     * </DL>
     */
    public static final String DEFAULT_EVICTION_MSG = "Key \"{}\" for \"{}\"  was removed ({})";

    /**
     * <DL>
     * <DT>EVICTION_MSG</DT>
     * <DD>the message format to use when reporting the eviction</DD>
     * </DL>
     */
    private static final String EVICTION_MSG =
        CacheConfig.getString("VirtualizeCache.cache.eviction.msg", DEFAULT_EVICTION_MSG); //$NON-NLS-1$

    /**
     * <DL>
     * <DT>serialVersionUID</DT>
     * <DD>default serial version id</DD>
     * </DL>
     */
    private static final long serialVersionUID = 1L;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VirtualizeCache.class);

    private static final VirtualizeCache<Map<String, String>> mapInstance = new VirtualizeCache<>();

    private static final VirtualizeCache<Object> objInstance = new VirtualizeCache<>();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the instance property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return the value of instance field
     */
    public static VirtualizeCache<Object> getObjectInstance() {
        return objInstance;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the instance property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return the value of instance field
     */
    public static VirtualizeCache<Map<String, String>> getXMLMapInstance() {
        return mapInstance;
    }

    private final long cacheMaxSize = CacheConfig.getLong("VirtualizeCache.cache.size", DEFAULT_CACHE_SIZE); //$NON-NLS-1$

    private final Cache<String, V> cache;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>VirtualizeCache Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     */
    public VirtualizeCache() {
        super();

        cache = Caffeine.newBuilder() //
            .maximumSize(getCacheMaxSize()) //
            .expireAfterWrite(10, TimeUnit.MINUTES) //
            .removalListener((final String key, final V data, final RemovalCause cause) //
            -> log.debug(EVICTION_MSG, key, data.toString(), cause)) //
            .build();
    }

    /**
     * {@inheritDoc}
     * <DL>
     * <DT>Description:</DT>
     * <DD>Performs any pending maintenance operations needed by the cache. Exactly which activities are performed -- if
     * any -- is implementation-dependent.</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @see com.github.benmanes.caffeine.cache.Cache#cleanUp()
     */
    public void cleanUp() {
        cache.cleanUp();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Returns the approximate number of entries in this cache</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return
     * @see com.github.benmanes.caffeine.cache.Cache#estimatedSize()
     */
    public long estimatedSize() {
        return cache.estimatedSize();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Returns the value associated with key in this cache. For usage details see
     * <code>com.github.benmanes.caffeine.cache.Cache#get(java.lang.Object, java.util.function.Function)</code></DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aKey
     * @param aMappingFunction
     * @return
     * @see com.github.benmanes.caffeine.cache.Cache#get(java.lang.Object, java.util.function.Function)
     */
    public Object get(final String aKey, final Function<? super String, ? extends V> aMappingFunction) {
        return cache.get(aKey, aMappingFunction);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the cache property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return the value of cache field
     */
    public Cache<String, V> getCache() {
        return cache;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the cacheMaxSize property. if the cache exceeds this size. items will be evicted based on
     * <a href="https://github.com/ben-manes/caffeine/wiki/Efficiency">frequency and recency</a></DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return the value of cacheMaxSize field
     */
    public long getCacheMaxSize() {
        return cacheMaxSize;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Returns the value associated with key in this cache, or null if there is no cached value for key.</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aKey
     * @return
     * @see com.github.benmanes.caffeine.cache.Cache#getIfPresent(java.lang.Object)
     */
    public V getIfPresent(final String aKey) {
        return cache.getIfPresent(aKey);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Discards any cached value for key <code>aKey</code>. For usage details see
     * <code>com.github.benmanes.caffeine.cache.Cache#invalidate(java.lang.Object)</code></DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aKey
     * @see com.github.benmanes.caffeine.cache.Cache#invalidate(java.lang.Object)
     */
    public void invalidate(final String aKey) {
        cache.invalidate(aKey);
    }

    /**
     * {@inheritDoc}
     * <DL>
     * <DT>Description:</DT>
     * <DD>Discards all entries in the cache. The behavior of this operation is undefined for an entry that is being
     * loaded and is otherwise not present.</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @see com.github.benmanes.caffeine.cache.Cache#invalidateAll()
     */
    public void invalidateAll() {
        cache.invalidateAll();
    }

    /**
     * {@inheritDoc}
     * <DL>
     * <DT>Description:</DT>
     * <DD>Discards any cached values for keys keys. The behavior of this operation is undefined for an entry that is
     * being loaded and is otherwise not present.</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aKeys
     * @see com.github.benmanes.caffeine.cache.Cache#invalidateAll(java.lang.Iterable)
     */
    public void invalidateAll(final Iterable<String> aKeys) {
        cache.invalidateAll(aKeys);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Associates value with key in this cache. For usage details see
     * <code>com.github.benmanes.caffeine.cache.Cache#put(java.lang.Object, java.lang.Object)</code></DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aKey
     * @param aValue
     * @see com.github.benmanes.caffeine.cache.Cache#put(java.lang.Object, java.lang.Object)
     */
    public void put(final String aKey, final V aValue) {
        cache.put(aKey, aValue);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>returns the Cache statistics</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @return
     * @see com.github.benmanes.caffeine.cache.Cache#stats()
     */
    public CacheStats stats() {
        return cache.stats();
    }

}
