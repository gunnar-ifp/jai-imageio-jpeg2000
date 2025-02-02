/*
 * $RCSfile: J2KRenderedImage.java,v $
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
 * $Date: 2006/09/28 00:57:57 $
 * $State: Exp $
 */
package com.github.jaiimageio.jpeg2000.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

import jj2000.j2k.codestream.reader.HeaderDecoder;

import com.github.jaiimageio.impl.common.SimpleRenderedImage;

public class J2KRenderedImage extends SimpleRenderedImage
{
    private Raster currentTile;
    private Point currentTileGrid;

    private J2KReadState readState;


    public J2KRenderedImage(ImageInputStream iis,
        J2KImageReadParamJava param,
        J2KMetadata metadata,
        J2KImageReader reader) throws IOException
    {
        this(new J2KReadState(iis, param, metadata, reader));
    }


    public J2KRenderedImage(ImageInputStream iis,
        J2KImageReadParamJava param,
        J2KImageReader reader) throws IOException
    {
        this(new J2KReadState(iis, param, reader));
    }


    public J2KRenderedImage(J2KReadState readState)
    {
        this.readState = readState;

        HeaderDecoder hd = readState.getHeader();

        //determnined by destination
        Rectangle destinationRegion = readState.getDestinationRegion();
        width = destinationRegion.width;
        height = destinationRegion.height;
        minX = destinationRegion.x;
        minY = destinationRegion.y;

        Rectangle tile0Rect = readState.getTile0Rect();
        tileWidth = tile0Rect.width;
        tileHeight = tile0Rect.height;
        tileGridXOffset = tile0Rect.x;
        tileGridYOffset = tile0Rect.y;

        sampleModel = readState.getSampleModel();
        colorModel = readState.getColorModel();
    }


    @Override
    public synchronized Raster getTile(int tileX, int tileY)
    {
        if (currentTile != null &&
            currentTileGrid.x == tileX &&
            currentTileGrid.y == tileY)
            return currentTile;

        if (tileX >= getNumXTiles() || tileY >= getNumYTiles())
            throw new IllegalArgumentException(I18N.getString("J2KReadState1"));

        try {
            int x = tileXToX(tileX);
            int y = tileYToY(tileY);
            currentTile = Raster.createWritableRaster(sampleModel,
                new Point(x, y));
            currentTile = readState.getTile(tileX,
                tileY,
                (WritableRaster)currentTile);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (currentTileGrid == null)
            currentTileGrid = new Point(tileX, tileY);
        else {
            currentTileGrid.x = tileX;
            currentTileGrid.y = tileY;
        }

        return currentTile;
    }
}
