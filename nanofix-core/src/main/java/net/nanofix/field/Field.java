package net.nanofix.field;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 25/03/12
 * Time: 20:59
 */
public interface Field {
    int getTag();
    byte[] getBytes();
    Object getValue();
}
