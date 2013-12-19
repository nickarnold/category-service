package com.levelsbeyond.exec;

import java.util.Date;
import java.util.concurrent.Callable;

import com.levelsbeyond.exec.Server.ExecutionResult;

/**
 * For remote execution of command line executables, such as episode 6's episodeectl. You may deploy this with a
 * properties file exec-server.properties which specifies the arg0 executable to be run. Otherwise, clients will be
 * required to provide arg0 in the arguments they send.
 * 
 * This class isn't capable of handling non-text output from processes, it will get mangled in the output stream.
 */
public class Server implements Callable<ExecutionResult> {
//	private static final Log log = LogFactory.getLog(Server.class);
//
//	private static long DEFAULT_TIMEOUT_MILLIS = 1000 * 30 * 60; // 30 minutes
//	private static int DEFAULT_MAX_THREADS = 3;
//	private static int CANCELLED_CODE = 500;
//	private static int DEFAULT_TIMEOUT_CHECK_INTERVAL = 1000 * 3; // 3 seconds 
//	
//	private static final String DEFAULT_EXECUTABLE_PROPERTY = "executable";
//	private static final String DEFAULT_TIMEOUT_PROPERTY = "timeout.millis";
//	private static final String TIMEOUT_CHECK_INTERVAL_PROPERTY = "timeout.checkInterval";
//	private static final String SERVER_PORT_PROPERTY = "port";
//	private static final String NUM_THREADS_PROPERTY = "numThreads";
//	private static final String DEBUG_PROPERTY = "debug";
//	private static String COMMAND_PROPERTY_PREFIX = "executable.command";	
//	
//	private static String jarPath;
//	private static Properties props;
//	private static XmlOptions xmlOptions;
//	
//	private static ThreadPoolExecutor threadPoolExecutor;	
//	private static CompletionService<ExecutionResult> executorService;
//	
//	private static boolean debug = false;
//	private static String defaultCommand;
//	private static long defaultTimeout = DEFAULT_TIMEOUT_MILLIS;
//	private static long timeoutCheckInterval = DEFAULT_TIMEOUT_CHECK_INTERVAL;
//
//	
//	private static Map<String, String> executableCommandMap = new HashMap<String, String>();
//	
//	// used to tie the future to the server ID.  Since execs are not guaranteed to start right away (could be blocked in the executor's queue),
//	// we only want to start tracking the execution for timeout once it starts.  This is temporary storage between those two points.
//	private static Map<String, Future<ExecutionResult>> queuedExecs = new ConcurrentHashMap<String, Future<ExecutionResult>>();
//	
//	// used to track executions that have started, the queue will pop once one takes too long
//	private static DelayQueue<ExecutionDelayInfo> timeoutQueue= new DelayQueue<ExecutionDelayInfo>();
//
//	private StrSubstitutor variableSubstitutor = new StrSubstitutor(new StrLookup() {
//		@Override
//		public String lookup(String name) {
//			String value = props.getProperty(name);
//			if (value == null) {
//				value = StrLookup.systemPropertiesLookup().lookup(name);
//				if (value == null) {
//					value = "";
//				}
//			}
//			return value;
//		}
//	});
//
//	public static void main(String args[]) throws Exception {
//		int port = 10101;
//		xmlOptions = new XmlOptions();
//		Map<String, String> namespaceMap = new TreeMap<String, String>();
//		namespaceMap.put("", "http://levelsbeyond.com/exec");
//		xmlOptions.setLoadSubstituteNamespaces(namespaceMap);
//		xmlOptions.setSaveOuter();
//		xmlOptions.setUseDefaultNamespace();
//
////		RollingFileAppender r = (RollingFileAppender) Logger.getRootLogger().getAllAppenders().getAppender("logfile");
////		Logger.getRootLogger().removeAppender(Logger.getRootLogger().getAppender("logfile"));
////		r.setFile(getJarPath() + File.separator + (r.getFile() == null ? "exec-server.log" : r.getFile()));
////		r.activateOptions();
////		Logger.getRootLogger().addAppender(r);
////		log.info("Logging to " + r.getFile());
//		props = new Properties();
//		props.load(ClassLoader.getSystemResourceAsStream("default.exec-server.properties"));
//		try {
//			props.load(new FileInputStream(getJarPath() + File.separator + "exec-server.properties"));
//		} catch (Exception e) {
//			log.warn("Couldn't locate exec-server.properties");
//		}
//
//		buildCommands(props);
//		
//		TimeoutChecker timeoutChecker = new TimeoutChecker();
//		new Thread(timeoutChecker, "timeout-checker").start();
//		
//		try {
//			if (props.containsKey(DEFAULT_EXECUTABLE_PROPERTY)) {
//				log.info("Setting defaultCommand to path:  " + props.getProperty(DEFAULT_EXECUTABLE_PROPERTY));
//				defaultCommand = props.getProperty(DEFAULT_EXECUTABLE_PROPERTY);
//				log.debug("defaultCommand now = " + defaultCommand);
//			}
//			if (props.containsKey(SERVER_PORT_PROPERTY)) {
//				port = Integer.parseInt(props.getProperty(SERVER_PORT_PROPERTY));
//			}
//			if (props.containsKey(DEBUG_PROPERTY)) {
//				debug = Boolean.parseBoolean(props.getProperty(DEBUG_PROPERTY));
//				log.info("Debug logging = " + debug);
//			}
//			if (props.containsKey(DEFAULT_TIMEOUT_PROPERTY)) {
//				defaultTimeout = Long.valueOf(props.getProperty(DEFAULT_TIMEOUT_PROPERTY));
//				log.info("Command timeout (in milliseconds) = " + defaultTimeout);
//			}
//			if (props.containsKey(TIMEOUT_CHECK_INTERVAL_PROPERTY)) {
//				timeoutCheckInterval = Long.valueOf(props.getProperty(TIMEOUT_CHECK_INTERVAL_PROPERTY));
//				log.info("timeout check interval (in milliseconds) = " + timeoutCheckInterval);
//			}
//			
//			int maxThreads = DEFAULT_MAX_THREADS;
//			if (props.containsKey(NUM_THREADS_PROPERTY)) {
//				maxThreads = Integer.parseInt(props.getProperty(NUM_THREADS_PROPERTY));			
//			} 
//			
//			threadPoolExecutor = new ThreadPoolExecutor(maxThreads, maxThreads,
//					20L, TimeUnit.SECONDS,
//					new LinkedBlockingQueue<Runnable>()			
//					);
//			
//			executorService = new ExecutorCompletionService<ExecutionResult>(threadPoolExecutor);
//			
//			log.info("Listening on port " + port);
//			try {
//				ServerSocket ssock = new ServerSocket(port);
//
//				while (true) {
//					// if the main thread has been interrupted, shutdown the executor and GTFO
//					if (Thread.currentThread().isInterrupted()) {
//						threadPoolExecutor.shutdownNow();
//						return;
//					}
//					Socket socket = ssock.accept();
//					
//					// Server constructor will now read the instruction immediately
//					// so that we can tell how it should be run (sync/async)
//					Server server = new Server(socket);
//					// STUD-739:  Only submit async instructions to the executor service
//					// sync instructions should just be handled inline
//					if (server.isAsyncInstruction()) {					
//						Future<ExecutionResult> result = executorService.submit(server);
//						// add the future to our queued map, this will allow us to
//						// find the future once it starts and start watching for
//						// timeouts
//						queuedExecs.put(server.getUuid(), result);
//					}
//					else {
//						try {
//							server.handleSyncRequest();
//						}
//						catch (Exception ex) {
//							log.error("Unable to handle sync request:  " + server.getInstructionText(), ex);
//						}
//					}
//				}
//			} catch (Exception e) {
//				log.error("Caught exception listening on " + port, e);
//			}
//		} catch (Exception e) {
//			log.error("Failed to launch - bad property value.", e);
//		}
//	}
//
//	private static void buildCommands(Properties props) {
//		executableCommandMap.clear();
//		Map<String, String> commandPropertyMap = new HashMap<String, String>();
//
//		for (Object propObj : props.keySet()) {
//			String testName = (String) propObj;
//			if (testName.toLowerCase().startsWith(COMMAND_PROPERTY_PREFIX.toLowerCase())) {
//				if (!commandPropertyMap.containsKey(testName)) {
//					commandPropertyMap.put(testName, props.getProperty(testName));
//				}
//			}
//		}
//
//		for (String commandProperty : commandPropertyMap.keySet()) {
//			String commandKey = null;
//			String commandPath = null;
//			if (commandProperty.startsWith(COMMAND_PROPERTY_PREFIX + ".")) {
//				commandKey = commandProperty.substring(COMMAND_PROPERTY_PREFIX.length() + 1); // +1 for the dot
//				commandPath = commandPropertyMap.get(commandProperty);
//			}
//
//			// build up the list of available commands by Key
//			executableCommandMap.put(commandKey, commandPath);
//
//			log.info("Registered command " + commandKey + " to exe path " + commandPath + ".");
//		}
//	}
//	
//	/** 
//	 * This method should be called by the Server instance on call() so that we can track timeouts based on actual run time.
//	 * @param executionId
//	 */
//	public static void executionStarted(String executionId) {
//		// clear this execution from the queued map
//		Future<ExecutionResult> queuedExec = queuedExecs.remove(executionId);
//		if (queuedExec != null) {
//			log.info("Execution " + executionId + " has started, watching for timeout (" + defaultTimeout + " millis..)");
//			log.info("There are " + queuedExecs.size() + " more queued executions..");
//			ExecutionDelayInfo<ExecutionResult> delayInfo = new ExecutionDelayInfo<Server.ExecutionResult>(executionId, queuedExec, defaultTimeout);
//			timeoutQueue.add(delayInfo);
//		}
//		else {
//			log.error("ERROR CONDITION:  Could not find future for execution id " + executionId);
//		}
//	}
//
//	public static Collection<String> getCommandKeys() {
//		ArrayList<String> keys = new ArrayList<String>();
//		for (String key : executableCommandMap.keySet()) {
//			keys.add(key);
//		}
//		return keys;
//	}
//	
//	private String uuid;
//	private Socket sock;
//	private XmlObject instruction;
//	private ExecutionResult executionResult;
//
//	Server(Socket sock) throws Exception {
//		this.uuid = UUID.randomUUID().toString();
//		this.sock = sock;
//		
//		executionResult = new ExecutionResult();
//		
//		// read in and parse the request data immediately so that
//		// we can make a decision about how to execute it
//		instruction = initInstruction();
//	}
//	
//	public String getUuid() {
//		return uuid;
//	}
//	
//	public ExecutionResult getExecutionResult() {
//		return executionResult;
//	}
//	
//	public XmlObject initInstruction() throws Exception {
//		DataInputStream bis = new DataInputStream(sock.getInputStream()); // have to do this shit because
//		int rqstLen = bis.readInt(); // f'in sock.shutdownOutput closes the whole socket
//		if (rqstLen > 30000) {
//			throw new Exception("Suspicious rqstLen received..  Bailing.");
//		}
//		byte[] rqstBuf = new byte[rqstLen];
//		bis.read(rqstBuf, 0, rqstLen);
//		XmlObject instruction = XmlObject.Factory.parse(new String(rqstBuf, "UTF-8"), xmlOptions);
//		
//		return instruction;
//	}
//	
//	/**
//	 * Returns the execution mode.  Only returns async for actual exec requests.  All other request
//	 * types are informational/metric related and should be run sync.
//	 * @return
//	 */
//	public boolean isAsyncInstruction() {
//		return (instruction instanceof ExecClientRequestDocument);
//	}
//	
//	public String getInstructionText() {
//		return instruction.xmlText(xmlOptions);		
//	}

	@Override
	public ExecutionResult call() {
//		try {
//			executionResult.startTime = new Date();
//			
//			// calling executionStarted will start timeout checking for this execution instance
//			executionStarted(this.uuid);
//			
//			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
//			
//			ExecServerResponse response = handleClientRequest(((ExecClientRequestDocument) instruction).getExecClientRequest(), dos);
//			executionResult.response = response;
//
//			writeResponse(response, dos);												
//		}
//		catch (Exception e) {
//			log.error("Caught exception producing output", e);
//			executionResult.exception = e;
//		} 
//		finally {
//			executionResult.finishedTime = new Date();
//			
//			if (sock != null) {
//				try {
//					sock.close();
//				} catch (Exception e) {
//				}
//			}
//		}
//		
		return null;
	}

//	private void writeResponse(XmlObject response, DataOutputStream dos) throws Exception {
//		byte[] bytes = response.xmlText(xmlOptions).getBytes("UTF-8");
//		synchronized (dos) {
//			dos.writeInt(bytes.length);
//			dos.write(bytes);
//		}		
//	}
//	
//	private ExecServerResponse handleClientRequest(ExecClientRequest rqst, DataOutputStream dos) throws InterruptedException {
//		boolean shouldLog = true;
//		int progressLineBufferSize = rqst.isSetProgressLineBufferSize() ? rqst.getProgressLineBufferSize() : 0;
//		boolean monitorErrorOutput = rqst.isSetMonitorErrorOutput() ? rqst.getMonitorErrorOutput() : false;
//		ArrayList<String> inputArgs = new ArrayList<String>();
//		ExecServerResponse rsp = ExecServerResponseDocument.Factory.newInstance().addNewExecServerResponse();
//
//		try {
//			shouldLog = rqst.getShouldLog() || debug;
//			if (shouldLog && rqst.isSetDescription()) {
//				log.info(variableSubstitutor.replace(rqst.getDescription()));
//			}
//
//			for (String arg : rqst.getArgArray()) {
//				inputArgs.add(variableSubstitutor.replace(arg));
//			}
//
//			String requestCommand = variableSubstitutor.replace(rqst.getCommand());
//			String path = null;
//			// need to read command key from the request and look up the path to the command in our property map
//			if (requestCommand != null && requestCommand.length() > 0 && !executableCommandMap.containsKey(requestCommand)) {
//				log.error("No command matches the request command key: " + requestCommand);
//				rsp.addStderr("No command matches the request command key: " + requestCommand);
//			} else if (requestCommand == null || requestCommand.length() == 0) {
//				path = defaultCommand;
//				log.info("No command key sent in request, using default command: " + defaultCommand);
//			} else {
//				path = executableCommandMap.get(requestCommand);
//			}
//
//			if (path != null && path.length() > 0) {
//				// Make the first argument the fully qualified path to the command
//				inputArgs.add(0, path);
//
//				String[] envp = getAddEnv(rqst.getEnvArray());
//
//				if (shouldLog) {
//					String args = "";
//					for (String arg : inputArgs) {
//						args += arg + " ";
//					}
//					log.info("Executing: " + args);
//					if (envp != null) {
//						log.info("Environment:");
//						for (String env : envp) {
//							log.info(env);
//						}
//					}
//				}
//
//				Process process = null;
//								
//				ProcessStreamHandler processOutputHandler = null;
//				ProcessStreamHandler processErrorHandler = null;
//				
//				try {
//					process = Runtime.getRuntime().exec(inputArgs.toArray(new String[inputArgs.size()]), envp);
//					processOutputHandler = new ProcessStreamHandler(this.uuid, process.getInputStream(), "OUT");
//					processErrorHandler = new ProcessStreamHandler(this.uuid, process.getErrorStream(), "ERR");
//					
//					if (progressLineBufferSize > 0) {
//						if (monitorErrorOutput) {
//							processErrorHandler.setDataOutputStream(dos);
//							processErrorHandler.setLineBufferSize(progressLineBufferSize);
//						} else {
//							processOutputHandler.setDataOutputStream(dos);
//							processOutputHandler.setLineBufferSize(progressLineBufferSize);
//						}
//					}
//
//					processOutputHandler.start();
//					processErrorHandler.start();
//
//					// this blocks until the process completes.  An external stimulus might interrupt here
//					rsp.setExitCode(process.waitFor());
//
//					rsp.setStdoutArray(processOutputHandler.getOutputArray());
//					rsp.setStderrArray(processErrorHandler.getOutputArray());
//
//				}
//				catch (InterruptedException ex) {
//					// Interrupts only happen because of a timeout situation at this time.  However, it would be possible
//					// for RE to send an interrupt so that exec processes could be canceled from RE
//					log.info("Execution " + process + " was interrupted, terminating external process..");
//					try {
//						process.destroy();
//						processOutputHandler.interrupt();
//						processOutputHandler.join();
//						processErrorHandler.interrupt();
//						processErrorHandler.join();
//					}
//					catch (Exception destroyEx) {
//						log.warn("Unable to terminate external process.");
//					}
//					
//					rsp.setExitCode(CANCELLED_CODE);
//
//					rsp.setStdoutArray(processOutputHandler.getOutputArray());
//					rsp.setStderrArray(processErrorHandler.getOutputArray());
//				}
//				catch (Exception e) {
//					log.error("Caught exception calling exec", e);
//					rsp.addStderr(e.getMessage());
//				}
//				finally {
//				}
//			}
//		} catch (Exception e) {
//			log.error("Caught exception producing output", e);
//			rsp.addStderr("Caught exception producing output: " + e.getMessage());
//		}
//		return rsp;
//	}
//
//	private String[] getAddEnv(String[] envp) {
//		ArrayList<String> env = new ArrayList<String>();
//		for (Map.Entry<String, String> var : System.getenv().entrySet()) {
//			env.add(var.getKey() + "=" + var.getValue());
//		}
//		if (SystemUtils.IS_OS_UNIX && !System.getenv().keySet().contains("HOME")) {
//			env.add("HOME=" + jarPath);
//		}
//		if (envp != null) {
//			for (String var : envp) {
//				env.add(var);
//			}
//		}
//		return env.toArray(new String[env.size()]);
//	}
//	
//	public void handleSyncRequest() {
//		try {
//			log.info("Handling sync request, instruction type = " + instruction.getClass().getSimpleName());
//			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
//			
//			XmlObject response = null;
//			
//			if (instruction instanceof CapabilitiesRequestDocument) {
//				response = handleCapabilitiesRequest();
//			}
//
//			if (response != null) {
//				writeResponse(response, dos);
//			}
//		} 
//		catch (Exception e) {
//			log.error("Caught exception producing output", e);
//		} 
//		finally {
//			if (sock != null) {
//				try {
//					sock.close();
//				} catch (Exception e) {
//				}
//			}
//		}
//		
//	}
//
//	private XmlObject handleCapabilitiesRequest() {
//		CapabilitiesResponse response = CapabilitiesResponseDocument.Factory.newInstance(xmlOptions).addNewCapabilitiesResponse();
//		for (String cmd : getCommandKeys()) {
//			response.addCommandKey(cmd);
//		}
//
//		return response;
//	}
//
//	private static String getJarPath() {
//		if (jarPath == null) {
//			try {
//				File jarFile = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//				jarPath = jarFile.getParentFile().getAbsolutePath();
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//		}
//		return jarPath;
//	}
//
//
//	/**
//	 *  ProcessStreamHandlers are used to stream stdout/stderr outputs back over the calling socket connection
//	 *  which allows RE to monitor results/update % complete as it happens
//	 *
//	 */
//	private class ProcessStreamHandler extends Thread {
//		private String processId;
//		private InputStream is;
//		private String streamType;
//		private DataOutputStream dataOutputStream;
//		private int lineBufferSize;
//		private List<String> outList = new ArrayList<String>();
//
//		ProcessStreamHandler(String processId, InputStream is, String streamType) {
//			this.processId = processId;
//			this.is = is;
//			this.streamType = streamType;
//		}
//
//		public void setDataOutputStream(DataOutputStream dataOutputStream) {
//			this.dataOutputStream = dataOutputStream;
//		}
//
//		public void setLineBufferSize(int lineBufferSize) {
//			this.lineBufferSize = lineBufferSize;
//		}
//
//		public String[] getOutputArray() {
//			synchronized (outList) {
//				return outList.toArray(new String[outList.size()]);
//			}
//		}
//
//		public void run() {
//			BufferedReader br = null;
//			try {
//				synchronized (outList) {
//					log.info(processId + "(" + streamType + ")" + ": Start, progress stream is " + (dataOutputStream != null ? "set" : "not set") + ", lineBufferSize = " + lineBufferSize);
//					InputStreamReader isr = new InputStreamReader(is);
//					br = new BufferedReader(isr);
//					String line = null;
//					while ((line = br.readLine()) != null) {
//						if (Thread.currentThread().isInterrupted()) {
//							log.info("ProcessStreamHandler interrupted!  Closing.");
//							outList.add("Process interrupted.");
//							return;
//						}
//						outList.add(line);
//						log.info(processId + " (" + streamType + ")" + ": " + line);
//						sendProgressData(false);
//					}
//					log.info(processId + " (" + streamType + ")" + ": Complete!");
//					sendProgressData(true);
//				}
//			} catch (IOException e) {
//				log.error("Exception while reading process output stream", e);
//			}
//			finally {
//				if (br != null) {
//					try {
//						br.close();						
//					}
//					catch (Exception ex) {}
//				}
//			}
//		}
//
//		private void sendProgressData(boolean flush) {
//			if (dataOutputStream != null && outList.size() > 0 && (flush || outList.size() >= lineBufferSize)) {
//				ExecServerProgress progress = ExecServerProgressDocument.Factory.newInstance().addNewExecServerProgress();
//				progress.setLineArray(getOutputArray());
//
//				try {
//					byte[] bytes = progress.xmlText(xmlOptions).getBytes("UTF-8");
//					synchronized (dataOutputStream) {
//						dataOutputStream.writeInt(bytes.length);
//						dataOutputStream.write(bytes);
//						log.info(processId + " (" + streamType + "): Sent " + bytes.length + " bytes of progress data...");
//					}
//				} catch (Exception e) {
//					log.error("Exception while sending progress data", e);
//				}
//				
//				outList.clear();
//			}
//		}
//	}
//	
	public static class ExecutionResult {
//		public ExecServerResponse response;
		public Date createdTime = new Date();
		public Date startTime;
		public Date finishedTime;
		public Exception exception;
	}
//	
//	/**
//	 * Runnable that constantly takes from the delayed execution queue, checks whether it is done,
//	 * and cancels if not (timeout situation)
//	 * 
//	 * @author davelamy
//	 *
//	 */
//	public static class TimeoutChecker implements Runnable {
//		private static final Log checkerLog = LogFactory.getLog(TimeoutChecker.class);
//
//		@Override
//		public void run() {
//			try {
//				while (true) {					
//					Thread.sleep(timeoutCheckInterval);
//					
//					List<ExecutionDelayInfo<ExecutionResult>> completedExecutions = new ArrayList<ExecutionDelayInfo<ExecutionResult>>();
//					
//					for (ExecutionDelayInfo<ExecutionResult> execution : timeoutQueue) {
//						if (execution.getDelay(TimeUnit.NANOSECONDS) < 0) {
//							Future<ExecutionResult> executionResult = execution.getFuture();
//							
//							if (!executionResult.isDone()) {
//								checkerLog.warn("Execution " + execution.getExecutionId() + " has been processing more than " + execution.getMaxTimeMillis() + " milliseconds, canceling..");
//								boolean success = executionResult.cancel(true);
//								
//								if (!success) {
//									checkerLog.warn("Unable to cancel execution " + execution.getExecutionId() + "!!");
//								}
//							}
//							else {
//								checkerLog.info("Execution " + execution.getExecutionId() + " has completed, removing from queue.");
//							}
//							
//							completedExecutions.add(execution);
//						}
//					}
//						
//					timeoutQueue.removeAll(completedExecutions);
//					
//				}
//			}
//			catch (InterruptedException ex) {
//				return;
//			}
//		}
//		
//	}
}
