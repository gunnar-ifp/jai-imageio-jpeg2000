/*
 * $RCSfile: BitsPerComponentBox.java,v $
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
 * $Date: 2005/02/11 05:01:31 $
 * $State: Exp $
 */

package com.github.jaiimageio.jpeg2000.impl;

import javax.imageio.metadata.IIOInvalidTreeException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is defined to represent a Bits Per Component Box of JPEG
 * JP2 file format. A Bits Per Component box has a length, and a fixed
 * type of "bpcc". Its content is a byte array containing the bit
 * depths of the color components.
 *
 * This box is necessary only when the bit depth are not identical for all
 * the components.
 */
public class BitsPerComponentBox extends Box
{
    /**
     * Counstructs a <code>BitsPerComponentBox</code> from the provided
     * byte array containing the bit depths of each color component.
     */
    public BitsPerComponentBox(byte[] bitDepth)
    {
        super(8 + bitDepth.length, 0x62706363, null);
        data = bitDepth;
    }


    /**
     * Constructs a <code>BitsPerComponentBox</code> based on the provide
     * <code>org.w3c.dom.Node</code>.
     */
    public BitsPerComponentBox(Node node) throws IIOInvalidTreeException
    {
        super(node);
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String name = child.getNodeName();

            if ("BitDepth".equals(name)) {
                data = Box.getByteArrayElementValue(child);
            }
        }
    }


    /** Returns the bit depths for all the image components. */
    public byte[] getBitDepth()
    {
        return data;
    }
}
