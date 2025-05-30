package com.github.thedeathlycow.thermoo.impl.compat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

class PatchListServiceTest {
    private HttpClient mockClient;

    @BeforeEach
    void setup() {
        mockClient = Mockito.mock(HttpClient.class);
    }

    @Test
    void when2xx_thenParsePatchList() throws IOException, InterruptedException {
        String responseBody = """
                {
                    "patches": [
                        {
                            "minecraft_version": "1.21.1",
                            "mods": [
                                "seasonsmod",
                                "hudmod",
                                "othermod"
                            ]
                        },
                        {
                            "minecraft_version": "1.12.2",
                            "mods": [
                                "seasonsmod",
                                "hudmod"
                            ]
                        }
                    ]
                }
                """;
        HttpResponse<Object> mockResponse = (HttpResponse<Object>) Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(responseBody);
        Mockito.when(mockClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);

        PatchList patchList = PatchListService.fetchPatches(mockClient);

        PatchList expectedPatchList = new PatchList(
                List.of(
                        new PatchedVersion("1.21.1", List.of("seasonsmod", "hudmod", "othermod")),
                        new PatchedVersion("1.12.2", List.of("seasonsmod", "hudmod"))
                )
        );
        Assertions.assertEquals(expectedPatchList, patchList);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 404})
    void when4xx_thenThrowsIOException(int statusCode) throws IOException, InterruptedException {
        HttpResponse<Object> mockResponse = (HttpResponse<Object>) Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(statusCode);
        Mockito.when(mockClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);

        Assertions.assertThrows(IOException.class, () -> PatchListService.fetchPatches(mockClient));
    }

    @ParameterizedTest
    @ValueSource(ints = {500, 502})
    void when5xx_thenThrowsIOException(int statusCode) throws IOException, InterruptedException {
        HttpResponse<Object> mockResponse = (HttpResponse<Object>) Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(statusCode);
        Mockito.when(mockClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);

        Assertions.assertThrows(IOException.class, () -> PatchListService.fetchPatches(mockClient));
    }
}