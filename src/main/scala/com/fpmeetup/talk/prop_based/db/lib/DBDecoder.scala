package com.fpmeetup.talk.prop_based.db.lib

class DBDecoder {

  def decode(bytes: Array[Byte]): String = {
    if (bytes == null) ""
    else
      new String(bytes)
  }
}
