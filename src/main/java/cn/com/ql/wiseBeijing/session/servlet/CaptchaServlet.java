package cn.com.ql.wiseBeijing.session.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.xxtui.support.session.Captcha;
import cn.xxtui.support.session.SessionConstant;
import cn.xxtui.support.session.SessionControl;


@WebServlet(urlPatterns = "/auth/captcha")
public class CaptchaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log =LogManager.getLogger(CaptchaServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream outputStream =null;
		try {
			Captcha captcha = new Captcha();
			HttpSession session=request.getSession();
			SessionControl sessionControl=SessionControl.getInstance();
			//Map.Entry<String, String> pairs=new DefaultMapEntry("code",captcha.getCode());//用完后，记得remove掉
			//sessionControl.set(session.getId(), SessionConstant.CAPTCHA,pairs ); ;
			outputStream = response.getOutputStream();
			captcha.setOutputStream(outputStream);
			captcha.gen();
			session.setAttribute(SessionConstant.CAPTCHA, captcha.getCode());
			log.info(captcha.getCode());
		} catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug(ex.getMessage());
			}
			ex.printStackTrace();
		}
		finally
		{
			if(outputStream!=null)
			{
				outputStream.close();
			}
		}
	}

}
