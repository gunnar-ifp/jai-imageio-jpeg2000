/*
 * $RCSfile: EntropyCoder.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:08 $
 * $State: Exp $
 *
 * Class:                   EntropyCoder
 *
 * Description:             The abstract class for entropy encoders
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
 * */
package jj2000.j2k.entropy.encoder;

import jj2000.j2k.StringSpec;
import jj2000.j2k.entropy.CBlkSizeSpec;
import jj2000.j2k.entropy.PrecinctSizeSpec;
import jj2000.j2k.entropy.StdEntropyCoderOptions;
import jj2000.j2k.image.ImgDataAdapter;
import jj2000.j2k.quantization.quantizer.CBlkQuantDataSrcEnc;
import jj2000.j2k.quantization.quantizer.Quantizer;
import jj2000.j2k.wavelet.Subband;
import jj2000.j2k.wavelet.analysis.SubbandAn;

import com.github.jaiimageio.jpeg2000.impl.J2KImageWriteParamJava;

/**
 * This abstract class provides the general interface for block-based entropy
 * encoders. The input to the entropy coder is the quantized wavelet
 * coefficients, or codewords, represented in sign magnitude. The output is a
 * compressed code-block with rate-distortion information.
 *
 * <P>
 * The source of data for objects of this class are 'CBlkQuantDataSrcEnc'
 * objects.
 *
 * <P>
 * For more details on the sign magnitude representation used see the
 * Quantizer class.
 *
 * <P>
 * This class provides default implemenations for most of the methods
 * (wherever it makes sense), under the assumption that the image and
 * component dimensions, and the tiles, are not modifed by the entropy
 * coder. If that is not the case for a particular implementation then the
 * methods should be overriden.
 *
 * @see Quantizer
 * @see CBlkQuantDataSrcEnc
 */
public abstract class EntropyCoder extends ImgDataAdapter
    implements CodedCBlkDataSrcEnc, StdEntropyCoderOptions
{

    /** The prefix for entropy coder options: 'C' */
    public final static char OPT_PREFIX = 'C';

    /**
     * The list of parameters that is accepted for entropy coding. Options
     * for entropy coding start with 'C'.
     */
    private final static String[][] pinfo = {
        { "Cblksiz", "[<tile-component idx>] <width> <height> " +
            "[[<tile-component idx>] <width> <height>]",
            "Specifies the maximum code-block size to use for tile-component. " +
                "The maximum width and height is 1024, however the surface area " +
                "(i.e. width x height) must not exceed 4096. The minimum width and " +
                "height is 4.",
            "64 64" },
        { "Cbypass", "[<tile-component idx>] true|false" +
            "[ [<tile-component idx>] true|false ...]",
            "Uses the lazy coding mode with the entropy coder. This will bypass " +
                "the MQ coder for some of the coding passes, where the distribution " +
                "is often close to uniform. Since the MQ codeword will be " +
                "terminated " +
                "at least once per lazy pass, it is important to use an efficient " +
                "termination algorithm, see the 'Cterm' option." +
                "'true' enables, 'false' disables it.",
            "false" },
        { "CresetMQ", "[<tile-component idx>] true|false" +
            "[ [<tile-component idx>] true|false ...]",
            "If this is enabled the probability estimates of the MQ coder are " +
                "reset after each arithmetically coded (i.e. non-lazy) coding pass. " +
                "'true' enables, 'false' disables it.",
            "false" },
        { "Creg_term", "[<tile-component idx>] true|false" +
            "[ [<tile-component idx>] true|false ...]",
            "If this is enabled the codeword (raw or MQ) is terminated on a " +
                "byte boundary after each coding pass. In this case it is important " +
                "to use an efficient termination algorithm, see the 'Cterm' option. " +
                "'true' enables, 'false' disables it.",
            "false" },
        { "Ccausal", "[<tile-component idx>] true|false" +
            "[ [<tile-component idx>] true|false ...]",
            "Uses vertically stripe causal context formation. If this is " +
                "enabled " +
                "the context formation process in one stripe is independant of the " +
                "next stripe (i.e. the one below it). 'true' " +
                "enables, 'false' disables it.",
            "false" },
        { "Cseg_symbol", "[<tile-component idx>] true|false" +
            "[ [<tile-component idx>] true|false ...]",
            "Inserts an error resilience segmentation symbol in the MQ " +
                "codeword at the end of " +
                "each bit-plane (cleanup pass). Decoders can use this " +
                "information to detect and " +
                "conceal errors.'true' enables, 'false' disables " +
                "it.",
            "false" },
        { "Cterm", "[<tile-component idx>] near_opt|easy|predict|full" +
            "[ [<tile-component idx>] near_opt|easy|predict|full ...]",
            "Specifies the algorithm used to terminate the MQ codeword. " +
                "The most efficient one is 'near_opt', which delivers a codeword " +
                "which in almost all cases is the shortest possible. The 'easy' is " +
                "a simpler algorithm that delivers a codeword length that is close " +
                "to the previous one (in average 1 bit longer). The 'predict' is" +
                " almost " +
                "the same as the 'easy' but it leaves error resilient information " +
                "on " +
                "the spare least significant bits (in average 3.5 bits), which can " +
                "be used by a decoder to detect errors. The 'full' algorithm " +
                "performs a full flush of the MQ coder and is highly inefficient.\n" +
                "It is important to use a good termination policy since the MQ " +
                "codeword can be terminated quite often, specially if the 'Cbypass'" +
                " or " +
                "'Creg_term' options are enabled (in the normal case it would be " +
                "terminated once per code-block, while if 'Creg_term' is specified " +
                "it will be done almost 3 times per bit-plane in each code-block).",
            "near_opt" },
        { "Clen_calc", "[<tile-component idx>] near_opt|lazy_good|lazy" +
            "[ [<tile-component idx>] ...]",
            "Specifies the algorithm to use in calculating the necessary MQ " +
                "length for each decoding pass. The best one is 'near_opt', which " +
                "performs a rather sophisticated calculation and provides the best " +
                "results. The 'lazy_good' and 'lazy' are very simple algorithms " +
                "that " +
                "provide rather conservative results, 'lazy_good' one being " +
                "slightly " +
                "better. Do not change this option unless you want to experiment " +
                "the effect of different length calculation algorithms.",
            "near_opt" },
        { "Cpp", "[<tile-component idx>] <dim> <dim> [<dim> <dim>] " +
            "[ [<tile-component idx>] ...]",
            "Specifies precinct partition dimensions for tile-component. The " +
                "first " +
                "two values apply to the highest resolution and the following ones " +
                "(if " +
                "any) apply to the remaining resolutions in decreasing order. If " +
                "less " +
                "values than the number of decomposition levels are specified, " +
                "then the " +
                "last two values are used for the remaining resolutions.",
            null },
    };

    /** The source of quantized wavelet coefficients */
    protected CBlkQuantDataSrcEnc src;


    /**
     * Initializes the source of quantized wavelet coefficients.
     *
     * @param src The source of quantized wavelet coefficients.
     */
    public EntropyCoder(CBlkQuantDataSrcEnc src)
    {
        super(src);
        this.src = src;
    }


    /**
     * Returns the code-block width for the specified tile and component.
     *
     * @param t The tile index
     *
     * @param c the component index
     *
     * @return The code-block width for the specified tile and component
     */
    public abstract int getCBlkWidth(int t, int c);


    /**
     * Returns the code-block height for the specified tile and component.
     *
     * @param t The tile index
     *
     * @param c the component index
     *
     * @return The code-block height for the specified tile and component
     */
    public abstract int getCBlkHeight(int t, int c);


    /**
     * Returns the reversibility of the tile-component data that is provided
     * by the object. Data is reversible when it is suitable for lossless and
     * lossy-to-lossless compression.
     *
     * <P>
     * Since entropy coders themselves are always reversible, it returns
     * the reversibility of the data that comes from the 'CBlkQuantDataSrcEnc'
     * source object (i.e. ROIScaler).
     *
     * @param t Tile index
     *
     * @param c Component index
     *
     * @return true is the data is reversible, false if not.
     *
     * @see jj2000.j2k.roi.encoder.ROIScaler
     */
    @Override
    public boolean isReversible(int t, int c)
    {
        return src.isReversible(t, c);
    }


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
    @Override
    public SubbandAn getAnSubbandTree(int t, int c)
    {
        return src.getAnSubbandTree(t, c);
    }


    /**
     * Returns the horizontal offset of the code-block partition. Allowable
     * values are 0 and 1, nothing else.
     */
    @Override
    public int getCbULX()
    {
        return src.getCbULX();
    }


    /**
     * Returns the vertical offset of the code-block partition. Allowable
     * values are 0 and 1, nothing else.
     */
    @Override
    public int getCbULY()
    {
        return src.getCbULY();
    }


    /**
     * Returns the parameters that are used in this class and
     * implementing classes. It returns a 2D String array. Each of the
     * 1D arrays is for a different option, and they have 3
     * elements. The first element is the option name, the second one
     * is the synopsis, the third one is a long description of what
     * the parameter is and the fourth is its default value. The
     * synopsis or description may be 'null', in which case it is
     * assumed that there is no synopsis or description of the option,
     * respectively. Null may be returned if no options are supported.
     *
     * @return the options name, their synopsis and their explanation,
     * or null if no options are supported.
     */
    public static String[][] getParameterInfo()
    {
        return pinfo;
    }


    /**
     * Creates a EntropyCoder object for the appropriate entropy coding
     * parameters in the parameter list 'pl', and having 'src' as the source
     * of quantized data.
     *
     * @param src The source of data to be entropy coded
     *
     * @param wp The parameter list (or options).
     *
     * @param cblks Code-block size specifications
     *
     * @param pss Precinct partition specifications
     *
     * @param bms By-pass mode specifications
     *
     * @param mqrs MQ-reset specifications
     *
     * @param rts Regular termination specifications
     *
     * @param css Causal stripes specifications
     *
     * @param sss Error resolution segment symbol use specifications
     *
     * @param lcs Length computation specifications
     *
     * @param tts Termination type specifications
     *
     * @exception IllegalArgumentException If an error occurs while parsing
     * the options in 'pl'
     */
    public static EntropyCoder createInstance(CBlkQuantDataSrcEnc src,
        J2KImageWriteParamJava wp,
        CBlkSizeSpec cblks,
        PrecinctSizeSpec pss,
        StringSpec bms, StringSpec mqrs,
        StringSpec rts, StringSpec css,
        StringSpec sss, StringSpec lcs,
        StringSpec tts)
    {
        // Check parameters
        //pl.checkList(OPT_PREFIX,pl.toNameArray(pinfo));
        return new StdEntropyCoder(src, cblks, pss, bms, mqrs, rts, css, sss, lcs, tts);
    }
}
