/*
 * $RCSfile: J2KImageReaderSpi.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2006/03/31 19:43:39 $
 * $State: Exp $
 */
package com.github.jaiimageio.jpeg2000.impl;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

import com.github.jaiimageio.impl.common.PackageUtil;

public class J2KImageReaderSpi extends ImageReaderSpi
{

    private static String[] writerSpiNames = { "com.github.jaiimageio.jpeg2000.impl.J2KImageWriterSpi" };
    private static String[] formatNames = { "jpeg 2000", "JPEG 2000", "jpeg2000", "JPEG2000" };
    private static String[] extensions = { "jp2" }; // Should add jpx or jpm
    private static String[] mimeTypes = { "image/jp2", "image/jpeg2000" };
    private boolean registered = false;


    public J2KImageReaderSpi()
    {
        super(PackageUtil.getVendor(),
            PackageUtil.getVersion(),
            formatNames,
            extensions,
            mimeTypes,
            "com.github.jaiimageio.jpeg2000.impl.J2KImageReader",
            STANDARD_INPUT_TYPE,
            writerSpiNames,
            false,
            null, null,
            null, null,
            true,
            "com_sun_media_imageio_plugins_jpeg2000_image_1.0",
            "com.github.jaiimageio.jpeg2000.impl.J2KMetadataFormat",
            null, null);
    }


    @Override
    public void onRegistration(ServiceRegistry registry,
        Class category)
    {
        if (registered) {
            return;
        }

        registered = true;

        // Set pairwise ordering to give codecLib reader precedence.
        Class codecLibReaderSPIClass = null;
        try {
            codecLibReaderSPIClass = Class.forName("com.github.jaiimageio.jpeg2000.impl.J2KImageReaderCodecLibSpi");
        }
        catch (Throwable t) {
            // Ignore it.
        }

        if (codecLibReaderSPIClass != null) {
            Object codecLibReaderSPI = registry.getServiceProviderByClass(codecLibReaderSPIClass);
            if (codecLibReaderSPI != null) {
                registry.setOrdering(category, codecLibReaderSPI, this);
            }
        }
    }


    @Override
    public String getDescription(Locale locale)
    {
        String desc = PackageUtil.getSpecificationTitle() +
            " JPEG 2000 Image Reader";
        return desc;
    }


    @Override
    public boolean canDecodeInput(Object source) throws IOException
    {
        if (!(source instanceof ImageInputStream)) {
            return false;
        }

        ImageInputStream stream = (ImageInputStream)source;

        //fix of 4938421
        stream.mark();
        int marker = (stream.read() << 8) | stream.read();

        if (marker == 0xFF4F) {
            stream.reset();
            return true;
        }

        stream.reset();
        stream.mark();
        byte[] b = new byte[12];
        stream.readFully(b);
        stream.reset();

        //Verify the signature box

        // The length of the signature box is 12
        if (b[0] != 0 || b[1] != 0 || b[2] != 0 || b[3] != 12)
            return false;

        // The signature box type is "jP  "
        if ((b[4] & 0xff) != 0x6A || (b[5] & 0xFF) != 0x50 ||
            (b[6] & 0xFF) != 0x20 || (b[7] & 0xFF) != 0x20)
            return false;

        // The signture content is 0x0D0A870A
        if ((b[8] & 0xFF) != 0x0D || (b[9] & 0xFF) != 0x0A ||
            (b[10] & 0xFF) != 0x87 || (b[11] & 0xFF) != 0x0A)
            return false;

        return true;
    }


    @Override
    public ImageReader createReaderInstance(Object extension)
        throws IIOException
    {
        return new J2KImageReader(this);
    }
}

