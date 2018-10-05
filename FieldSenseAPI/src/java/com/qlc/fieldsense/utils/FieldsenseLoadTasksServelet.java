/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Ramesh 
 * @date 08-05-2015
 */
public class FieldsenseLoadTasksServelet extends HttpServlet {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("FieldsenseLoadTasksServelet");
    private int hitCount;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        log4jLog.info("Inside FieldsenseLoadTasksServelet class init() method");
        hitCount = 0;
    }

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        log4jLog.info("Inside FieldsenseLoadTasksServelet class doGet() method");
        if (hitCount == 0) {
            final long ONCE_PER_DAY = 1000 * 60 * 60 * 24;
            Date date12am = new Date();
            date12am.setDate(date12am.getDate() + 1);
            date12am.setHours(0);
            date12am.setMinutes(0);
            date12am.setSeconds(0);
            TimerTask task = new GenarateStats();
            Timer timer = new Timer();
            timer.schedule(task, date12am, ONCE_PER_DAY);
        }
        hitCount++;
        PrintWriter out = resp.getWriter();
        out.println("Hit Count  :" + hitCount);
    }
}
