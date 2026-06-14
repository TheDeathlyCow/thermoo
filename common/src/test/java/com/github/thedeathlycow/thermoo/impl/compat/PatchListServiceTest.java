package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

class PatchListServiceTest {
    private HttpClient mockClient;
    private final URI uri = URI.create("https://www.thedeathlycow.com/thermoo-patches-patch-list.json"); // doesnt need to point at anything in particular

    @BeforeEach
    void setup() {
        mockClient = Mockito.mock(HttpClient.class);
    }

    @Test
    void when200_thenParsePatchList() throws IOException, InterruptedException {
        String responseBody = """
                {
                    "patches": [
                        {
                            "loader": "fabric",
                            "minecraft_versions": ["1.21.1"],
                            "mods": [
                                "seasonsmod",
                                "hudmod",
                                "othermod"
                            ]
                        },
                        {
                            "loader": "neoforge",
                            "minecraft_versions": ["1.12.2"],
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

        PatchList patchList = PatchListService.fetchPatchList(mockClient, uri);

        PatchList expectedPatchList = new PatchList(
                List.of(
                        new PatchedVersion(Loader.FABRIC, List.of("1.21.1"), List.of("seasonsmod", "hudmod", "othermod")),
                        new PatchedVersion(Loader.NEOFORGE, List.of("1.12.2"), List.of("seasonsmod", "hudmod"))
                )
        );
        Assertions.assertEquals(expectedPatchList, patchList);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 404, 500, 502})
    void whenError_thenThrowsIOException(int statusCode) throws IOException, InterruptedException {
        HttpResponse<Object> mockResponse = (HttpResponse<Object>) Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(statusCode);
        Mockito.when(mockClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);

        Assertions.assertThrows(IOException.class, () -> PatchListService.fetchPatchList(mockClient, uri));
    }
}