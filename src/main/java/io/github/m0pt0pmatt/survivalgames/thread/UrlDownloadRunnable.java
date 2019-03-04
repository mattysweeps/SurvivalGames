/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.m0pt0pmatt.survivalgames.thread;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class UrlDownloadRunnable extends PercentageProgressable implements Runnable, Progressable {

    private final URL url;
    private final String fileName;
    private final long expectedFileSize;

    public UrlDownloadRunnable(URL url, String fileName, long expectedFileSize) {
        this.url = checkNotNull(url, "url");
        this.fileName = checkNotNull(fileName, "fileName");
        this.expectedFileSize = expectedFileSize;
    }

    public UrlDownloadRunnable(URL url, String fileName) {
        this(url, fileName, 0);
    }

    @Override
    public void run() {

        int count = 0;
        int limit = 4096;
        byte[] byteBuffer = new byte[limit];

        try {
            InputStream inputStream = url.openStream();
            FileOutputStream fos = new FileOutputStream(fileName);
            do {
                int c = inputStream.read(byteBuffer);
                if (c < 0) {
                    break;
                }

                fos.write(byteBuffer, 0, c);
                count += c;

                if (expectedFileSize != 0) {
                    setPercentage((double) count / (double) expectedFileSize);
                }

            } while (true);

            inputStream.close();
            fos.close();

        } catch (Exception e) {
            throw new RuntimeException("Unable to download URL", e);
        } finally {
            setPercentage(1.0);
        }
    }
}
