/*
 * $RCSfile: BinaryDataOutput.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:15 $
 * $State: Exp $
 *
 * Interface:           BinaryDataOutput
 *
 * Description:         Stream like interface for bit as well as byte
 *                      level output to a stream or file.
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

package jj2000.j2k.io;

import java.io.IOException;

/**
 * This interface defines the output of binary data to streams and/or files.
 *
 * <P>
 * Byte level output (i.e., for byte, int, long, float, etc.) should
 * always be byte aligned. For example, a request to write an
 * <code>int</code> should always realign the output at the byte level.
 *
 * <P>
 * The implementation of this interface should clearly define if
 * multi-byte output data is written in little- or big-endian byte
 * ordering (least significant byte first or most significant byte
 * first, respectively).
 *
 * @see EndianType
 */
public interface BinaryDataOutput
{

    /**
     * Should write the byte value of <code>v</code> (i.e., 8 least
     * significant bits) to the output. Prior to writing, the output
     * should be realigned at the byte level.
     *
     * <P>
     * Signed or unsigned data can be written. To write a signed
     * value just pass the <code>byte</code> value as an argument. To
     * write unsigned data pass the <code>int</code> value as an argument
     * (it will be automatically casted, and only the 8 least
     * significant bits will be written).
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeByte(int v) throws IOException;


    /**
     * Should write the short value of <code>v</code> (i.e., 16 least
     * significant bits) to the output. Prior to writing, the output
     * should be realigned at the byte level.
     *
     * <P>
     * Signed or unsigned data can be written. To write a signed
     * value just pass the <code>short</code> value as an argument. To
     * write unsigned data pass the <code>int</code> value as an argument
     * (it will be automatically casted, and only the 16 least
     * significant bits will be written).
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeShort(int v) throws IOException;


    /**
     * Should write the int value of <code>v</code> (i.e., the 32 bits) to
     * the output. Prior to writing, the output should be realigned at
     * the byte level.
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeInt(int v) throws IOException;


    /**
     * Should write the long value of <code>v</code> (i.e., the 64 bits)
     * to the output. Prior to writing, the output should be realigned
     * at the byte level.
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeLong(long v) throws IOException;


    /**
     * Should write the IEEE float value <code>v</code> (i.e., 32 bits) to
     * the output. Prior to writing, the output should be realigned at
     * the byte level.
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeFloat(float v) throws IOException;


    /**
     * Should write the IEEE double value <code>v</code> (i.e., 64 bits)
     * to the output. Prior to writing, the output should be realigned
     * at the byte level.
     *
     * @param v The value to write to the output
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     *
     */
    public void writeDouble(double v) throws IOException;


    /**
     * Returns the endianness (i.e., byte ordering) of the implementing
     * class. Note that an implementing class may implement only one
     * type of endianness or both, which would be decided at creatiuon
     * time.
     *
     * @return Either <code>EndianType.BIG_ENDIAN</code> or
     * <code>EndianType.LITTLE_ENDIAN</code>
     *
     * @see EndianType
     *
     *
     *
     */
    public int getByteOrdering();


    /**
     * Any data that has been buffered must be written, and the stream should
     * be realigned at the byte level.
     *
     * @exception IOException If an I/O error ocurred.
     *
     *
     */
    public void flush() throws IOException;
}
