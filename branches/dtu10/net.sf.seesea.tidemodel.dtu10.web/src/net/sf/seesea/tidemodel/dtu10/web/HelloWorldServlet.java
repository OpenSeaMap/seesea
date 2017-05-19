package net.sf.seesea.tidemodel.dtu10.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.*;

@Component(service = Servlet.class, property = "osgi.http.whiteboard.servlet.pattern=/hello", scope = ServiceScope.PROTOTYPE)
public class HelloWorldServlet extends javax.servlet.http.HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8943047581190688795L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println("Hello World");
	}
}