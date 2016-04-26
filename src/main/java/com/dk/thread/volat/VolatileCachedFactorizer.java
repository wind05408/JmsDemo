package com.dk.thread.volat;

import net.jcip.annotations.Immutable;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by dk on 2016/4/26.
 * 使用不可变对象的volatile类型引用，缓存最新的结果
 */
@WebServlet(name = "VolatileCachedFactorizer")
public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null,null);

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        BigInteger[] factors = cache.getFactors(i);
        if(factors==null){
            factors = factor(i);
            cache = new OneValueCache(i,factors);
        }
        encodeIntoResponse(servletResponse,factors);

    }

    private void encodeIntoResponse(ServletResponse servletResponse, BigInteger[] factors) {
    }

    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

    private BigInteger extractFromRequest(ServletRequest servletRequest) {
        return null;
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}

/**
 *
 */
@Immutable
class OneValueCache{
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger i,BigInteger[] factors){
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }

    public BigInteger[] getFactors(BigInteger i){
        if(lastNumber==null||!lastFactors.equals(i)){
            return null;
        }else{
            return Arrays.copyOf(lastFactors,lastFactors.length);
        }
    }
}



