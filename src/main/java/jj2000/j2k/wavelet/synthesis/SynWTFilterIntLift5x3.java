/*
 * $RCSfile: SynWTFilterIntLift5x3.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:34 $
 * $State: Exp $
 *
 * Class:                   SynWTFilterIntLift5x3
 *
 * Description:             A synthetizing wavelet filter implementing the
 *                          lifting 5x3 transform.
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
 *  */
package jj2000.j2k.wavelet.synthesis;


/**
 * This class inherits from the synthesis wavelet filter definition for int
 * data. It implements the inverse wavelet transform specifically for the 5x3
 * filter. The implementation is based on the lifting scheme.
 *
 * <P>
 * See the SynWTFilter class for details such as normalization, how to
 * split odd-length signals, etc. In particular, this method assumes that the
 * low-pass coefficient is computed first.
 *
 * @see SynWTFilter
 * @see SynWTFilterInt
 */
public class SynWTFilterIntLift5x3 extends SynWTFilterInt
{

    /**
     * An implementation of the synthetize_lpf() method that works on int
     * data, for the inverse 5x3 wavelet transform using the lifting
     * scheme. See the general description of the synthetize_lpf() method in
     * the SynWTFilter class for more details.
     *
     * <P>
     * The coefficients of the first lifting step are [-1/4 1 -1/4].
     *
     * <P>
     * The coefficients of the second lifting step are [1/2 1 1/2].
     *
     * @param lowSig This is the array that contains the low-pass
     * input signal.
     *
     * @param lowOff This is the index in lowSig of the first sample to
     * filter.
     *
     * @param lowLen This is the number of samples in the low-pass
     * input signal to filter.
     *
     * @param lowStep This is the step, or interleave factor, of the
     * low-pass input signal samples in the lowSig array.
     *
     * @param highSig This is the array that contains the high-pass
     * input signal.
     *
     * @param highOff This is the index in highSig of the first sample to
     * filter.
     *
     * @param highLen This is the number of samples in the high-pass
     * input signal to filter.
     *
     * @param highStep This is the step, or interleave factor, of the
     * high-pass input signal samples in the highSig array.
     *
     * @param outSig This is the array where the output signal is
     * placed. It should be long enough to contain the output signal.
     *
     * @param outOff This is the index in outSig of the element where
     * to put the first output sample.
     *
     * @param outStep This is the step, or interleave factor, of the
     * output samples in the outSig array.
     *
     * @see SynWTFilter#synthetize_lpf
     */
    @Override
    public void synthetize_lpf(int[] lowSig, int lowOff, int lowLen, int lowStep,
        int[] highSig, int highOff, int highLen, int highStep,
        int[] outSig, int outOff, int outStep)
    {

        int i;
        int outLen = lowLen + highLen; //Length of the output signal
        int iStep = 2 * outStep; //Upsampling in outSig
        int ik; //Indexing outSig
        int lk; //Indexing lowSig
        int hk; //Indexing highSig

        /*
         *Generate even samples (inverse low-pass filter)
         */

        //Initialize counters
        lk = lowOff;
        hk = highOff;
        ik = outOff;

        //Handle tail boundary effect. Use symmetric extension.
        if (outLen > 1) {
            outSig[ik] = lowSig[lk] - ((highSig[hk] + 1) >> 1);
        }
        else {
            outSig[ik] = lowSig[lk];
        }

        lk += lowStep;
        hk += highStep;
        ik += iStep;

        //Apply lifting step to each "inner" sample.
        for (i = 2; i < outLen - 1; i += 2) {
            outSig[ik] = lowSig[lk] -
                ((highSig[hk - highStep] + highSig[hk] + 2) >> 2);

            lk += lowStep;
            hk += highStep;
            ik += iStep;
        }

        //Handle head boundary effect if input signal has odd length.
        if ((outLen % 2 == 1) && (outLen > 2)) {
            outSig[ik] = lowSig[lk] - ((2 * highSig[hk - highStep] + 2) >> 2);
        }

        /*
         *Generate odd samples (inverse high pass-filter)
         */

        //Initialize counters
        hk = highOff;
        ik = outOff + outStep;

        //Apply first lifting step to each "inner" sample.
        for (i = 1; i < outLen - 1; i += 2) {
            // Since signs are inversed (add instead of substract)
            // the +1 rounding dissapears.
            outSig[ik] = highSig[hk] +
                ((outSig[ik - outStep] + outSig[ik + outStep]) >> 1);

            hk += highStep;
            ik += iStep;
        }

        //Handle head boundary effect if input signal has even length.
        if (outLen % 2 == 0 && outLen > 1) {
            outSig[ik] = highSig[hk] + outSig[ik - outStep];
        }
    }


    /**
     * An implementation of the synthetize_hpf() method that works on int
     * data, for the inverse 5x3 wavelet transform using thelifting
     * scheme. See the general description of the synthetize_hpf() method in
     * the SynWTFilter class for more details.
     *
     * <P>
     * The coefficients of the first lifting step are [-1/4 1 -1/4].
     *
     * <P>
     * The coefficients of the second lifting step are [1/2 1 1/2].
     *
     * @param lowSig This is the array that contains the low-pass
     * input signal.
     *
     * @param lowOff This is the index in lowSig of the first sample to
     * filter.
     *
     * @param lowLen This is the number of samples in the low-pass
     * input signal to filter.
     *
     * @param lowStep This is the step, or interleave factor, of the
     * low-pass input signal samples in the lowSig array.
     *
     * @param highSig This is the array that contains the high-pass
     * input signal.
     *
     * @param highOff This is the index in highSig of the first sample to
     * filter.
     *
     * @param highLen This is the number of samples in the high-pass
     * input signal to filter.
     *
     * @param highStep This is the step, or interleave factor, of the
     * high-pass input signal samples in the highSig array.
     *
     * @param outSig This is the array where the output signal is
     * placed. It should be long enough to contain the output signal.
     *
     * @param outOff This is the index in outSig of the element where
     * to put the first output sample.
     *
     * @param outStep This is the step, or interleave factor, of the
     * output samples in the outSig array.
     *
     * @see SynWTFilter#synthetize_hpf
     */
    @Override
    public void synthetize_hpf(int[] lowSig, int lowOff, int lowLen, int lowStep,
        int[] highSig, int highOff, int highLen, int highStep,
        int[] outSig, int outOff, int outStep)
    {

        int i;
        int outLen = lowLen + highLen; //Length of the output signal
        int iStep = 2 * outStep; //Upsampling in outSig
        int ik; //Indexing outSig
        int lk; //Indexing lowSig
        int hk; //Indexing highSig

        /*
         *Generate even samples (inverse low-pass filter)
         */

        //Initialize counters
        lk = lowOff;
        hk = highOff;
        ik = outOff + outStep;

        //Apply lifting step to each "inner" sample.
        for (i = 1; i < outLen - 1; i += 2) {
            outSig[ik] = lowSig[lk] -
                ((highSig[hk] + highSig[hk + highStep] + 2) >> 2);

            lk += lowStep;
            hk += highStep;
            ik += iStep;
        }

        if ((outLen > 1) && (outLen % 2 == 0)) {
            // symmetric extension.
            outSig[ik] = lowSig[lk] - ((2 * highSig[hk] + 2) >> 2);
        }
        /*
         *Generate odd samples (inverse high pass-filter)
         */

        //Initialize counters
        hk = highOff;
        ik = outOff;

        if (outLen > 1) {
            outSig[ik] = highSig[hk] + outSig[ik + outStep];
        }
        else {
            // Normalize for Nyquist gain
            outSig[ik] = highSig[hk] >> 1;
        }

        hk += highStep;
        ik += iStep;

        //Apply first lifting step to each "inner" sample.
        for (i = 2; i < outLen - 1; i += 2) {
            // Since signs are inversed (add instead of substract)
            // the +1 rounding dissapears.
            outSig[ik] = highSig[hk] +
                ((outSig[ik - outStep] + outSig[ik + outStep]) >> 1);
            hk += highStep;
            ik += iStep;
        }

        //Handle head boundary effect if input signal has odd length.
        if (outLen % 2 == 1 && outLen > 1) {
            outSig[ik] = highSig[hk] + outSig[ik - outStep];
        }
    }


    /**
     * Returns the negative support of the low-pass analysis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * @return 2
     */
    @Override
    public int getAnLowNegSupport()
    {
        return 2;
    }


    /**
     * Returns the positive support of the low-pass analysis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * @return The number of taps of the low-pass analysis filter in the
     * positive direction
     */
    @Override
    public int getAnLowPosSupport()
    {
        return 2;
    }


    /**
     * Returns the negative support of the high-pass analysis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * @return The number of taps of the high-pass analysis filter in
     * the negative direction
     */
    @Override
    public int getAnHighNegSupport()
    {
        return 1;
    }


    /**
     * Returns the positive support of the high-pass analysis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * @return The number of taps of the high-pass analysis filter in
     * the positive direction
     */
    @Override
    public int getAnHighPosSupport()
    {
        return 1;
    }


    /**
     * Returns the negative support of the low-pass synthesis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * <P>
     * A MORE PRECISE DEFINITION IS NEEDED
     *
     * @return The number of taps of the low-pass synthesis filter in the
     * negative direction
     */
    @Override
    public int getSynLowNegSupport()
    {
        return 1;
    }


    /**
     * Returns the positive support of the low-pass synthesis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * <P>
     * A MORE PRECISE DEFINITION IS NEEDED
     *
     * @return The number of taps of the low-pass synthesis filter in the
     * positive direction
     */
    @Override
    public int getSynLowPosSupport()
    {
        return 1;
    }


    /**
     * Returns the negative support of the high-pass synthesis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * <P>
     * A MORE PRECISE DEFINITION IS NEEDED
     *
     * @return The number of taps of the high-pass synthesis filter in the
     * negative direction
     */
    @Override
    public int getSynHighNegSupport()
    {
        return 2;
    }


    /**
     * Returns the positive support of the high-pass synthesis filter. That is
     * the number of taps of the filter in the negative direction.
     *
     * <P>
     * A MORE PRECISE DEFINITION IS NEEDED
     *
     * @return The number of taps of the high-pass synthesis filter in the
     * positive direction
     */
    @Override
    public int getSynHighPosSupport()
    {
        return 2;
    }


    /**
     * Returns the implementation type of this filter, as defined in this
     * class, such as WT_FILTER_INT_LIFT, WT_FILTER_FLOAT_LIFT,
     * WT_FILTER_FLOAT_CONVOL.
     *
     * @return WT_FILTER_INT_LIFT.
     */
    @Override
    public int getImplType()
    {
        return WT_FILTER_INT_LIFT;
    }


    /**
     * Returns the reversibility of the filter. A filter is considered
     * reversible if it is suitable for lossless coding.
     *
     * @return true since the 5x3 is reversible, provided the appropriate
     * rounding is performed.
     */
    @Override
    public boolean isReversible()
    {
        return true;
    }


    /**
     * Returns true if the wavelet filter computes or uses the same "inner"
     * subband coefficient as the full frame wavelet transform, and false
     * otherwise. In particular, for block based transforms with reduced
     * overlap, this method should return false. The term "inner" indicates
     * that this applies only with respect to the coefficient that are not
     * affected by image boundaries processings such as symmetric extension,
     * since there is not reference method for this.
     *
     * <P>
     * The result depends on the length of the allowed overlap when
     * compared to the overlap required by the wavelet filter. It also depends
     * on how overlap processing is implemented in the wavelet filter.
     *
     * @param tailOvrlp This is the number of samples in the input signal
     * before the first sample to filter that can be used for overlap.
     *
     * @param headOvrlp This is the number of samples in the input signal
     * after the last sample to filter that can be used for overlap.
     *
     * @param inLen This is the lenght of the input signal to filter.The
     * required number of samples in the input signal after the last sample
     * depends on the length of the input signal.
     *
     * @return true if both overlaps are greater than 2, and correct
     * processing is applied in the analyze() method.
     */
    @Override
    public boolean isSameAsFullWT(int tailOvrlp, int headOvrlp, int inLen)
    {

        //If the input signal has even length.
        if (inLen % 2 == 0) {
            if (tailOvrlp >= 2 && headOvrlp >= 1) return true;
            else return false;
        }
        //Else if the input signal has odd length.
        else {
            if (tailOvrlp >= 2 && headOvrlp >= 2) return true;
            else return false;
        }
    }


    /**
     * Returns a string of information about the synthesis wavelet filter
     *
     * @return wavelet filter type.
     */
    @Override
    public String toString()
    {
        return "w5x3 (lifting)";
    }

}
