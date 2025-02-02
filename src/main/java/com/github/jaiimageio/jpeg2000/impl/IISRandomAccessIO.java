/*
 * $RCSfile: IISRandomAccessIO.java,v $
 *
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. All  Rights Reserved.
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
 * $Date: 2006/08/08 00:31:47 $
 * $State: Exp $
 */
package com.github.jaiimageio.jpeg2000.impl;

import java.io.IOException;
import java.nio.ByteOrder;

import javax.imageio.stream.ImageInputStream;

import jj2000.j2k.io.EndianType;
import jj2000.j2k.io.RandomAccessIO;

/**
 * A wrapper for converting an <code>ImageInputStream</code> into a
 * <code>RandomAccessIO</code>. The resulting class is read-only.
 */
public class IISRandomAccessIO implements RandomAccessIO
{

    /** The <code>ImageInputStream</code> that is wrapped */
    private ImageInputStream iis;


    /**
     * Creates a <code>RandomAccessIO</code> instance from the supplied
     * <code>ImageInputStream</code>.
     *
     * @param iis The source <code>ImageInputStream</code>.
     */
    public IISRandomAccessIO(ImageInputStream iis)
    {
        if (iis == null) {
            throw new IllegalArgumentException("iis == null!");
        }
        this.iis = iis;
    }


    @Override
    public void close() throws IOException
    {
        iis.close();
    }


    /**
     * Returns the stream position clamped to a maximum of
     * <code>Integer.MAX_VALUE</code>.
     */
    @Override
    public int getPos() throws IOException
    {
        long pos = iis.getStreamPosition();
        return pos > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)pos;
    }


    @Override
    public void seek(int off) throws IOException
    {
        iis.seek(off);
    }


    /**
     * Returns the length of the data stream.
     *
     * <p>
     * If the length of the <code>ImageInputStream</code> is not
     * <code>-1</code>, then it is returned after being clamped to
     * a maximum value of <code>Integer.MAX_VALUE</code>. If the
     * <code>ImageInputStream</code> is <code>-1</code>, the stream
     * is read to a maximum position of <code>Integer.MAX_VALUE</code>
     * and its final position is returned. The position of the stream
     * is unchanged from the value it had prior to the call.
     * </p>
     */
    @Override
    public int length() throws IOException
    {
        long len = iis.length();

        // If the length is non-negative, use it.
        if (len != -1L) {
            return len > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)len;
        }

        // If the length is negative, read until the stream ends.
        iis.mark();
        int bufLen = 1024;
        byte[] buf = new byte[bufLen];
        long pos = iis.getStreamPosition();
        while (pos < Integer.MAX_VALUE) {
            int numRead = iis.read(buf, 0, bufLen);
            if (numRead == -1) break; // EOF
            pos += numRead;
        }
        iis.reset();

        // Return the last position.
        return pos > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)pos;
    }


    @Override
    public int read() throws IOException
    {
        return iis.read();
    }


    @Override
    public void readFully(byte b[], int off, int n) throws IOException
    {
        iis.readFully(b, off, n);
    }


    @Override
    public int getByteOrdering()
    {
        return iis.getByteOrder() == ByteOrder.BIG_ENDIAN ? EndianType.BIG_ENDIAN : EndianType.LITTLE_ENDIAN;
    }


    @Override
    public byte readByte() throws IOException
    {
        return iis.readByte();
    }


    @Override
    public int readUnsignedByte() throws IOException
    {
        return iis.readUnsignedByte();
    }


    @Override
    public short readShort() throws IOException
    {
        return iis.readShort();
    }


    @Override
    public int readUnsignedShort() throws IOException
    {
        return iis.readUnsignedShort();
    }


    @Override
    public int readInt() throws IOException
    {
        return iis.readInt();
    }


    @Override
    public long readUnsignedInt() throws IOException
    {
        return iis.readUnsignedInt();
    }


    @Override
    public long readLong() throws IOException
    {
        return iis.readLong();
    }


    @Override
    public float readFloat() throws IOException
    {
        return iis.readFloat();
    }


    @Override
    public double readDouble() throws IOException
    {
        return iis.readDouble();
    }


    @Override
    public int skipBytes(int n) throws IOException
    {
        return iis.skipBytes(n);
    }


    /**
     * A null operation as writing is not supported.
     */
    @Override
    public void flush()
    {
        // Intentionally empty.
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void write(int b) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeByte(int v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeShort(int v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeInt(int v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeLong(long v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeFloat(float v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }


    /**
     * Throws an <code>IOException</code> as writing is not supported.
     */
    @Override
    public void writeDouble(double v) throws IOException
    {
        throw new IOException("Writing is not supported!");
    }
}
