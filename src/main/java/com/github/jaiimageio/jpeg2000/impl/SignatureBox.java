/*
 * $RCSfile: SignatureBox.java,v $
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
 * $Date: 2005/02/11 05:01:37 $
 * $State: Exp $
 */
package com.github.jaiimageio.jpeg2000.impl;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;

/**
 * This class is defined to represent a Signature Box of JPEG JP2
 * file format. This type of box has a fixed length of 12, a type of "jP "
 * and a four byte content of {@code 0x0D0A870A}, which is used to detects of the
 * common file transmission errors which substitutes {@literal <CR><LF>} with {@literal <LF>} or
 * vice versa.
 */
public class SignatureBox extends Box
{
    /** Constructs a <code>SignatureBox</code>. */
    public SignatureBox()
    {
        super(12, 0x6A502020, null);
    }


    /**
     * Constructs a <code>SignatureBox</code> based on the provided
     * <code>org.w3c.dom.Node</code>.
     */
    public SignatureBox(Node node) throws IIOInvalidTreeException
    {
        super(node);
    }


    /**
     * Constructs a <code>SignatureBox</code> based on the provided
     * byte array.
     */
    public SignatureBox(byte[] data) throws IIOInvalidTreeException
    {
        super(12, 0x6A502020, data);
    }


    /**
     * Creates an <code>IIOMetadataNode</code> from this signature
     * box. The format of this node is defined in the XML dtd and xsd
     * for the JP2 image file.
     */
    @Override
    public IIOMetadataNode getNativeNode()
    {
        IIOMetadataNode node = new IIOMetadataNode(Box.getName(getType()));
        setDefaultAttributes(node);
        node.setAttribute("Signature", Integer.toString(0x0D0A870A));
        return node;
    }


    @Override
    protected void compose()
    {
        if (data != null)
            return;
        data = new byte[] { (byte)0x0D, (byte)0x0A, (byte)0x87, (byte)0x0A };
    }
}

