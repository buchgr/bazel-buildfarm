// Copyright 2018 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package build.buildfarm.common;

import static com.google.common.truth.Truth.assertThat;

import com.google.devtools.remoteexecution.v1test.Digest;
import com.google.protobuf.ByteString;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DigestUtilTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void invalidHashCodeIsDetectedDuringConstruction() {
    DigestUtil util = new DigestUtil(DigestUtil.HashFunction.MD5);
    thrown.expect(NumberFormatException.class);
    thrown.expectMessage("[foo] is not a valid MD5 hash.");
    util.build("foo", 3);
  }

  @Test
  public void canBuildAValidDigest() {
    DigestUtil util = new DigestUtil(DigestUtil.HashFunction.MD5);
    String bazelMd5Hash = "24ef4c36ec66c15ef9f0c96fe27c0e0b";
    long payloadSizeInBytes = 5;
    Digest digest = util.build(bazelMd5Hash, payloadSizeInBytes);
    assertThat(digest.getHash()).isEqualTo(bazelMd5Hash);
    assertThat(digest.getSizeBytes()).isEqualTo(payloadSizeInBytes);
  }

  @Test
  public void computesMd5Hash() {
    ByteString content = ByteString.copyFromUtf8("bazel");
    DigestUtil util = new DigestUtil(DigestUtil.HashFunction.MD5);
    Digest digest = util.compute(content);
    assertThat(digest.getHash()).isEqualTo("24ef4c36ec66c15ef9f0c96fe27c0e0b");
  }

  @Test
  public void computesSha256Hash() {
    ByteString content = ByteString.copyFromUtf8("bazel");
    DigestUtil util = new DigestUtil(DigestUtil.HashFunction.SHA256);
    Digest digest = util.compute(content);
    assertThat(digest.getHash())
        .isEqualTo("aa0e09c406dd0db1a3bb250216045e81644d26c961c0e8c34e8a0354476ca6d4");
  }

  @Test
  public void computesSha1Hash() {
    ByteString content = ByteString.copyFromUtf8("bazel");
    DigestUtil util = new DigestUtil(DigestUtil.HashFunction.SHA1);
    Digest digest = util.compute(content);
    assertThat(digest.getHash()).isEqualTo("287d5d65c10a8609e9c504c81f650b0e1669a824");
  }
}
