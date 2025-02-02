/*
 * $RCSfile: J2KImageWriterSpi.java,v $
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

import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.util.Locale;

import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;

import com.github.jaiimageio.impl.common.PackageUtil;

public class J2KImageWriterSpi extends ImageWriterSpi
{
    private static String[] readerSpiNames = { "com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi" };
    private static String[] formatNames = { "jpeg 2000", "JPEG 2000", "jpeg2000", "JPEG2000" };
    private static String[] extensions = { "jp2" }; // Should add jpx or jpm
    private static String[] mimeTypes = { "image/jp2", "image/jpeg2000" };
    private boolean registered = false;


    public J2KImageWriterSpi()
    {
        super(PackageUtil.getVendor(),
            PackageUtil.getVersion(),
            formatNames,
            extensions,
            mimeTypes,
            "com.github.jaiimageio.jpeg2000.impl.J2KImageWriter",
            STANDARD_OUTPUT_TYPE,
            readerSpiNames,
            false,
            null, null,
            null, null,
            true,
            "com_sun_media_imageio_plugins_jpeg2000_image_1.0",
            "com.github.jaiimageio.jpeg2000.impl.J2KMetadataFormat",
            null, null);
    }


    @Override
    public String getDescription(Locale locale)
    {
        String desc = PackageUtil.getSpecificationTitle() +
            " JPEG 2000 Image Writer";
        return desc;
    }


    @Override
    public void onRegistration(ServiceRegistry registry,
        Class category)
    {
        if (registered) {
            return;
        }

        registered = true;

        // Set pairwise ordering to give codecLib writer precedence.
        Class codecLibWriterSPIClass = null;
        try {
            codecLibWriterSPIClass = Class.forName("com.github.jaiimageio.jpeg2000.impl.J2KImageWriterCodecLibSpi");
        }
        catch (Throwable t) {
            // Ignore it.
        }

        if (codecLibWriterSPIClass != null) {
            Object codecLibWriterSPI = registry.getServiceProviderByClass(codecLibWriterSPIClass);
            if (codecLibWriterSPI != null) {
                registry.setOrdering(category, codecLibWriterSPI, this);
            }
        }
    }


    @Override
    public boolean canEncodeImage(ImageTypeSpecifier type)
    {
        SampleModel sm = type.getSampleModel();
        if (sm.getNumBands() > 16384)
            return false;
        if (sm.getDataType() < DataBuffer.TYPE_BYTE ||
            sm.getDataType() > DataBuffer.TYPE_INT)
            return false;
        return true;
    }


    @Override
    public ImageWriter createWriterInstance(Object extension)
        throws IIOException
    {
        return new J2KImageWriter(this);
    }
}
