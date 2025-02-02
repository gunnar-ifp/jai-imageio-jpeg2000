/*
 * $RCSfile: FacilityManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:02:25 $
 * $State: Exp $
 *
 * Class:                   MsgLoggerManager
 *
 * Description:             Manages common facilities across threads
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


package jj2000.j2k.util;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages common facilities for mutithreading environments, It can
 * register different facilities for each thread, and also a default one, so
 * that they can be referred by static methods, while possibly having
 * different ones for different threads. Also a default facility exists that
 * is used for threads for which no particular facility has been registerd
 * registered.
 *
 * <P>
 * Currently the only kind of facilities managed is MsgLogger.
 *
 * <P>
 * An example use of this class is if 2 instances of a decoder are running
 * in different threads and the messages of the 2 instances should be
 * separated.
 *
 * <P>
 * The default MsgLogger is a StreamMsgLogger that uses System.out as
 * the 'out' stream and System.err as the 'err' stream, and a line width of
 * 78. This can be changed using the registerMsgLogger() method.
 *
 * @see MsgLogger
 *
 * @see StreamMsgLogger
 */
public class FacilityManager
{

    private static final StreamMsgLogger DEFAULT_LOGGER = new StreamMsgLogger(System.out, System.err, 78);

    /** The loggers associated to different threads */
    private final static Map<Thread, MsgLogger> loggerList = Collections.synchronizedMap(new WeakHashMap<Thread, MsgLogger>());

    /** The default logger, for threads that have none associated with them */
    private static volatile MsgLogger defMsgLogger = DEFAULT_LOGGER;

    /** The ProgressWatch instance associated to different threads */
    private final static Map<Thread, ProgressWatch> watchProgList = Collections.synchronizedMap(new WeakHashMap<Thread, ProgressWatch>());

    /**
     * The default ProgressWatch for threads that have none
     * associated with them.
     */
    private static volatile ProgressWatch defWatchProg = null;


    /**
     * Register the ProgressWatch for the given thread.
     * <p>
     * If any other logging facility was registered with the
     * given thread, it is overridden. If the Thread is null then 'ml' is taken
     * as the default message logger that is used for threads that have no
     * ProgressWatch registered.
     * <p>
     * To unregister, use {@link #unregisterProgressWatch(Thread)}
     *
     * @param t The thread to associate with progress watcher
     * @param pw The ProgressWatch to associate with thread
     *
     */
    public static void registerProgressWatch(Thread t, ProgressWatch pw)
    {
        if (pw == null) {
            throw new NullPointerException();
        }
        if (t == null) {
            defWatchProg = pw;
        }
        else {
            watchProgList.put(t, pw);
        }
    }


    /**
     * Unregister the ProgressWatch previously registered for the given thread.
     * <p>
     * If t is null, the default progress watch is unregistered.
     *
     * @see #registerProgressWatch(Thread, ProgressWatch)
     * @param t
     * Thread to unregister progress watch for, or <code>null</code>
     * to unregister the default progress watch.
     */
    public static void unregisterProgressWatch(Thread t)
    {
        if (t == null) {
            defWatchProg = null;
        }
        else {
            watchProgList.remove(t);
        }
    }


    /**
     * Return the ProgressWatch instance registered with the current
     * thread (the thread that calls this method). If the current
     * thread has no registered ProgressWatch, then the default one is used.
     *
     * @see #registerProgressWatch(Thread, ProgressWatch)
     */
    public static ProgressWatch getProgressWatch()
    {
        ProgressWatch pw = watchProgList.get(Thread.currentThread());
        return (pw == null) ? defWatchProg : pw;
    }


    /**
     * Register MsgLogger 'ml' as the logging facility of the given thread.
     * <p>
     * If any other logging facility was registered with the thread, it is
     * overriden. If the Thread is <code>null</code>, then the given message
     * logger will be set as the default for threads that have no MsgLogger
     * registered.
     *
     * @see #unregisterMsgLogger(Thread)
     * @param t
     * The thread to associate a MsgLogger for
     * @param ml
     * The MsgLogger to associate
     */
    public static void registerMsgLogger(Thread t, MsgLogger ml)
    {
        if (ml == null) {
            throw new NullPointerException();
        }
        if (t == null) {
            defMsgLogger = ml;
        }
        else {
            loggerList.put(t, ml);
        }
    }


    /**
     * Unregister the MsgLogger previously registered for the given thread.
     * <p>
     * If the Thread is <code>null</code>, then the default logger is reset to a
     * {@link StreamMsgLogger} using {@link System#out} and {@link System#err}.
     *
     * @see #registerMsgLogger(Thread, MsgLogger)
     *
     * @param t
     * The thread to remove the MsgLogger for, or <code>null</code>
     * to reset the default message logger.
     */
    public static void unregisterMsgLogger(Thread t)
    {
        if (t == null) {
            defMsgLogger = DEFAULT_LOGGER;
        }
        else {
            loggerList.remove(t);
        }
    }


    /**
     * Return the MsgLogger registered with the current thread (the thread that
     * calls this method).
     * <p>
     * If the current thread has no registered {@link MsgLogger} then the default
     * message logger is returned.
     *
     * @see #registerMsgLogger(Thread, MsgLogger)
     * @see #getMsgLogger(Thread)
     * @return The MsgLogger registered for the current thread, or the default
     * one if there is none registered for it.
     *
     *
     */
    public static MsgLogger getMsgLogger()
    {
        MsgLogger ml = loggerList.get(Thread.currentThread());
        return (ml == null) ? defMsgLogger : ml;
    }


    /**
     * Return the MsgLogger registered with the thread 't'.
     * <p>
     * If the thread 't' has no registered {@link MsgLogger}, then the default
     * message logger is returned.
     *
     * @param t
     * The thread for which to return the MsgLogger
     * @return The MsgLogger registered for the current thread, or the default
     * one if there is none registered for it.
     */
    public static MsgLogger getMsgLogger(Thread t)
    {
        MsgLogger ml = loggerList.get(t);
        return (ml == null) ? defMsgLogger : ml;
    }
}
