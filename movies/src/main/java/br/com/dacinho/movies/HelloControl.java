package br.com.dacinho.movies;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloControl {
	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello World!";
	}
}
