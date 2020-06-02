/*
  Copyright 2020 The SODA Authors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.soda.utils.signature;

import uk.co.lucasweb.aws.v4.signer.Header;
import uk.co.lucasweb.aws.v4.signer.HttpRequest;
import uk.co.lucasweb.aws.v4.signer.Signer;
import uk.co.lucasweb.aws.v4.signer.credentials.AwsCredentials;

import java.net.URI;
import java.net.URISyntaxException;

public class SodaV4Signer {

    /**
     * Generate v4 Signature.
     *
     * @param method     Method name e.g GET
     * @param url        URL e.g https://www.opensds.io/
     * @param accessKey  Access key get from AKSK API
     * @param secretKey  Secret key get from  AKSK API
     * @param payload    Request body converted in to SHA265
     * @param regionName Region name
     * @param headers    Headers
     * @return Signature
     */
    public static String getSignature(String method, String url,
                                      String accessKey, String secretKey,
                                      String payload, String regionName, Header... headers) {
        HttpRequest httpRequest = null;
        try {
            httpRequest = new HttpRequest(method, new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Signer.builder()
                .awsCredentials(new AwsCredentials(accessKey, secretKey))
                .region(regionName)
                .headers(headers)
                .buildS3(httpRequest, payload)
                .getSignature();
    }
}
