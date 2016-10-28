/**
 * @author nrapopor - Nick Rapoport
 * @copyright Copyright 2016 ( Oct 27, 2016 ) Nick Rapoport all rights reserved.
 */
package com.parasoft.sa.virtualize.cache;

import java.util.ResourceBundle;

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
/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>Resource Bundle loader for VirtualizeCache</DD>
 * <DT>Date:</DT>
 * <DD>Oct 27, 2016</DD>
 * </DL>
 *
 * @author nrapopor - Nick Rapoport
 *
 */
public class CacheConfig {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CacheConfig.class);

    private static final String BUNDLE_NAME = "CacheConfig"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static int getInt(final String aKey, final int aDefaultValue) {
        int result = aDefaultValue;
        try {
            final String strValue = RESOURCE_BUNDLE.getString(aKey);
            if (strValue != null && strValue.isEmpty()) {
                result = Integer.parseInt(strValue);
            }
        } catch (final Exception ex) {
            log.error("caught {} Error (using  defult {} ) : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , aDefaultValue, ex);
        }
        return result;
    }

    public static long getLong(final String aKey, final long aDefaultValue) {
        long result = aDefaultValue;
        try {
            final String strValue = RESOURCE_BUNDLE.getString(aKey);
            if (strValue != null && strValue.isEmpty()) {
                result = Long.parseLong(strValue);
            }
        } catch (final Exception ex) {
            log.error("caught {} Error (using  defult {} ) : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , aDefaultValue, ex);
        }
        return result;
    }

    public static String getString(final String aKey) {
        return getString(aKey, '!' + aKey + '!');
    }

    public static String getString(final String aKey, final String aDefaultValue) {
        final String result = aDefaultValue;
        try {
            return RESOURCE_BUNDLE.getString(aKey);
        } catch (final Exception ex) {
            log.error("caught {} Error (using  defult {} ) : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , aDefaultValue, ex);
        }
        return result;
    }

    private CacheConfig() {
    }
}
