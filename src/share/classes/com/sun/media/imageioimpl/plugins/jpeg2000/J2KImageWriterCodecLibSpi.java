/*
 * $RCSfile: J2KImageWriterCodecLibSpi.java,v $
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
 * $Date: 2005-02-11 05:01:35 $
 * $State: Exp $
 */
package com.sun.media.imageioimpl.plugins.jpeg2000;

import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;

import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.spi.IIORegistry;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.IIOException;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.util.Locale;

import com.sun.media.imageioimpl.common.PackageUtil;

public class J2KImageWriterCodecLibSpi extends ImageWriterSpi {
    private static String [] readerSpiNames =
        {"com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderCodecLibSpi"};
    private static String[] formatNames =
        {"jpeg 2000", "JPEG 2000", "jpeg2000", "JPEG2000"};
    private static String[] extensions =
        {"jp2"}; // Should add jpx or jpm
    private static String[] mimeTypes = {"image/jp2", "image/jpeg2000"};

    public J2KImageWriterCodecLibSpi() {
        super(PackageUtil.getVendor(),
              PackageUtil.getVersion(),
              formatNames,
              extensions,
              mimeTypes,
              "com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterCodecLib",
              STANDARD_OUTPUT_TYPE,
              readerSpiNames,
              false,
              null, null,
              null, null,
              true,
              "com_sun_media_imageio_plugins_jpeg2000_image_1.0",
              "com.sun.media.imageioimpl.plugins.jpeg2000.J2KMetadataFormat",
              null, null);
    }

    public String getDescription(Locale locale) {
        return "codecLib JPEG 2000 Image Writer";
    }

    public void onRegistration(ServiceRegistry registry,
                               Class category) {
        // Branch based on codecLib availability.
        if(!PackageUtil.isCodecLibAvailable()) {
            // Deregister provider.
            registry.deregisterServiceProvider(this);
        } else {
            // Set pairwise ordering to give codecLib writer precedence.
            Class javaWriterSPIClass = null;
            try {
                javaWriterSPIClass =
                    Class.forName("com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi");
            } catch(Throwable t) {
                // Ignore it.
            }

            if(javaWriterSPIClass != null) {
                Object javaWriterSPI =
                    registry.getServiceProviderByClass(javaWriterSPIClass);
                if(javaWriterSPI != null) {
                    registry.setOrdering(category, this, javaWriterSPI);
                }
            }
        }
    }

    public boolean canEncodeImage(ImageTypeSpecifier type) {
        SampleModel sm = type.getSampleModel();
        if (sm.getNumBands() > 16384)
            return false;
        if (sm.getDataType() < DataBuffer.TYPE_BYTE ||
            sm.getDataType() > DataBuffer.TYPE_INT)
            return false;
        return true;
    }

    public ImageWriter createWriterInstance(Object extension)
        throws IIOException {
        return new J2KImageWriterCodecLib(this);
    }
}