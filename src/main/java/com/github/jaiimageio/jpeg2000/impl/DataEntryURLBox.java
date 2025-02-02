/*
 * $RCSfile: DataEntryURLBox.java,v $
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

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is defined to represent a Data Entry URL Box of JPEG JP2
 * file format. A Data Entry URL Box has a length, and a fixed type
 * of "url ". Its content are a one-byte version, a three-byte flags and
 * a URL pertains to the UUID List box within its UUID Info superbox.
 */
public class DataEntryURLBox extends Box
{
    /** Cache the element names for this box's xml definition */
    private static String[] elementNames = { "Version", "Flags", "URL" };


    /**
     * This method will be called by the getNativeNodeForSimpleBox of the
     * class Box to get the element names.
     */
    public static String[] getElementNames()
    {
        return elementNames;
    }

    /** The element values. */
    private byte version;
    private byte[] flags;
    private String url;


    /** Constructs a <code>DataEntryURLBox</code> from its content data. */
    public DataEntryURLBox(byte[] data)
    {
        super(8 + data.length, 0x75726C20, data);
    }


    /** Constructs a <code>DataEntryURLBox</code> from its data elements. */
    public DataEntryURLBox(byte version, byte[] flags, String url)
    {
        super(12 + url.length(), 0x75726C20, null);
        this.version = version;
        this.flags = flags;
        this.url = url;
    }


    /** Constructs a <code>DataEntryURLBox</code> from a Node. */
    public DataEntryURLBox(Node node) throws IIOInvalidTreeException
    {
        super(node);
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String name = child.getNodeName();

            if ("Version".equals(name)) {
                version = Box.getByteElementValue(child);
            }

            if ("Flags".equals(name)) {
                flags = Box.getByteArrayElementValue(child);
            }

            if ("URL".equals(name)) {
                url = Box.getStringElementValue(child);
            }
        }
    }


    /** Parses the content of this box from its content byte array. */
    @Override
    protected void parse(byte[] data)
    {
        version = data[0];
        flags = new byte[3];
        flags[0] = data[1];
        flags[1] = data[2];
        flags[2] = data[3];

        url = new String(data, 4, data.length - 4);
    }


    /**
     * Creates an <code>IIOMetadataNode</code> from this data entry URL
     * box. The format of this node is defined in the XML dtd and xsd
     * for the JP2 image file.
     */
    @Override
    public IIOMetadataNode getNativeNode()
    {
        return getNativeNodeForSimpleBox();
    }


    /** Returns the <code>Version</code> data element. */
    public byte getVersion()
    {
        return version;
    }


    /** Returns the <code>Flags</code> data element. */
    public byte[] getFlags()
    {
        return flags;
    }


    /** Returns the <code>URL</code> data element. */
    public String getURL()
    {
        return url;
    }


    @Override
    protected void compose()
    {
        if (data != null)
            return;
        data = new byte[4 + url.length()];

        data[0] = version;
        data[1] = flags[0];
        data[2] = flags[1];
        data[3] = flags[2];
        System.arraycopy(url.getBytes(), 0, data, 4, url.length());
    }
}
