/*
 * Copyright (c) 2016, Inversoft Inc., All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.primeframework.jwt.hmac;

import org.primeframework.jwt.Signer;
import org.primeframework.jwt.domain.Algorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * This class can sign and verify a JWT that was signed using HMAC. An instance of this class is intended to
 * be re-used with the {@link HmacVerifier}.
 *
 * @author Daniel DeGroff
 */
public class HmacSigner implements Signer {

  private final Algorithm algorithm;

  private byte[] secret;

  private HmacSigner(Algorithm algorithm, String secret) {
    this.algorithm = algorithm;
    this.secret = secret.getBytes();
  }

  public static HmacSigner newSha256Signer(String secret) {
    return new HmacSigner(Algorithm.HS256, secret);
  }

  public static HmacSigner newSha512Signer(String secret) {
    return new HmacSigner(Algorithm.HS512, secret);
  }

  @Override
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  @Override
  public byte[] sign(String message) {
    Objects.requireNonNull(algorithm);
    Objects.requireNonNull(secret);

    try {
      Mac mac = Mac.getInstance(algorithm.getName());
      mac.init(new SecretKeySpec(secret, algorithm.getName()));
      return mac.doFinal(message.getBytes());
    } catch (InvalidKeyException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
