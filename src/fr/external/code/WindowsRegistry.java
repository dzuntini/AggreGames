package fr.external.code;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import fr.zuntini.platform.AGList;


 
/**
 * Very evil class to read from the Windows registry by breaking into the WindowsPreference
 * class methods and forcing them to be accessible.
 * <p/>
 * N.B. All access to WindowsPreferences (rather than just Preferences) must be through introspection,
 * as this class only exists on Windows platforms.
 *
 * @author David Croft (<a href="http://www.davidc.net">www.davidc.net</a>)
 * @version $Id: WindowsRegistry.java 285 2009-06-18 17:48:28Z david $
 */
//@SuppressWarnings({"HardCodedStringLiteral", "StringConcatenation"})
public class WindowsRegistry
{
  /* Windows hives */
  public static final int HKEY_CURRENT_USER = 0x80000001;
  public static final int HKEY_LOCAL_MACHINE = 0x80000002;
 
  /* Windows security masks */
  private static final int KEY_READ = 0x20019;
 
  /* Constants used to interpret returns of native functions */
  private static final int NATIVE_HANDLE = 0;
  private static final int ERROR_CODE = 1;
 
  /* Windows error codes. */
  private static final int ERROR_SUCCESS = 0;
  private static final int ERROR_FILE_NOT_FOUND = 2;
 
  @SuppressWarnings({ "unchecked", "rawtypes" })
public static String getKeySz(int hive, String keyName, String valueName)
          throws BackingStoreException
  {
    if (hive != HKEY_CURRENT_USER && hive != HKEY_LOCAL_MACHINE) {
      throw new IllegalArgumentException("Invalid hive " + hive);
    }
 
    final Class clazz = Preferences.userRoot().getClass();
 
    try {
      final Method openKeyMethod = clazz.getDeclaredMethod("WindowsRegOpenKey",
              int.class, byte[].class, int.class);
      openKeyMethod.setAccessible(true);
 
      final Method closeKeyMethod = clazz.getDeclaredMethod("WindowsRegCloseKey",
              int.class);
      closeKeyMethod.setAccessible(true);
 
      final Method queryValueMethod = clazz.getDeclaredMethod("WindowsRegQueryValueEx",
              int.class, byte[].class);
      queryValueMethod.setAccessible(true);
 
      int[] result = (int[]) openKeyMethod.invoke(null, hive, stringToByteArray(keyName), KEY_READ);
      if (result[ERROR_CODE] != ERROR_SUCCESS) {
        if (result[ERROR_CODE] == ERROR_FILE_NOT_FOUND) {
          throw new BackingStoreException("Not Found error opening key " + keyName);
        }
        else {
          throw new BackingStoreException("Error " + result[ERROR_CODE] + " opening key " + keyName);
        }
      }
 
      int hKey = result[NATIVE_HANDLE];
 
      byte[] b = (byte[]) queryValueMethod.invoke(null, hKey, stringToByteArray(valueName));
      closeKeyMethod.invoke(null, hKey);
 
      if (b == null)
        return null;
      else
        return byteArrayToString(b);
    }
    catch (InvocationTargetException e) {
      throw new BackingStoreException(e.getCause());
    }
    catch (NoSuchMethodException | IllegalAccessException e) {
      throw new BackingStoreException(e);
    }
  }
 
  /**
   * Returns this java string as a null-terminated byte array
   *
   * @param str The string to convert
   * @return The resulting null-terminated byte array
   */
  private static byte[] stringToByteArray(String str)
  {
    byte[] result = new byte[str.length() + 1];
    for (int i = 0; i < str.length(); i++) {
      result[i] = (byte) str.charAt(i);
    }
    result[str.length()] = 0;
    return result;
  }
 
  /**
   * Converts a null-terminated byte array to java string
   *
   * @param array The null-terminated byte array to convert
   * @return The resulting string
   */
  private static String byteArrayToString(byte[] array)
  {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < array.length - 1; i++) {
    	if ((char) array[i] != '"')
      result.append((char) array[i]);
    }
    return result.toString();
  }
 //Here begin my part
  @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
  private static String testKey(int hive, String keyName, String valueName)
  {
    String s = null;
    String[] a = null;
    try {
      s = getKeySz(hive, keyName, valueName);
      //a[0] = s;
      System.out.println("s ============="+ s);
     if (s.contains(" %"))
    	 a = s.split(" %");
     if (s.contains(","))
    	a = s.split(",");
      s = a[0];
    }
    catch (BackingStoreException e) {
      System.out.println("    !!" + e.getMessage());
    }
    return s;
  }
 
  public static String testKey2(String arg)
  {
    String a = AGList.getkey(arg);
    System.out.println("La clé est egale a ="+a);
	 //"Battle.net","Bethesda","Epic Store", "GoG Galaxy", "Origin","Others","Steam","Uplay"};
	  return testKey(HKEY_CURRENT_USER, AGList.getkey(a), "");
   
  }
}