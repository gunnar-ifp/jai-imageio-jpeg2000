/*
 * $RCSfile: InverseWT.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:32 $
 * $State: Exp $
 *
 * Class:                   InverseWT
 *
 * Description:             This interface defines the specifics
 *                          of inverse wavelet transforms.
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
package jj2000.j2k.wavelet.synthesis;

import jj2000.j2k.decoder.DecoderSpecs;
import jj2000.j2k.image.BlkImgDataSrc;

/**
 * This abstract class extends the WaveletTransform one with the specifics of
 * inverse wavelet transforms.
 *
 * <P>
 * The image can be reconstructed at different resolution levels. This is
 * controlled by the setResLevel() method. All the image, tile and component
 * dimensions are relative the the resolution level being used. The number of
 * resolution levels indicates the number of wavelet recompositions that will
 * be used, if it is equal as the number of decomposition levels then the full
 * resolution image is reconstructed.
 *
 * <P>
 * It is assumed in this class that all tiles and components the same
 * reconstruction resolution level. If that where not the case the
 * implementing class should have additional data structures to store those
 * values for each tile. However, the 'recResLvl' member variable always
 * contain the values applicable to the current tile, since many methods
 * implemented here rely on them.
 */
public abstract class InverseWT extends InvWTAdapter
    implements BlkImgDataSrc
{

    /**
     * Initializes this object with the given source of wavelet
     * coefficients. It initializes the resolution level for full resolutioin
     * reconstruction (i.e. the maximum resolution available from the 'src'
     * source).
     *
     * <P>
     * It is assumed here that all tiles and components have the same
     * reconstruction resolution level. If that was not the case it should be
     * the value for the current tile of the source.
     *
     * @param src from where the wavelet coefficinets should be obtained.
     *
     * @param decSpec The decoder specifications
     */
    public InverseWT(MultiResImgData src, DecoderSpecs decSpec)
    {
        super(src, decSpec);
    }


    /**
     * Creates an InverseWT object that works on the data type of the source,
     * with the special additional parameters from the parameter
     * list. Currently the parameter list is ignored since no special
     * parameters can be specified for the inverse wavelet transform yet.
     *
     * @param src The source of data for the inverse wavelet
     * transform.
     *
     * @param decSpec The parameter list containing parameters applicable to the
     * inverse wavelet transform (other parameters can also be present).
     */
    public static InverseWT createInstance(CBlkWTDataSrcDec src,
        DecoderSpecs decSpec)
    {

        // full page wavelet transform
        return new InvWTFull(src, decSpec);
    }
}
