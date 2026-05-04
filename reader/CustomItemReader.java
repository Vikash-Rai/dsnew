package com.equabli.collectprism.reader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator.InvocationTargetThrowableWrapper;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;

import com.equabli.client.CommonRestClient;
import com.equabli.domain.Response;
import com.equabli.domain.helpers.CommonConstants;

public class CustomItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

	@Autowired
	private CommonRestClient client;

	private PagingAndSortingRepository<?, ?> repository;

	private Sort sort;

	private volatile int page = 0;

	private int pageSize = 10;

	private volatile int current = 0;

	private List<?> arguments;

	private volatile List<T> results;

	private final Object lock = new Object();

	private String methodName;
	
	private String authHeader;

	private volatile String jobName;

	private volatile Long totalSize = 0L;

	public CustomItemReader() {
		setName(ClassUtils.getShortName(CustomItemReader.class));
	}
	
	public String getAuthHeader() {
		return authHeader;
	}

	public void setAuthHeader(String authHeader) {
		this.authHeader = authHeader;
	}

	public void setArguments(List<?> arguments) {
		this.arguments = arguments;
	}

	public void setSort(Map<String, Sort.Direction> sorts) {
		this.sort = convertToSort(sorts);
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setRepository(PagingAndSortingRepository<?, ?> repository) {
		this.repository = repository;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(repository != null, "A PagingAndSortingRepository is required");
		Assert.state(pageSize > 0, "Page size must be greater than 0");
		Assert.state(sort != null, "A sort is required");
	}

	@Nullable
	@Override
	protected T doRead() throws Exception {

		synchronized (lock) {
			boolean nextPageNeeded = (results != null && current >= results.size());

			if (results == null || nextPageNeeded) {

				results = doPageRead();
				if(results.size() < pageSize) {
					page ++;
				}

				if(results.size() <= 0) {
					return null;
				}
				logger.debug("Processing " + results.size() + " Records For " + jobName + ". Processed " + totalSize + " Records.");
				totalSize = totalSize + results.size();
				Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - " + jobName + " In Progress", "Processing " + results.size() + " Records For " + jobName + ". Processed " + totalSize + " Records." + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
				if(mailResponse.getValidation()) {
					logger.info("Email Sent");
				}

				if (nextPageNeeded) {
					current = 0;
				}
			}

			if(current < results.size()) {
				T curLine = results.get(current);
				current++;
				return curLine;
			}
			else {
				return null;
			}
		}
	}

	@Override
	protected void jumpToItem(int itemLastIndex) throws Exception {
		synchronized (lock) {
			page = itemLastIndex / pageSize;
			current = itemLastIndex % pageSize;
		}
	}

	@SuppressWarnings("unchecked")
	protected List<T> doPageRead() throws Exception {
		Pageable pageRequest = PageRequest.of(page, pageSize, sort);

		MethodInvoker invoker = createMethodInvoker(repository, methodName);

		List<Object> parameters = new ArrayList<>();

		if(arguments != null && arguments.size() > 0) {
			parameters.addAll(arguments);
		}

		parameters.add(pageRequest);

		invoker.setArguments(parameters.toArray());

		Page<T> curPage = (Page<T>) doInvoke(invoker);

		return curPage.getContent();
	}

	@Override
	protected void doOpen() throws Exception {
		logger.debug(jobName + " Started!!! ");
		Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - " + jobName + " started", "Data Scrubbing - " + jobName + " started" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse.getValidation()) {
			logger.info("Email Sent");
		}
	}

	@Override
	protected void doClose() throws Exception {
		synchronized (lock) {
			current = 0;
			page = 0;
			results = null;
			logger.debug("Total " + totalSize + " Records Processed For " + jobName);
			logger.debug(jobName + " Completed!!! ");
			Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - " + jobName + " completed", "Total " + totalSize + " Records Processed For " + jobName + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE, authHeader);
			if(mailResponse.getValidation()) {
				logger.info("Email Sent");
			}
			totalSize = 0L;
		}
	}

	private Sort convertToSort(Map<String, Sort.Direction> sorts) {
		List<Sort.Order> sortValues = new ArrayList<>();

		for (Map.Entry<String, Sort.Direction> curSort : sorts.entrySet()) {
			sortValues.add(new Sort.Order(curSort.getValue(), curSort.getKey()));
		}

		return Sort.by(sortValues);
	}

	private Object doInvoke(MethodInvoker invoker) throws Exception{
		try {
			invoker.prepare();
		}
		catch (ClassNotFoundException | NoSuchMethodException e) {
			throw new DynamicMethodInvocationException(e);
		}

		try {
			return invoker.invoke();
		}
		catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			}
			else {
				throw new InvocationTargetThrowableWrapper(e.getCause());
			}
		}
		catch (IllegalAccessException e) {
			throw new DynamicMethodInvocationException(e);
		}
	}

	private MethodInvoker createMethodInvoker(Object targetObject, String targetMethod) {
		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(targetObject);
		invoker.setTargetMethod(targetMethod);
		return invoker;
	}


	
	
}
