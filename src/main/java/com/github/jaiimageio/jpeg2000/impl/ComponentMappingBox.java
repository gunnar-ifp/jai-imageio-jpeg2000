/*
 * $RCSfile: ComponentMappingBox.java,v $
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
 * This class is defined to represent a Color Specification Box of JPEG JP2
 * file format. A Channel Definition Box has a length, and a fixed type
 * of "cmap". This box exists if and only is a PaletteBox exists. Its
 * content defines the type LUT output components and their mapping to the
 * color component.
 */
public class ComponentMappingBox extends Box
{
    /** The data elements. */
    private short[] components;
    private byte[] type;
    private byte[] map;


    /**
     * Constructs a <code>ComponentMappingBox</code> from the provided
     * content byte array.
     */
    public ComponentMappingBox(byte[] data)
    {
        super(8 + data.length, 0x636D6170, data);
    }


    /**
     * Constructs a <code>ComponentMappingBox</code> from the provided
     * component mapping.
     */
    public ComponentMappingBox(short[] comp, byte[] t, byte[] m)
    {
        super(8 + (comp.length << 2), 0x636D6170, null);
        this.components = comp;
        this.type = t;
        this.map = m;
    }


    /**
     * Constructs a <code>ComponentMappingBox</code> based on the provided
     * <code>org.w3c.dom.Node</code>.
     */
    public ComponentMappingBox(Node node) throws IIOInvalidTreeException
    {
        super(node);
        NodeList children = node.getChildNodes();
        int len = children.getLength() / 3;
        components = new short[len];
        type = new byte[len];
        map = new byte[len];

        len *= 3;
        int index = 0;

        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            String name = child.getNodeName();

            if ("Component".equals(name)) {
                components[index] = Box.getShortElementValue(child);
            }

            if ("ComponentType".equals(name)) {
                type[index] = Box.getByteElementValue(child);
            }

            if ("ComponentAssociation".equals(name)) {
                map[index++] = Box.getByteElementValue(child);
            }
        }
    }


    /** Parse the component mapping from the provided content data array. */
    @Override
    protected void parse(byte[] data)
    {
        int len = data.length / 4;
        components = new short[len];
        type = new byte[len];
        map = new byte[len];

        for (int i = 0, j = 0; i < len; i++) {
            components[i] = (short)(((data[j++] & 0xFF) << 8) | (data[j++] & 0xFF));
            type[i] = data[j++];
            map[i] = data[j++];
        }
    }


    /**
     * Creates an <code>IIOMetadataNode</code> from this component mapping
     * box. The format of this node is defined in the XML dtd and xsd
     * for the JP2 image file.
     */
    @Override
    public IIOMetadataNode getNativeNode()
    {
        IIOMetadataNode node = new IIOMetadataNode(Box.getName(getType()));
        setDefaultAttributes(node);

        for (int i = 0; i < components.length; i++) {
            IIOMetadataNode child = new IIOMetadataNode("Component");
            Short obj = Short.valueOf(components[i]);
            child.setUserObject(Short.valueOf(components[i]));
            child.setNodeValue("" + components[i]);
            node.appendChild(child);

            child = new IIOMetadataNode("ComponentType");
            child.setUserObject(Byte.valueOf(type[i]));
            child.setNodeValue("" + type[i]);
            node.appendChild(child);

            child = new IIOMetadataNode("ComponentAssociation");
            child.setUserObject(Byte.valueOf(map[i]));
            child.setNodeValue("" + map[i]);
            node.appendChild(child);
        }

        return node;
    }


    public short[] getComponent()
    {
        return components;
    }


    public byte[] getComponentType()
    {
        return type;
    }


    public byte[] getComponentAssociation()
    {
        return map;
    }


    @Override
    protected void compose()
    {
        if (data != null)
            return;
        data = new byte[type.length << 2];

        for (int i = 0, j = 0; i < type.length; i++) {
            data[j++] = (byte)(components[i] >> 8);
            data[j++] = (byte)(components[i] & 0xFF);
            data[j++] = type[i];
            data[j++] = map[i];
        }
    }
}
