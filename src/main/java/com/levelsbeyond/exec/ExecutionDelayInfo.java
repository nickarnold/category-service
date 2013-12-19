package com.levelsbeyond.exec;

import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see java.util.concurrent.Delayed implementation that works a bit backwards from how Delayed might ususally work.  Rather
 * than delaying the start of an execution, it starts tracking how long an execution has actually been running,
 * so the delay time calculation is <i>(startTime + maxTime) - currentTime</i>.  A negative result means that
 * the execution has run too long and therefore should be canceled.
 * 
 * @author davelamy
 *
 * @param <T>
 */
public class ExecutionDelayInfo<T> implements Delayed {
	private static final Logger log = LoggerFactory.getLogger(ExecutionDelayInfo.class);
	private String executionId;
	private long creationTime = System.currentTimeMillis();
	private long maxTimeMillis;
	private Future<T> future;

	public ExecutionDelayInfo(String executionId, Future<T> future, long maxTimeMillis) {
		this.executionId = executionId;
		
		this.future = future;
		this.maxTimeMillis = maxTimeMillis;
	}
	
	/**
	 * Returns the execution ID being tracked for timeout.
	 * @return
	 */
	public String getExecutionId() {
		return executionId;
	}

	/**
	 * Returns the Future associated with this execution, so that it can be cancelled if it times out.
	 * @return
	 */
	public Future<T> getFuture() {
		return future;
	}
	
	/**
	 * Returns the configured max time that this execution may run.
	 * @return
	 */
	public long getMaxTimeMillis() {
		return maxTimeMillis;
	}
	
	/**
	 * Returns the start time of this execution in milliseconds
	 * @return
	 */
	public long getStartTime() {
		return creationTime;
	}
	
	@Override
	public int compareTo(Delayed delayed) {
		if (delayed == this) return 0;
		
		return (int)(getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS)); 
	}

	@Override
	public long getDelay(TimeUnit timeUnit) {
		// if the future is complete just return -1 so we can remove this from the queue
		if (future.isDone()) {
			log.info("Execution " + executionId + " is complete, returning delay of -1.");
			return -1;
		}
		
		long startTime = timeUnit.convert(creationTime, TimeUnit.MILLISECONDS);
		long maxTime = startTime + timeUnit.convert(maxTimeMillis, TimeUnit.MILLISECONDS);
		long currTime = timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		
		long retVal = maxTime - currTime;
		log.info("Execution " + executionId + " delay = " + retVal + " (time unit " + timeUnit.name() + ")");
		return retVal;
	}
	
}
