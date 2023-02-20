package com.example.email.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

@Controller
public class ContactController {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/contact")
	public String showContactForm() {
		
		
		return "contact_forms";
	}
	
	
	@PostMapping("/contact")
	public String SubmitContact(HttpServletRequest request
			,@RequestParam("attachment") MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException {
		String fullname = request.getParameter("fullname");
		
		String s1 = request.getParameter("email");
	
		String[] email = s1.split(",");
		
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		
		//SimpleMailMessage message = new SimpleMailMessage();
		
		MimeMessage  message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		
		
		
		String mailSubject = subject;
		String mailContent ="<p><b>Sender Name :</b>"+ fullname + "</p>";
		mailContent += "<p><b>Recip E-mail : </b>"+ email + "</p>";
		mailContent += "<p><b>Subject : </b>"+ subject + "</p>";
		mailContent += "<p><b>Content : </b>"+ content + "</p>";
		
		mailContent += "<hr><img src='cid:logoImage' alt='image not found' />";
		
		
		
		
		
		helper.setFrom("manikarthik1611@gmail.com","manikarthik");
		helper.setTo(email);
		helper.setSubject(mailSubject);
		helper.setText(mailContent,true);
		
		ClassPathResource resource = new ClassPathResource("/static/images/ojaslogoo.png");
		helper.addInline("logoImage", resource);
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			InputStreamSource source = new InputStreamSource() {
				
				@Override
				public InputStream getInputStream() throws IOException {
					// TODO Auto-generated method stub
					return multipartFile.getInputStream();
				}
			};
			
			helper.addAttachment(fileName, source);
		}
		javaMailSender.send(message);
		
		
		return "message";
	}
}
