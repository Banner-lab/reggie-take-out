package com.leexm.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.leexm.reggie.common.BaseContext;
import com.leexm.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import sun.management.counter.Variability;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginCheckFilter
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("拦截到请求: {}",requestURI);

        String[] uris = {
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(requestURI,uris);
        if(check){
            filterChain.doFilter(request, response);
            return ;
        }

        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录: {}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return ;
        }

        //判断移动端用户是否登录
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录: {}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return ;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    private boolean check(String uri,String[] urls){
        for(String url : urls){
            if(PATH_MATCHER.match(url,uri)){
                return true;
            }
        }
        return false;
    }
}
