/*
 * $RCSfile: ForwWTDataProps.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:30 $
 * $State: Exp $
 *
 * Class:                   ForwWTDataProps
 *
 * Description:             Extends ImgData with forward wavelet specific
 *                          things.
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

import jj2000.j2k.image.ImgData;
import jj2000.j2k.wavelet.Subband;

/**
 * This interface extends the ImgData interface with methods that are
 * necessary for forward wavelet data (i.e. data that is produced by a forward
 * wavelet transform).
 */
public interface ForwWTDataProps extends ImgData
{

    /**
     * Returns the reversibility of the given tile-component. Data is
     * reversible when it is suitable for lossless and
     * lossy-to-lossless compression.
     *
     * @param t Tile index
     *
     * @param c Component index
     *
     * @return true is the data is reversible, false if not.
     *
     *
     */
    public boolean isReversible(int t, int c);


    /**
     * Returns a reference to the root of subband tree structure representing
     * the subband decomposition for the specified tile-component.
     *
     * @param t The index of the tile.
     *
     * @param c The index of the component.
     *
     * @return The root of the subband tree structure, see Subband.
     *
     * @see SubbandAn
     *
     * @see Subband
     */
    public SubbandAn getAnSubbandTree(int t, int c);


    /**
     * Returns the horizontal offset of the code-block partition. Allowable
     * values are 0 and 1, nothing else.
     */
    public int getCbULX();


    /**
     * Returns the vertical offset of the code-block partition. Allowable
     * values are 0 and 1, nothing else.
     */
    public int getCbULY();
}
