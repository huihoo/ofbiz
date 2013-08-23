package org.ofbiz.daniel.exercise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilHttp;

public class LearningEvents {
	public static String processFirstForm(HttpServletRequest request, HttpServletResponse response) {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		request.setAttribute("combined", firstName + " " + lastName);
		request.setAttribute("allParams", UtilHttp.getParameterMap(request));
		request.setAttribute("submit", "Submitted");
		return "success";
	}
}
