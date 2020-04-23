package com.ljj.interceptor;

import com.alibaba.fastjson.JSON;
import com.ljj.entity.OperateLog;
import com.ljj.service.OperateLogService;
import com.ljj.utils.RequestUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志记录AOP实现
 * Created by ljj on 2020/4/23.
 */
@Aspect
public class LogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

	// 开始时间
	private long startTime = 0L;
	// 结束时间
	private long endTime = 0L;

	@Autowired
	OperateLogService operateLogService;

	@Before("execution(* *..controller..*.*(..))")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		LOGGER.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	@After("execution(* *..controller..*.*(..))")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		LOGGER.debug("doAfterInServiceLayer");
	}

	@Around("execution(* *..controller..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 获取request
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();

		OperateLog operateLog = new OperateLog();
		// 从注解中获取操作名称、获取响应结果
		Object result = pjp.proceed();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method.isAnnotationPresent(RequiresPermissions.class)) {
			RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
			String[] permissions = requiresPermissions.value();
			if (permissions.length > 0) {
				operateLog.setPermissions(permissions[0]);
			}
		}
		endTime = System.currentTimeMillis();
		LOGGER.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);

		operateLog.setBasePath(RequestUtil.getBasePath(request));
		operateLog.setIp(RequestUtil.getIpAddr(request));
		operateLog.setMethod(request.getMethod());
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			operateLog.setParameter(request.getQueryString());
		} else {
			operateLog.setParameter(ObjectUtils.toString(request.getParameterMap()));
		}
		operateLog.setResult(JSON.toJSONString(result));
		operateLog.setSpendTime((int) (endTime - startTime));
		operateLog.setStartTime(startTime);
		operateLog.setUri(request.getRequestURI());
		operateLog.setUrl(ObjectUtils.toString(request.getRequestURL()));
		operateLog.setUserAgent(request.getHeader("User-Agent"));
		operateLog.setUsername(ObjectUtils.toString(request.getUserPrincipal()));
		operateLogService.insertOperateLog(operateLog);
		return result;
	}

}
