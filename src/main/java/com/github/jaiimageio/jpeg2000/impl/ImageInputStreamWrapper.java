/*
 * $RCSfile: ImageInputStreamWrapper.java,v $
 *
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this  list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for
 * use in the design, construction, operation or maintenance of any
 * nuclear facility.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:01:32 $
 * $State: Exp $
 */
package com.github.jaiimageio.jpeg2000.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.ImageInputStream;

/**
 * This class is designed to wrap a <code>ImageInputStream</code> into
 * a <code>InputStream</code>. The reason is that <code>ImageInputStream</code>
 * implements <code>DataInput</code> but doesn't extend
 * <code>InputStream</code>. However, the JJ2000 JPEG 2000 packages accepts
 * a <code>InputStream</code> when reads a JPEG 2000 image file.
 */
public class ImageInputStreamWrapper extends InputStream
{

    /** The <code>ImageInputStream</code> to be wrapped. */
    private ImageInputStream src;


    /**
     * Constructs an <code>ImageInputStreamWrapper</code> from the provided
     * <code>ImageInputStream</code>.
     *
     * @param src The <code>ImageInputStream</code> to be wrapped.
     */
    public ImageInputStreamWrapper(ImageInputStream src)
    {
        this.src = src;
    }


    // Override the methods defined in <code>InputStream</code>
    @Override
    public int read() throws IOException
    {
        return src.read();
    }


    @Override
    public void close() throws IOException
    {
        src.close();
    }


    @Override
    public synchronized void mark(int readlimit)
    {
        src.mark();
    }


    @Override
    public boolean markSupported()
    {
        return true;
    }


    @Override
    public int read(byte b[]) throws IOException
    {
        return src.read(b, 0, b.length);
    }


    @Override
    public int read(byte b[], int off, int len) throws IOException
    {
        return src.read(b, off, len);
    }


    @Override
    public synchronized void reset() throws IOException
    {
        src.reset();
    }


    @Override
    public long skip(long n) throws IOException
    {
        return src.skipBytes(n);
    }
}
