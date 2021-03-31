package org.example.core.web;

import org.example.constants.ServiceExceptionEnum;
import org.example.core.exception.ServiceException;
import org.example.core.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@ControllerAdvice
public class GlobeExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobeExceptionHandler.class);


    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Response<String> handlerServiceException(ServiceException serviceException) {
        LOGGER.error("[exceptionHandler]", serviceException);
        return Response.error(serviceException.getCode(), serviceException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response<String> exceptionHandler(Exception e) {
        LOGGER.error("[exceptionHandler]", e);
        return Response.error(ServiceExceptionEnum.SYS_ERROR.getCode(),
                ServiceExceptionEnum.SYS_ERROR.getMessage());
    }
}
