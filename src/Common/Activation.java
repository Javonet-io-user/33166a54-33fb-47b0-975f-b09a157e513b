package Common;

import com.javonet.Javonet;
import com.javonet.JavonetApartmentState;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Activation {
  private static final int DEFAULT_BUFFER_SIZE = 100;
  private static final int EOF = 0;
  private static String email;
  private static String licenseKey;

  public static void setLicense(String javonetLicenseEmail, String javonetLicenseKey) {
    email = javonetLicenseEmail;
    licenseKey = javonetLicenseKey;
  }

  public static void initializeJavonet() {
    if (!Javonet.isActivated()) {
      if (email == null || licenseKey == null) {
        try {
          throw new Exception(
              "To use this wrapper please register for Javonet Free Trial at https://my.javonet.com/signin?type=free and call Activation.setLicense(...) before using any class from the wrapper.\n\nYou can also set global license during creation of the wrapper at www.javonet.io in Options tab.");
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
      }
      try {
        Javonet.setApartmentState(JavonetApartmentState.STA);
        Javonet.activate(email, licenseKey, JavonetFramework.v45);
        Javonet.setUsePrivateHandleField(true, "javonetHandle");
        AddEmbeddedDllReference("JavonetTest.dll");
        AddEmbeddedDllReference("Helper.dll");
      } catch (JavonetException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void AddEmbeddedDllReference(String fileName)
      throws IOException, JavonetException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream input = classLoader.getResourceAsStream(fileName);
    byte[] dllBytes = toByteArray(input);

    Javonet.addReference(fileName, dllBytes);
  }

  public static byte[] toByteArray(final InputStream input) throws IOException {
    try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      copy(input, output);
      return output.toByteArray();
    }
  }

  public static int copy(final InputStream input, final OutputStream output) throws IOException {
    final long count = copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    if (count > Integer.MAX_VALUE) {
      return -1;
    }
    return (int) count;
  }

  public static long copyLarge(
      final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
    long count = 0;
    int n;
    while ((n = input.read(buffer)) > EOF) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }
}
