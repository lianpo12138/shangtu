package com.dubu.turnover.exception;
/**
 * @author yehefeng
 * @date 2016下午10:11:53
 *
 * http://www.dubuinfo.com
 * 上海笃步
 *
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.dubu.turnover.domain.enums.ErrorCode;

public class UserException extends RuntimeException{
	 private final static Logger logger = LoggerFactory.getLogger(UserException.class);

	    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	    private String errCode = ErrorCode.SYS_ERROR.getKey();
	    private String errMessage = ErrorCode.SYS_ERROR.getValue();

	    public UserException() {
	        super(ErrorCode.SYS_ERROR.getValue());
	    }

	    public UserException(String message) {
	        super(message);
	        this.errMessage = message;
	    }

	    public UserException(HttpStatus status, String message) {
	        super(message);
	        this.status = status;
	        this.errMessage = message;
	    }

	    public UserException(HttpStatus status, ErrorCode code) {
	        super(code.getValue());
	        this.status = status;
	        this.errCode = code.getKey();
	        this.errMessage = code.getValue();
	    }

	    public UserException(ErrorCode code) {
	        super(code.getValue());
	        this.errCode = code.getKey();
	        this.errMessage = code.getValue();
	    }

	    public UserException(ErrorCode code, String message) {
	        super(message);
	        this.errCode = code.getKey();
	        this.errMessage = message;
	    }

	    public HttpStatus getStatus() {
	        return status;
	    }

	    public ErrorMessage getResultMessage() {
	        return this.new ErrorMessage();
	    }
	    public OriginalMessage getOriginalMessage() {
	        return this.new OriginalMessage();
	    }

	    public class ErrorMessage {

	        public String getCode() {
	            return errCode;
	        }

	        public String getMessage() {
	            return errMessage;
	            //浠庡浗闄呭寲鏂囦欢涓幏鍙栨湰鍦版秷鎭�
	            //return MessageSourceUtils.getMessage(errCode);
	        }
	    }

	    public class OriginalMessage {

	        public String getCode() {
	            return errCode;
	        }

	        public String getMessage() {
	            //杩斿洖娑堟伅
	            return errMessage;
	        }
	    }

}
