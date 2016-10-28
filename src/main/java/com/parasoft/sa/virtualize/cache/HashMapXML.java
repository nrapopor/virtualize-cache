/**
 * @author nrapopor - Nick Rapoport
 * @copyright Copyright 2016 ( Oct 27, 2016 ) Nick Rapoport all rights reserved.
 */
package com.parasoft.sa.virtualize.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>extends the <code>HashMap</code> to return the xml representation of the key value pairs in the
 *  &lt;HashMapXML&gt;&lt;key&gt;value&lt;key&gt; ... &lt;/HashMapXML&gt; format <br/>
 * Warning: GIGO -- There is no limitation on the keys or values, however there is also no guarantee that the xml
 * generated will be valid.</DD>
 * <DT>Date:</DT>
 * <DD>Oct 27, 2016</DD>
 * </DL>
 *
 * @see java.util.HashMap
 * @author nrapopor - Nick Rapoport
 *
 */
public class HashMapXML<K, V> extends HashMap<K, V> {
    /**
     * <DL>
     * <DT>serialVersionUID</DT>
     * <DD>TODO add serialVersionUID description</DD>
     * </DL>
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HashMapXML.class);

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HashMapXML Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     */
    public HashMapXML() {
        super();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HashMapXML Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aInitialCapacity
     */
    public HashMapXML(final int aInitialCapacity) {
        super(aInitialCapacity);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HashMapXML Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aInitialCapacity
     * @param aLoadFactor
     */
    public HashMapXML(final int aInitialCapacity, final float aLoadFactor) {
        super(aInitialCapacity, aLoadFactor);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HashMapXML Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 27, 2016</DD>
     * </DL>
     *
     * @param aM
     */
    public HashMapXML(final Map<? extends K, ? extends V> aM) {
        super(aM);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

    /**
     * <DL><DT>Description:</DT><DD>
     * return the xml representation of the key value pairs in the
     * &lt;HashMapXML&gt;&lt;key&gt;value&lt;key&gt; ... &lt;/HashMapXML&gt; format.
     * </DD>
     * <DT>Date:</DT><DD>Oct 27, 2016</DD>
     * </DL>
     * @param wrapperTag
     * @return
     */
    public String toString(final String wrapperTag) {
        final StringBuilder result = new StringBuilder("<").append(wrapperTag);
        if (isEmpty()) {
            result.append("/>");

        } else {
            result.append(">");
            for (final K key : keySet()) {
                result.append("<").append(key).append(">").append(get(key)).append("</").append(key.toString())
                .append(">");
            }
            result.append("</").append(wrapperTag).append(">");
        }
        return result.toString();

    }

}
