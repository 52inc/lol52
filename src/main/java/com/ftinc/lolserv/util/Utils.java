package com.ftinc.lolserv.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will contain a handful of useful functions
 *
 * Project: CanaryAPI
 * Created by drew.heavner on 8/27/14.
 */
public class Utils {

    /**
     * Generate a random AlphaNumeric string to use as a token
     *
     * @return      the 128 length random alpha numeric string
     */
    public static String generateToken(){
        return RandomStringUtils.randomAlphanumeric(128);
    }

    /**
     * Get the time from epoch in seconds
     *
     * @return      epoch seconds
     */
    public static long time(){
        return Instant.now().getEpochSecond();
    }

    public static boolean isEmpty(String val){
        if(val == null || val.isEmpty()) return true;
        return false;
    }

    /**
     * Generate a timed salted hash
     *
     * @param data      the token to hash
     * @param salt      the salt to flavor it with
     * @return          the sha1 hash of the token+salt+time, or an empty string
     */
    public static String generateTimedHash(String data, String salt){
        return sha1(data.concat(salt).concat(String.valueOf(time())));
    }

    public static File generateGifFile(String hash){
        File file = new File(hash.concat(".gif"));
        return file;
    }

    /**
     * Verify a timed hash for validity
     *
     * @param token     the hashed token to check against
     * @param data      the data to be hashed
     * @param salt      the hash flavor
     * @param life      the life of the hash
     * @return          true if valid, false otherwise
     */
    public static boolean verifyTimedHash(String token, String data, String salt, long life){
        long time = time();

        // Verify hash against now
        if(token.equals(sha1(data.concat(salt).concat(String.valueOf(time)))))
            return true;

        // If that didn't work, try verifying it, each time subtracting 1 from time, life times
        for(int offset=1; offset<life; offset++){
            if(token.equals(sha1(data.concat(salt).concat(String.valueOf(time - offset)))))
                return true;
        }

        // Nope!
        return false;
    }

    /**
     * Convert a result set instance into a Key/Value map for ease of
     * use
     *
     * @param set       the result set to convert
     * @return          the converted map
     */
    public static Map<String, Object> resultToMap(ResultSet set){
        Map<String, Object> map = new HashMap<>();

        try {
            ResultSetMetaData metaData = set.getMetaData();
            int columnCount = metaData.getColumnCount();
            for(int i=1; i<=columnCount; i++){
                String key = metaData.getColumnName(i);
                Object value = set.getObject(i);
                map.put(key, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * Hash a string to SHA1
     *
     * @param input     the string to hash
     * @return          the hashed string
     */
    public static String sha1(String input)  {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            return Hex.encodeHexString(result);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Hash a string to sha256
     *
     * @param input     the string to hash
     * @return          the hashed string, or an empty one
     */
    public static String sha256(String input){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }

        md.update(input.getBytes());
        byte[] shaDig = md.digest();
        return new String(Hex.encodeHex(shaDig));
    }

}