import com.nesbot.commons.datetime.Dater;
import helpers.Config;
import models.Post;
import play.Application;
import play.GlobalSettings;
import play.mvc.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static play.mvc.Results.*;

public class Global extends GlobalSettings
{
   public static Application app;

   @Override
   public void beforeStart(Application app)
   {
      Global.app = app;
      play.Logger.info("Populating posts - start");

      Post.clear();

      TreeMap<Long, Post> posts = new TreeMap<Long, Post>();

      try
      {
         for (File f : new File(Config.postsPath()).listFiles())
         {
            if (f.isFile())
            {
               Pattern filePattern = Pattern.compile("^p([0-9]{4}_[0-9]{1,2}_[0-9]{1,2})_(.+)\\.scala.html$");

               Matcher matcher = filePattern.matcher(f.getName());
               if (matcher.matches() && matcher.groupCount() == 2)
               {
                  BufferedReader br = new BufferedReader(new FileReader(f.getCanonicalPath()));
                  String s = br.readLine();
                  br.close();

                  if (s.startsWith("@*") && s.endsWith("*@"))
                  {
                     Post p = new Post(s.substring(2, s.length()-2).trim(), matcher.group(2).replace('_','-'), Dater.create(matcher.group(1), "_").timestamp());
                     posts.put(p.updated, p);
                  }
               }
            }
         }

         for (Post p : posts.descendingMap().values())
         {
            p.save();
         }

         play.Logger.info("Populating posts - done");
      }
      catch(IOException ex)
      {
         play.Logger.error("************  STOP !!! **************", ex);
      }
      catch(Exception ex)
      {
         play.Logger.error("************  STOP !!! **************", ex);
      }
   }
   @Override
   public Result onActionNotFound(String uri)
   {
      if (Config.isDev())
      {
         return null;
      }

      return notFound(views.html.errors.notFound.render(uri));
   }
}