package net.nanofix.message;

/**
 * User; Mark Wardell
 * Date; 11/10/11
 * Time; 07;45
 */
public interface FIXConstants {

    public static final int CHECKSUM_SIZE = 7;  // 10=nnnX
    public static final String BEGIN_STRING_PREFIX = "8=FIX.";
    public static final String BEGIN_STRING_PREFIX_FIXT = "8=FIXT.";

    public static final char FIX_TRUE = 'Y';
    public static final char FIX_FALSE = 'N';


    public static interface SessionRejectReason {
        public static final long UNDEFINED_TAG = 3L;
        public static final long TAG_NOT_DEFINED_FOR_THIS_MESSAGE_TYPE = 2L;
        public static final long SENDINGTIME_ACCURACY_PROBLEM = 10L;
        public static final long REQUIRED_TAG_MISSING = 1L;
        public static final long INVALID_TAG_NUMBER = 0L;
        public static final long DECRYPTION_PROBLEM = 7L;
        public static final long INCORRECT_DATA_FORMAT_FOR_VALUE = 6L;
        public static final long VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG = 5L;
        public static final long TAG_SPECIFIED_WITHOUT_A_VALUE = 4L;
        public static final long COMPID_PROBLEM = 9L;
        public static final long SIGNATURE_PROBLEM = 8L;
        public static final long INVALID_MSGTYPE = 11L;
    }
}
