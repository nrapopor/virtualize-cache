/**
 * @author nrapopor - Nick Rapoport
 * @copyright Copyright 2016 ( Oct 27, 2016 ) Nick Rapoport all rights reserved.
 */
package com.parasoft.sa.virtualize.cache;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO add description</DD>
 * <DT>Date:</DT>
 * <DD>Oct 27, 2016</DD>
 * </DL>
 *
 * @author nrapopor - Nick Rapoport
 *
 */
public class VirtualizeCacheTest {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VirtualizeCacheTest.class);

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>setUp Before Class</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>tear Down After Class</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>set Up before test</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>tear Down after test</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.parasoft.sa.virtualize.cache.VirtualizeCache#getObjectInstance()}.
     */
    @Test
    public void test01GetObjectInstance() {
        final VirtualizeCache<Object> instance = VirtualizeCache.getObjectInstance();
        assertNotNull(instance);
    }

    /**
     * Test method for {@link com.parasoft.sa.virtualize.cache.VirtualizeCache#getXMLMapInstance()}.
     */
    @Test
    public void test02GetXMLMapInstance() {
        final VirtualizeCache<Map<String, String>> instance = VirtualizeCache.getXMLMapInstance();
        assertNotNull(instance);
    }

    /**
     * Test method for {@link com.parasoft.sa.virtualize.cache.VirtualizeCache#put(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void test03Put() {
        final VirtualizeCache<Map<String, String>> instance = VirtualizeCache.getXMLMapInstance();
        final long max = instance.getCacheMaxSize();
        final int overflow = Integer.parseInt("" + max) + 10;
        final List<String> keys = new ArrayList<>(overflow);
        for (int i = 0; i < overflow; i++) {
            final Map<String, String> value = new HashMapXML<>();
            for (int j = 0; j < 10; j++) {
                value.put("key" + j, "value" + j);
            }
            final String guid = UUID.randomUUID().toString();
            keys.add(guid);
            instance.put(guid, value);
        }
        instance.cleanUp();
        long size = instance.estimatedSize();
        assertEquals(max, size);
        int nullCount = 0;
        for (final String key : keys) {
            final Map<String, String> element = instance.getIfPresent(key);
            if (element == null) {
                if (nullCount > 10) {
                    fail("more than 10 keys evicted");
                }
                nullCount++;
            } else {
                assertNotNull(element);
            }
        }
        CacheStats stats = instance.stats();
        assertNotNull(stats);
        log.info("stats: {}", stats.toString());

        final String key = keys.get(keys.size() - 1);
        Map<String, String> element = instance.getIfPresent(key);
        assertNotNull(element);
        instance.cleanUp();
        instance.invalidate(key);
        element = instance.getIfPresent(key);
        assertNull(element);
        //instance.cleanUp();
        stats = instance.stats();
        assertNotNull(stats);
        log.info("stats: {}", stats.toString());
        instance.invalidateAll(keys);
        instance.cleanUp();
        size = instance.estimatedSize();
        assertEquals(0, size);
    }

}
