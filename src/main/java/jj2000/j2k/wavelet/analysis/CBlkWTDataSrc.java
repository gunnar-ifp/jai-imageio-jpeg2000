/*
 * $RCSfile: CBlkWTDataSrc.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:30 $
 * $State: Exp $
 *
 * Class:                   CBlkWTDataSrc
 *
 * Description:             Interface that define methods for transfer of WT
 *                          data in a code-block basis.
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

import jj2000.j2k.wavelet.WaveletTransform;

/**
 * This abstract class defines methods to transfer wavelet data in a
 * code-block by code-block basis. In each call to 'getNextCodeBlock()' or
 * 'getNextInternCodeBlock()' a new code-block is returned. The code-blocks
 * are returned in no specific order.
 *
 * <P>
 * This class is the source of data for the quantizer. See the 'Quantizer'
 * class.
 *
 * <P>
 * Note that no more of one object may request data, otherwise one object
 * would get some of the data and another one another part, in no defined
 * manner.
 *
 * @see ForwWTDataProps
 *
 * @see WaveletTransform
 *
 * @see jj2000.j2k.quantization.quantizer.CBlkQuantDataSrcEnc
 *
 * @see jj2000.j2k.quantization.quantizer.Quantizer
 */
public interface CBlkWTDataSrc extends ForwWTDataProps
{

    /**
     * Returns the position of the fixed point in the specified component, or
     * equivalently the number of fractional bits. This is the position of the
     * least significant integral (i.e. non-fractional) bit, which is
     * equivalent to the number of fractional bits. For instance, for
     * fixed-point values with 2 fractional bits, 2 is returned. For
     * floating-point data this value does not apply and 0 should be
     * returned. Position 0 is the position of the least significant bit in
     * the data.
     *
     * @param n The index of the component.
     *
     * @return The position of the fixed-point, which is the same as
     * the number of fractional bits. For floating-point data 0 is
     * returned.
     *
     *
     */
    public int getFixedPoint(int n);


    /**
     * Return the data type of this CBlkWTDataSrc for the given
     * component in the current tile. Its value should be either
     * DataBlk.TYPE_INT or DataBlk.TYPE_FLOAT but can change
     * according to the current tile-component.
     *
     * @param t Tile index
     *
     * @param c Component index
     *
     * @return Current data type
     *
     */
    public int getDataType(int t, int c);


    /**
     * Returns the next code-block in the current tile for the specified
     * component, as a copy (see below). The order in which code-blocks are
     * returned is not specified. However each code-block is returned only
     * once and all code-blocks will be returned if the method is called 'N'
     * times, where 'N' is the number of code-blocks in the tile. After all
     * the code-blocks have been returned for the current tile calls to this
     * method will return 'null'.
     *
     * <P>
     * When changing the current tile (through 'setTile()' or 'nextTile()')
     * this method will always return the first code-block, as if this method
     * was never called before for the new current tile.
     *
     * <P>
     * The data returned by this method is always a copy of the internal
     * data of this object, if any, and it can be modified "in place" without
     * any problems after being returned. The 'offset' of the returned data is
     * 0, and the 'scanw' is the same as the code-block width. The 'magbits'
     * of the returned data is not set by this method and should be
     * ignored. See the 'CBlkWTData' class.
     *
     * <P>
     * The 'ulx' and 'uly' members of the returned 'CBlkWTData' object
     * contain the coordinates of the top-left corner of the block, with
     * respect to the tile, not the subband.
     *
     * @param n The component for which to return the next code-block.
     *
     * @param cblk If non-null this object will be used to return the new
     * code-block. If null a new one will be allocated and returned. If the
     * "data" array of the object is non-null it will be reused, if possible,
     * to return the data.
     *
     * @return The next code-block in the current tile for component 'n', or
     * null if all code-blocks for the current tile have been returned.
     *
     * @see CBlkWTData
     *
     *
     */
    public abstract CBlkWTData getNextCodeBlock(int n, CBlkWTData cblk);


    /**
     * Returns the next code-block in the current tile for the specified
     * component. The order in which code-blocks are returned is not
     * specified. However each code-block is returned only once and all
     * code-blocks will be returned if the method is called 'N' times, where
     * 'N' is the number of code-blocks in the tile. After all the code-blocks
     * have been returned for the current tile calls to this method will
     * return 'null'.
     *
     * <P>
     * When changing the current tile (through 'setTile()' or 'nextTile()')
     * this method will always return the first code-block, as if this method
     * was never called before for the new current tile.
     *
     * <P>
     * The data returned by this method can be the data in the internal
     * buffer of this object, if any, and thus can not be modified by the
     * caller. The 'offset' and 'scanw' of the returned data can be
     * arbitrary. The 'magbits' of the returned data is not set by this method
     * and should be ignored. See the 'CBlkWTData' class.
     *
     * <P>
     * The 'ulx' and 'uly' members of the returned 'CBlkWTData' object
     * contain the coordinates of the top-left corner of the block, with
     * respect to the tile, not the subband.
     *
     * @param n The component for which to return the next code-block.
     *
     * @param cblk If non-null this object will be used to return the new
     * code-block. If null a new one will be allocated and returned. If the
     * "data" array of the object is non-null it will be reused, if possible,
     * to return the data.
     *
     * @return The next code-block in the current tile for component 'n', or
     * null if all code-blocks for the current tile have been returned.
     *
     * @see CBlkWTData
     *
     *
     */
    public abstract CBlkWTData getNextInternCodeBlock(int n, CBlkWTData cblk);

}
