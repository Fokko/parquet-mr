/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.parquet.io;

import java.io.ByteArrayInputStream;

class MockInputStream extends ByteArrayInputStream {

  static final byte[] TEST_ARRAY = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

  private int[] lengths;
  private int current = 0;

  MockInputStream(int... actualReadLengths) {
    super(TEST_ARRAY);
    this.lengths = actualReadLengths;
  }

  @Override
  public synchronized int read(byte[] b, int off, int len) {
    if (current < lengths.length) {
      if (len <= lengths[current]) {
        // when len == lengths[current], the next read will by 0 bytes
        int bytesRead = super.read(b, off, len);
        lengths[current] -= bytesRead;
        return bytesRead;
      } else {
        int bytesRead = super.read(b, off, lengths[current]);
        current += 1;
        return bytesRead;
      }
    } else {
      return super.read(b, off, len);
    }
  }

  public long getPos() {
    return this.pos;
  }
}
