/*
 * $RCSfile: CBlkWTDataInt.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:30 $
 * $State: Exp $
 *
 * Class:                   CBlkWTDataInt
 *
 * Description:             Implementation of CBlkWTData for 'int' data
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


package jj2000.j2k.wavelet.analysis;

import jj2000.j2k.image.DataBlk;

/**
 * This is an implementation of the 'CBlkWTData' abstract class for signed 32
 * bit integer data.
 *
 * <P>
 * The methods in this class are declared final, so that they can
 * be inlined by inlining compilers.
 *
 * @see CBlkWTData
 */
public class CBlkWTDataInt extends CBlkWTData
{

    /** The array where the data is stored */
    public int[] data;


    /**
     * Returns the data type of this object, always DataBlk.TYPE_INT.
     *
     * @return The data type of the object, always DataBlk.TYPE_INT
     *
     *
     *
     */
    @Override
    public final int getDataType()
    {
        return DataBlk.TYPE_INT;
    }


    /**
     * Returns the array containing the data, or null if there is no
     * data array. The returned array is an int array.
     *
     * @return The array of data (a int[]) or null if there is no
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
    public final int[] getDataInt()
    {
        return data;
    }


    /**
     * Sets the data array to the specified one. The provided array
     * must be a int array, otherwise a ClassCastException is
     * thrown. The size of the array is not checked for consistency
     * with the code-block dimensions.
     *
     * @param arr The data array to use. Must be an int array.
     *
     *
     */
    @Override
    public final void setData(Object arr)
    {
        data = (int[])arr;
    }


    /**
     * Sets the data array to the specified one. The size of the array
     * is not checked for consistency with the code-block dimensions. This
     * method is more efficient than 'setData()'.
     *
     * @param arr The data array to use.
     *
     *
     */
    public final void setDataInt(int[] arr)
    {
        data = arr;
    }

}
