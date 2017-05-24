package com.fpmeetup.talk.prop_based.db

import com.fpmeetup.talk.prop_based.db.lib.{DBDecoder, DBEncoder}

class DatabaseStore() {

  private val dBEncoder = new DBEncoder()
  private val dbDecoder = new DBDecoder()

  private var storage: Array[Byte] = _

  def put(input: String) {
    storage = dBEncoder.encode(input)
  }

  def get(): String = {
    dbDecoder.decode(storage)
  }

}
