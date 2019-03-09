package com.cloubi.blog.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloubi.blog.model.Blog;
import com.cloubi.blog.repository.BlogRepository;

@Controller
public class BlogController {
	
	@Autowired
	private BlogRepository blogRepository;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getAllBlogs() {
		List<Blog> blogs = blogRepository.findAll();
		
		// Sorting Blogs using Dates in ASCENDING ORDER.
		Collections.sort(blogs, new Comparator<Blog>() {
			  public int compare(Blog o1, Blog o2) {
			      return o1.getDate().compareTo(o2.getDate());
			  }
			});
		return new ModelAndView("mainview", "blogs", blogs);
	}
	
	@RequestMapping(value="/addnewblog", method=RequestMethod.GET)
	public ModelAndView addNewBlog(ModelMap model) {
		return new ModelAndView("addnewblog", "blog", new Blog());
	}
	
	@RequestMapping(value="/saveblog", method=RequestMethod.POST)
	public String saveBlog(@Valid Blog blog, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {
		
		if(blog.getBlogTitle().isEmpty() || blog.getBlogText().isEmpty() || result.hasErrors()) {
			logger.error(">>> while save blog has errors...");
			if (blog.getBlogTitle().isEmpty())
				model.addAttribute("emptyTitle", true);
			
			if (blog.getBlogText().isEmpty())
				model.addAttribute("emptyText", true);
			
			return "addnewblog";
		}
		
		blog.setDate(new Date());
		blogRepository.save(blog);
		return "redirect:/";
	}
	
	@RequestMapping(value="/viewblog/{id}", method=RequestMethod.GET)
	public ModelAndView viewBlog(@PathVariable String id, ModelMap model) {
		Optional<Blog> optional = blogRepository.findById(id);
		return new ModelAndView("viewblog", "blog", optional.get());
	}
	
	@RequestMapping(value="/editblog/{id}", method=RequestMethod.GET)
	public ModelAndView editBlog(@PathVariable String id, ModelMap model) {
		Optional<Blog> optional = blogRepository.findById(id);
		return new ModelAndView("editblog", "blog", optional.get());
	}
	
	@RequestMapping(value="/updateblog", method=RequestMethod.POST)
	public String updateBlog(@ModelAttribute("blog") Blog blog, ModelMap model) {
		
		Optional<Blog> optional = blogRepository.findById(blog.getId());
		Blog updatedBlog = optional.get();
		updatedBlog.setBlogTitle(blog.getBlogTitle());
		updatedBlog.setBlogText(blog.getBlogText());
		updatedBlog.setDate(new Date());
		
		if(updatedBlog.getBlogTitle().isEmpty() || updatedBlog.getBlogText().isEmpty()) {
			logger.error(">>> while update blog has errors...");
			if (updatedBlog.getBlogTitle().isEmpty())
				model.addAttribute("emptyTitle", true);
			
			if (updatedBlog.getBlogText().isEmpty())
				model.addAttribute("emptyText", true);
			
			return "editblog";
		}
		
		blogRepository.save(updatedBlog);
		return "redirect:/";
	}
	
	@RequestMapping(value="/deleteblog/{id}", method=RequestMethod.GET)
	public String deleteBlog(@PathVariable String id, ModelMap model) {
		blogRepository.deleteById(id);
		return "redirect:/";
	}

}
