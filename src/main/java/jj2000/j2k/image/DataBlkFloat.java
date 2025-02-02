/*
 * $RCSfile: DataBlkFloat.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:12 $
 * $State: Exp $
 *
 * Interface:           DataBlkFloat
 *
 * Description:         A float implementation of DataBlk
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
 * This is an implementation of the <code>DataBlk</code> interface for
 * 32 bit floating point data (float).
 *
 * <P>
 * The methods in this class are declared final, so that they can
 * be inlined by inlining compilers.
 *
 * @see DataBlk
 */
public class DataBlkFloat extends DataBlk
{
    /** The array where the data is stored */
    public float[] data;


    /**
     * Creates a DataBlkFloat with 0 dimensions and no data array
     * (i.e. data is null).
     *
     *
     *
     */
    public DataBlkFloat()
    {
    }


    /**
     * Creates a DataBlkFloat with the specified dimensions and
     * position. The data array is initialized to an array of size
     * w*h.
     *
     * @param ulx The horizontal coordinate of the upper-left corner
     * of the block
     *
     * @param uly The vertical coordinate of the upper-left corner
     * of the block
     *
     * @param w The width of the block (in pixels)
     *
     * @param h The height of the block (in pixels)
     *
     *
     */
    public DataBlkFloat(int ulx, int uly, int w, int h)
    {
        this.ulx = ulx;
        this.uly = uly;
        this.w = w;
        this.h = h;
        offset = 0;
        scanw = w;
        data = new float[w * h];
    }


    /**
     * Copy constructor.
     * Creates a DataBlkFloat which is the copy of the DataBlkFloat
     * given as paramter.
     *
     * @param src the object to be copied.
     *
     *
     */
    public DataBlkFloat(DataBlkFloat src)
    {
        this.ulx = src.ulx;
        this.uly = src.uly;
        this.w = src.w;
        this.h = src.h;
        this.offset = 0;
        this.scanw = this.w;
        this.data = new float[this.w * this.h];
        for (int i = 0; i < this.h; i++)
            System.arraycopy(src.data, i * src.scanw,
                this.data, i * this.scanw, this.w);
    }


    /**
     * Returns the identifier of this data type, <code>TYPE_FLOAT</code>,
     * as defined in <code>DataBlk</code>.
     *
     * @return The type of data stored. Always
     * <code>DataBlk.TYPE_FLOAT</code>
     *
     * @see DataBlk#TYPE_FLOAT
     *
     *
     */
    @Override
    public final int getDataType()
    {
        return TYPE_FLOAT;
    }


    /**
     * Returns the array containing the data, or null if there is no
     * data array. The returned array is a float array.
     *
     * @return The array of data (a float[]) or null if there is no
     * data.
     *
     *
     *
     */
    @Override
    public final Object getData()
    {
        return data;
    }


    /**
     * Returns the array containing the data, or null if there is no
     * data array.
     *
     * @return The array of data or null if there is no data.
     *
     *
     */
    public final float[] getDataFloat()
    {
        return data;
    }


    /**
     * Sets the data array to the specified one. The provided array
     * must be a float array, otherwise a ClassCastException is
     * thrown. The size of the array is not checked for consistency
     * with the block's dimensions.
     *
     * @param arr The data array to use. Must be a float array.
     *
     *
     */
    @Override
    public final void setData(Object arr)
    {
        data = (float[])arr;
    }


    /**
     * Sets the data array to the specified one. The size of the array
     * is not checked for consistency with the block's dimensions.
     *
     * @param arr The data array to use.
     *
     *
     */
    public final void setDataFloat(float[] arr)
    {
        data = arr;
    }


    /**
     * Returns a string of informations about the DataBlkInt.
     */
    @Override
    public String toString()
    {
        String str = super.toString();
        if (data != null) {
            str += ",data=" + data.length + " bytes";
        }
        return str;
    }
}
