/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.compat;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class PatchListService {
    /**
     * Fetches the Thermoo Patches patch list from the given URI. This call is blocking and should be executed asynchronously
     * (such as on a virtual thread).
     *
     * @param client An active HTTP client
     * @param uri The URI of the location of the patch list
     * @return Returns the decoded patch list from the URI
     * @throws InterruptedException Thrown if the client is interrupted
     * @throws IOException          Thrown if I/O error occurs while sending/receiving data, or if the response is not OK.
     */
    public static PatchList fetchPatchList(HttpClient client, URI uri) throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new HttpResponseException(response.statusCode(), response.body());
        }

        JsonObject json = GsonHelper.parse(response.body());
        return PatchList.CODEC.parse(
                JsonOps.INSTANCE,
                json
        ).getOrThrow();
    }

    private PatchListService() {

    }
}