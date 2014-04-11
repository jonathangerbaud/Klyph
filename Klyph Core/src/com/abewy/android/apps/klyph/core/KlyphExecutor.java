/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.core;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import android.os.AsyncTask;

public class KlyphExecutor
{
	private static volatile Executor executor;
	private static final Object LOCK = new Object();
	private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 128;
    private static final int DEFAULT_KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue<Runnable>(10);
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "KlyphCore Thread #" + counter.incrementAndGet());
        }
    };

	public static Executor getExecutor() {
        synchronized (LOCK) {
            if (KlyphExecutor.executor == null) {
                Executor executor = getAsyncTaskExecutor();
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAXIMUM_POOL_SIZE,
                            DEFAULT_KEEP_ALIVE, TimeUnit.SECONDS, DEFAULT_WORK_QUEUE, DEFAULT_THREAD_FACTORY);
                }
                KlyphExecutor.executor = executor;
            }
        }
        return KlyphExecutor.executor;
    }

    /**
     * Sets the Executor used by the SDK for non-AsyncTask background work.
     *
     * @param executor
     *          the Executor to use; must not be null.
     */
    public static void setExecutor(Executor executor) {
    	if (executor == null) {
            throw new NullPointerException("KlyphCore : Argument 'executor' cannot be null");
        }
        
        synchronized (LOCK) {
        	KlyphExecutor.executor = executor;
        }
    }
    
    private static Executor getAsyncTaskExecutor() {
        Field executorField = null;
        try {
            executorField = AsyncTask.class.getField("THREAD_POOL_EXECUTOR");
        } catch (NoSuchFieldException e) {
            return null;
        }

        if (executorField == null) {
            return null;
        }

        Object executorObject = null;
        try {
            executorObject = executorField.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }

        if (executorObject == null) {
            return null;
        }

        if (!(executorObject instanceof Executor)) {
            return null;
        }

        return (Executor) executorObject;
    }
}
