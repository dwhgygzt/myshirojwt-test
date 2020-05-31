package com.example.app.exception;

/**
 * 业务类异常
 *
 * @author <a href="mailto:guzhongtao@middol.com">guzhongtao</a>
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMsg;

    /**
     * 抛出异常,默认错误码 -1 .
     *
     * @param errorMsg 错误信息
     */
    public static void create(String errorMsg) {
        throw new BusinessException(errorMsg);
    }

    /**
     * 抛出异常，指定 errorCode
     */
    public static void create(String errorCode, String errorMsg) {
        throw new BusinessException(errorCode, errorMsg);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 默认构造，一般用于获取动态方法堆栈.
     */
    public BusinessException() {
        super();
    }

    /**
     * 根据异常信息构造异常.
     */
    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = "-1";
    }

    /**
     * 根据异常编号和参数构造异常.
     */
    public BusinessException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 屏蔽异常堆栈,业务类异常暂不需要爬栈.
     */
    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}
