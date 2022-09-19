/*
 * Copyright 2022 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.server.grpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

class HttpJsonTranscodingOptionsBuilderTest {

    @Test
    void shouldDisallowEmptyNaming() {
        assertThatThrownBy(() -> HttpJsonTranscodingOptions.builder()
                                                           .queryParamNaming(ImmutableList.of())
                                                           .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Can't disable both useProtoFieldNameQueryParams and useCamelCaseQueryParams");
    }

    @Test
    void shouldReturnConfiguredSettings() {
        final HttpJsonTranscodingOptions withCamelCase =
                HttpJsonTranscodingOptions.builder()
                                          .queryParamNaming(
                                                  HttpJsonTranscodingQueryParamNaming.LOWER_CAMEL_CASE)
                                          .build();
        assertThat(withCamelCase.queryParamNamings())
                .containsExactly(HttpJsonTranscodingQueryParamNaming.LOWER_CAMEL_CASE);

        final HttpJsonTranscodingOptions onlyCamelCase =
                HttpJsonTranscodingOptions.builder()
                                          .queryParamNaming(HttpJsonTranscodingQueryParamNaming.ORIGINAL_FIELD)
                                          .build();
        assertThat(onlyCamelCase.queryParamNamings())
                .containsExactly(HttpJsonTranscodingQueryParamNaming.ORIGINAL_FIELD);

        final HttpJsonTranscodingOptions onlyOriginalField =
                HttpJsonTranscodingOptions.builder()
                                          .queryParamNaming(ImmutableList.of(
                                                  HttpJsonTranscodingQueryParamNaming.LOWER_CAMEL_CASE,
                                                  HttpJsonTranscodingQueryParamNaming.ORIGINAL_FIELD))
                                          .build();
        assertThat(onlyOriginalField.queryParamNamings())
                .containsExactlyInAnyOrder(HttpJsonTranscodingQueryParamNaming.ORIGINAL_FIELD,
                                           HttpJsonTranscodingQueryParamNaming.LOWER_CAMEL_CASE);

        final HttpJsonTranscodingOptions defaultOptions =
                HttpJsonTranscodingOptions.of();
        assertThat(defaultOptions.queryParamNamings())
                .containsExactly(HttpJsonTranscodingQueryParamNaming.ORIGINAL_FIELD);
    }
}