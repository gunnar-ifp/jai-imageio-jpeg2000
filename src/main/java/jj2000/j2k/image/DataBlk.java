/*
 * $RCSfile: DataBlk.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:12 $
 * $State: Exp $
 *
 * Interface:           DataBlk
 *
 * Description:         A generic interface to hold 2D blocks of data.
 *
 *
 *
 * COPYRIGHT:
 *
 * This software module was originally developed by Raphaël Grosbois and
 * Diego Santa Cruz (Swiss Federal Institute of Technology-EPFL); Joel
 * Askelöf (Ericsson Radio Systems AB); and Bertrand Berthelot, David
 * Bouchard, Félix Henry, Gerard Mozelle and Patrice Onno (Canon Research
 * Centre France S.A) in the course of development of the JPEG2000
 * standard as specified by ISO/IEC 15444 (JPEG 2000 Standard). This
 * software module is an implementation of a part of the JPEG 2000
 * Standard. Swiss Federal Institute of Technology-EPFL, Ericsson Radio
 * Systems AB and Canon Research Centre France S.A (collectively JJ2000
 * Partners) agree not to assert against ISO/IEC and users of the JPEG
 * 2000 Standard (Users) any of their rights under the copyright, not
 * including other intellectual property rights, for this software module
 * with respect to the usage by ISO/IEC and Users of this software module
 * or modifications thereof for use in hardware or software products
 * claiming conformance to the JPEG 2000 Standard. Those intending to use
 * this software module in hardware or software products are advised that
 * their use may infringe existing patents. The original developers of
 * this software module, JJ2000 Partners and ISO/IEC assume no liability
 * for use of this software module or modifications thereof. No license
 * or right to this software module is granted for non JPEG 2000 Standard
 * conforming products. JJ2000 Partners have full right to use this
 * software module for his/her own purpose, assign or donate this
 * software module to any third party and to inhibit third parties from
 * using this software module for non JPEG 2000 Standard conforming
 * products. This copyright notice must be included in all copies or
 * derivative works of this software module.
 *
 * Copyright (c) 1999/2000 JJ2000 Partners.
 *
 *
 *
 */

package jj2000.j2k.image;

/**
 * This is a generic abstract class to store data from a block of an
 * image. This class does not have the notion of components. Therefore, it
 * should be used for data from a single component. Subclasses should
 * implement the different types of storage (<code>int</code>,
 * <code>float</code>, etc.).
 *
 * <P>
 * The data is always stored in one array, of the type matching the data
 * type (i.e. for 'int' it's an 'int[]'). The data should be stored in the
 * array in standard scan-line order. That is the samples go from the top-left
 * corner of the code-block to the lower-right corner by line and then column.
 *
 * <P>
 * The member variable 'offset' gives the index in the array of the first
 * data element (i.e. the top-left coefficient (ulx,uly)). The member variable
 * 'scanw' gives the width of the scan that is used to store the data, that
 * can be different from the width of the block. Element '(x,y)' of the
 * code-block (i.e. '(ulx,uly)' is the top-left coefficient), will appear at
 * position 'offset+(y-uly)*scanw+(x-ulx)' in the array of data.
 *
 * <P>
 * A block of data can have the <i>progressive</i> attribute
 * set. Data is progressive when it is obtained by successive
 * refinement and the values in this block are approximations of the
 * "final" values. When the final values are returned the progressive
 * attribute must be turned off.
 *
 * <P>
 * The classes <code>DataBlkInt</code> and <code>DataBlkFloat</code>
 * provide implementations for <code>int</code> and <code>float</code> types
 * respectively.
 *
 * @see DataBlkInt
 *
 * @see DataBlkFloat
 */
public abstract class DataBlk
{

    /**
     * The identifier for the <code>byte</code> data type, as signed 8
     * bits.
     */
    public final static int TYPE_BYTE = 0;

    /**
     * The identifier for the <code>short</code> data type, as signed 16
     * bits.
     */
    public final static int TYPE_SHORT = 1;

    /**
     * The identifier for the <code>int</code> data type, as signed 32
     * bits.
     */
    public final static int TYPE_INT = 3;

    /** The identifier for the <code>float</code> data type */
    public final static int TYPE_FLOAT = 4;

    /**
     * The horizontal coordinate (in pixels) of the upper-left corner
     * of the block of data. This is relative to the component of the
     * image from where this block was filled or is to be filled.
     */
    public int ulx;

    /**
     * The vertical coordinate of the upper-left corner of the block
     * of data. This is relative to the component of the image from where
     * this block was filled or is to be filled.
     */
    public int uly;

    /** The width of the block, in pixels. */
    public int w;

    /** The height of the block, in pixels. */
    public int h;

    /** The offset in the array of the top-left coefficient */
    public int offset;

    /** The width of the scanlines used to store the data in the array */
    public int scanw;

    /** The progressive attribute (<code>false</code> by default) */
    public boolean progressive;


    /**
     * Returns the size in bits, given the data type. The data type
     * must be one defined in this class. An
     * <code>IllegalArgumentException</code> is thrown if <code>type</code> is
     * not defined in this class.
     *
     * @param type The data type.
     *
     * @return The size in bits of the data type.
     *
     *
     *
     */
    public static int getSize(int type)
    {
        switch (type) {
            case TYPE_BYTE:
                return 8;
            case TYPE_SHORT:
                return 16;
            case TYPE_INT:
            case TYPE_FLOAT:
                return 32;
            default:
                throw new IllegalArgumentException();
        }
    }


    /**
     * Returns the data type of the <code>DataBlk</code> object, as
     * defined in this class.
     *
     * @return The data type of the object, as defined in thsi class.
     *
     *
     *
     */
    public abstract int getDataType();


    /**
     * Returns the array containing the data, or null if there is no
     * data. The returned array is of the type returned by
     * <code>getDataType()</code> (e.g., for <code>TYPE_INT</code>, it is a
     * <code>int[]</code>).
     *
     * <P>
     * Each implementing class should provide a type specific
     * equivalent method (e.g., <code>getDataInt()</code> in
     * <code>DataBlkInt</code>) which returns an array of the correct
     * type explicetely and not through an <code>Object</code>.
     *
     * @return The array containing the data, or <code>null</code> if
     * there is no data.
     *
     *
     *
     * @see #getDataType
     *
     */
    public abstract Object getData();


    /**
     * Sets the data array to the specified one. The type of the
     * specified data array must match the one returned by
     * <code>getDataType()</code> (e.g., for <code>TYPE_INT</code>, it should
     * be a <code>int[]</code>). If the wrong type of array is given a
     * <code>ClassCastException</code> will be thrown.
     *
     * <P>
     * The size of the array is not necessarily checked for
     * consistency with <code>w</code> and <code>h</code> or any other fields.
     *
     * <P>
     * Each implementing class should provide a type specific
     * equivalent method (e.g., <code>setDataInt()</code> in
     * <code>DataBlkInt</code>) which takes an array of the correct
     * type explicetely and not through an <code>Object</code>.
     *
     * @param arr The new data array to use
     *
     *
     *
     * @see #getDataType
     */
    public abstract void setData(Object arr);


    /**
     * Returns a string of informations about the DataBlk
     *
     * @return Block dimensions and progressiveness in a string
     *
     *
     */
    @Override
    public String toString()
    {
        String typeString = "";
        switch (getDataType()) {
            case TYPE_BYTE:
                typeString = "Unsigned Byte";
                break;
            case TYPE_SHORT:
                typeString = "Short";
                break;
            case TYPE_INT:
                typeString = "Integer";
                break;
            case TYPE_FLOAT:
                typeString = "Float";
                break;
        }

        return "DataBlk: " +
            "upper-left(" + ulx + "," + uly + "), width= " + w +
            ", height= " + h + ", progressive= " + progressive +
            ", offset= " + offset + ", scanw= " + scanw +
            ", type= " + typeString;
    }
}
