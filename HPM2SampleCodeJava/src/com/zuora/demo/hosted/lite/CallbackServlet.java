/**    
 *   Copyright (c) 2014 Zuora, Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *   this software and associated documentation files (the "Software"), to use copy, 
 *   modify, merge, publish the Software and to distribute, and sublicense copies of 
 *   the Software, provided no fee is charged for the Software.  In addition the
 *   rights specified above are conditioned upon the following:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   Zuora, Inc. or any other trademarks of Zuora, Inc.  may not be used to endorse
 *   or promote products derived from this Software without specific prior written
 *   permission from Zuora, Inc.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 *   ZUORA, INC. BE LIABLE FOR ANY DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *   ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.zuora.demo.hosted.lite;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zuora.demo.hosted.lite.util.HPMHelper;

/**
 * CallbackServlet is called after 
 * 		1. failed to display Hosted Page
 * 		2. succeeded and failed to submit Hosted Page
 * 
 * @author Tony.Liu, Chunyu.Jia.
 */
public class CallbackServlet extends HttpServlet {
	
	private static final long serialVersionUID = 6280395152816828551L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HPMHelper hpmHelper = HPMHelper.getInstance();
		
		// Print out callback parameters.
		System.out.println("Callback parameters are:");
		for(Object key : req.getParameterMap().keySet()) {
			System.out.println((String)key + " : " + req.getParameter((String)key));
		}
		
		// Validate signature.
		try {
			if(req.getParameter("signature") != null && req.getParameter("field_passthrough4") != null ) {
				if(!hpmHelper.isValidSignature(req.getParameter("field_passthrough4"), req.getParameter("signature"))) {
					throw new Exception("Invalid signature.");					
				}
			}
		} catch (Exception e) {
			System.out.println("Error happened during validating signature.");
			
			e.printStackTrace();
			
			throw new ServletException("Error happened during validating signature. " + e.getMessage());
		}
		
		req.getRequestDispatcher("/Result.jsp").forward(req, resp);
	}
}
