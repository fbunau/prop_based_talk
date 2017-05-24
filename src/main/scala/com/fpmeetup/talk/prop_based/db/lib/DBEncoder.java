package com.fpmeetup.talk.prop_based.db.lib;

// Dramatization
public class DBEncoder {

  public byte[] encode(String input) throws Exception {
      if (input.length() == 0) return null;

      byte[] buffer = new byte[input.length()];
      for (int i = 0; i < input.length(); ++i) {
          char c = input.charAt(i);
          if (c < 32 || c > 126) {
              throw new Exception("Unsupported characters");
          }
          buffer[i] = (byte)c;
      }
      return buffer;
  }

}
