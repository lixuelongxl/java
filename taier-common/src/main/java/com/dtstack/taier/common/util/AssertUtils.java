package com.dtstack.taier.common.util;

import com.dtstack.taier.common.exception.DtCenterDefException;
import com.dtstack.taier.common.exception.ErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Company: www.dtstack.com
 *
 * @author qianyi
 */
public abstract class AssertUtils {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isOverLength(String content, Integer limit, String message) {
        if (StringUtils.isNotBlank(content) && content.length() > limit) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new DtCenterDefException(errorCode);
        }
    }

    public static void isTrue(boolean expression, String msg, ErrorCode errorCode) {
        if (!expression) {
            throw new DtCenterDefException(msg);
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isNull(Object obj, ErrorCode errorCode) {
        if (obj != null) {
            throw new DtCenterDefException(errorCode);
        }
    }

    public static void notBlank(String obj, ErrorCode errorCode) {
        if (StringUtils.isBlank(obj)) {
            throw new DtCenterDefException(errorCode);
        }
    }

    public static void notBlank(String obj, String message) {
        if (StringUtils.isBlank(obj)) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new DtCenterDefException(message);
        }
    }

    public static void isFalse(boolean expression, ErrorCode errorCode) {
        if (expression) {
            throw new DtCenterDefException(errorCode);
        }
    }

    public static void notNull(Object obj, ErrorCode errorCode) {
        if (obj == null) {
            throw new DtCenterDefException(errorCode);
        }
    }

    public static void notNull(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new DtCenterDefException(message);
        }
    }

    public static void notNull(Collection collection, ErrorCode errorCode) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new DtCenterDefException(errorCode);
        }
    }

}
