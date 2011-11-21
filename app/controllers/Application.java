package controllers;

import helpers.Html;
import models.Post;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.errors.notFound;
import views.html.index;
import views.html.rss;
import views.html.show;

public class Application extends Controller
{
   public static Result index()
   {
      return ok(index.render(Post.findAll()));
   }
   public static Result show(Long year, Long month, Long day, String slug)
   {
      Post post = Post.findBySlug(slug);

      if (post == null)
      {
         return notFound(notFound.render(slug));
      }

      if (!Html.viewExits(Html.tagName(post)))
      {
         return notFound(notFound.render("template for : " + Html.tagName(post)));
      }

      return ok(show.render(post, post.next(), post.previous()));
   }
   public static Result rss()
   {
      response().setContentType("application/rss+xml");
      return ok(rss.render(Post.findAll()));
   }
}