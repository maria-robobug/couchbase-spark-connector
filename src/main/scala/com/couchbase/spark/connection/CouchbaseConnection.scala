/**
 * Copyright (C) 2015 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.spark.connection

import java.util.concurrent.atomic.AtomicReference

import com.couchbase.client.java.{Cluster, Bucket, CouchbaseCluster}
import org.apache.spark.{SparkConf, SparkContext}

class CouchbaseConnection {

  private val cluster: AtomicReference[Cluster] = new AtomicReference[Cluster]()
  private val bucket: AtomicReference[Bucket] = new AtomicReference[Bucket]()

  def bucket(cfg: CouchbaseConfig): Bucket = {

    if (cluster.get() == null) {
      cluster.set(CouchbaseCluster.create(cfg.host))
    }


    if (bucket.get() == null) {
      bucket.set(cluster.get().openBucket(cfg.bucket, cfg.password))
    }

    bucket.get()
  }

}

object CouchbaseConnection {

  private val connection = new CouchbaseConnection()
  def get = connection

}
