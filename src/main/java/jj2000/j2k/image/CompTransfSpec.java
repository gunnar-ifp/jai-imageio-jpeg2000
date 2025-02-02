/*
 * $RCSfile: CompTransfSpec.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:11 $
 * $State: Exp $
 *
 * Class:                   CompTransfSpec
 *
 * Description:             Component Transformation specification
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

import jj2000.j2k.ModuleSpec;
import jj2000.j2k.image.invcomptransf.InvCompTransf;

/**
 * This class extends ModuleSpec class in order to hold tile
 * specifications for component transformation
 *
 * @see ModuleSpec
 *
 */
public class CompTransfSpec extends ModuleSpec
{

    /**
     * Constructs an empty 'CompTransfSpec' with specified number of
     * tile and components. This constructor is called by the
     * decoder. Note: The number of component is here for symmetry
     * purpose. It is useless since only tile specifications are
     * meaningful.
     *
     * @param nt Number of tiles
     *
     * @param nc Number of components
     *
     * @param type the type of the specification module i.e. tile specific,
     * component specific or both.
     *
     */
    public CompTransfSpec(int nt, int nc, byte type)
    {
        super(nt, nc, type);
    }


    /**
     * Check if component transformation is used in any of the tiles. This
     * method must not be used by the encoder.
     *
     * @return True if a component transformation is used in at least on
     * tile.
     *
     */
    public boolean isCompTransfUsed()
    {
        if (((Integer)def).intValue() != InvCompTransf.NONE) {
            return true;
        }

        if (tileDef != null) {
            for (int t = nTiles - 1; t >= 0; t--) {
                if (tileDef[t] != null &&
                    (((Integer)tileDef[t]).intValue() != InvCompTransf.NONE)) {
                    return true;
                }
            }
        }
        return false;
    }
}
