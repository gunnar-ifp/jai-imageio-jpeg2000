/*
 * $RCSfile: XMLBox.java,v $
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
import org.w3c.dom.NodeList;

/**
 * This class is defined to represent a XML box of JPEG JP2
 * file format. This type of box has a length, a type of "xml ". Its
 * content is a text string of a XML instance.
 */
public class XMLBox extends Box
{
    /** Cache the element names for this box's xml definition */
    private static String[] elementNames = { "Content" };


    /**
     * This method will be called by the getNativeNodeForSimpleBox of the
     * class Box to get the element names.
     */
    public static String[] getElementNames()
    {
        return elementNames;
    }


    /** Create a Box from its content. */
    public XMLBox(byte[] data)
    {
        super(8 + data.length, 0x786D6C20, data);
    }


    /**
     * Constructs a <code>UUIDListBox</code> based on the provided
     * <code>org.w3c.dom.Node</code>.
     */
    public XMLBox(Node node) throws IIOInvalidTreeException
    {
        super(node);
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String name = child.getNodeName();

            if ("Content".equals(name)) {
                String value = child.getNodeValue();
                if (value != null)
                    data = value.getBytes();
                else if (child instanceof IIOMetadataNode) {
                    value = (String)((IIOMetadataNode)child).getUserObject();
                    if (value != null)
                        data = value.getBytes();
                }
            }
        }
    }


    /**
     * Creates an <code>IIOMetadataNode</code> from this XML
     * box. The format of this node is defined in the XML dtd and xsd
     * for the JP2 image file.
     */
    @Override
    public IIOMetadataNode getNativeNode()
    {
        try {
            IIOMetadataNode node = new IIOMetadataNode(Box.getName(getType()));
            setDefaultAttributes(node);
            IIOMetadataNode child = new IIOMetadataNode("Content");
            String value = null;
            if (data != null)
                value = new String(data);
            child.setUserObject(value);
            child.setNodeValue(value);
            node.appendChild(child);
            return node;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(I18N.getString("Box0"));
        }
    }
}
