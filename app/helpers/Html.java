package helpers;

import com.nesbot.commons.datetime.Dater;
import com.nesbot.commons.lang.Strings;
import controllers.routes;
import models.Post;
import play.Logger;
import play.api.Play;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

public class Html
{
   public static String prettyTimestamp(long ts)
   {
      return Dater.create(ts).toString("MMM d, yyyy");
   }
   public static String prettyTimestamp(Post post)
   {
      return prettyTimestamp(post.updated);
   }
   public static String toRFC822(long ts)
   {
      return Dater.create(ts).toRFC822();
   }
   public static String urlAssetFull(String file)
   {
      return Config.urlbaseabsoluteNoSlash() + routes.Assets.at(file);
   }
   public static String url(Post post)
   {
      Dater d = Dater.create(post.updated);
      return routes.Application.show(d.year(), d.month(), d.date(), post.slug).url();
   }
   public static String urlFull(Post post)
   {
      return Config.urlbaseabsoluteNoSlash() + url(post);
   }
   public static play.api.templates.Html nl2br(String s)
   {
      return new play.api.templates.Html(s.replaceAll("\n", "<br/>"));
   }
   public static String urlencode(String s)
   {
      try
      {
         return URLEncoder.encode(s, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
         Logger.error(s, e);
      }
      return s;
   }
   public static String tagName(Post post)
   {
      return Strings.format("{0}p{1}_{2}.scala.html", Config.postsPath(), Dater.create(post.updated).toString("yyyy_M_d"), post.slug.replace('-', '_'));
   }
   public static String view(Post post)
   {
      return Strings.format("{0}p{1}_{2}", Strings.replace(Config.postsPath(), "app/views/", "views.html."), Dater.create(post.updated).toString("yyyy_M_d"), post.slug.replace('-', '_')).replace('/', '.');
   }
   public static boolean viewExits(String file)
   {
      return Play.current().getFile(file).exists();
   }
   public static play.api.templates.Html render(Post post)
   {
      return renderDynamic(view(post));
   }
   public static play.api.templates.Html renderDynamic(String viewClazz)
   {
      try
      {
         Class<?> clazz = Play.current().classloader().loadClass(viewClazz);
         Method render = clazz.getDeclaredMethod("render");
         return (play.api.templates.Html)render.invoke(clazz);
      }
      catch(ClassNotFoundException ex)
      {
         Logger.error("Html.renderDynamic() : could not find view " + viewClazz);
      }
      catch(NoSuchMethodException ex)
      {
         Logger.error("Html.renderDynamic() : could not find render method " + viewClazz);
      }
      catch(IllegalAccessException ex)
      {
         Logger.error("Html.renderDynamic() : could not invoke render method " + viewClazz);
      }
      catch(InvocationTargetException ex)
      {
         Logger.error("Html.renderDynamic() : could not invoke render method " + viewClazz);
      }
      return play.api.templates.Html.empty();
   }
}