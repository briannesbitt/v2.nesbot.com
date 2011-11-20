package helpers;

import play.Configuration;
import play.api.Play;

public class Config
{
   private static Configuration config;

   private static Configuration config()
   {
      if (config != null)
      {
         return config;
      }
      return config = Play.isDev(Play.current()) ? Configuration.root().getSub("dev") : Configuration.root().getSub("prod");
   }
   public static boolean isDev()
   {
      return Play.isDev(Play.current());
   }
   public static boolean isProd()
   {
      return Play.isProd(Play.current());
   }

   public static String postsPath()
   {
      return Configuration.root().getString("posts.path");
   }
   public static String urlbaseabsolute()
   {
      return config().getString("urlbaseabsolute");
   }
   public static String urlbaseabsoluteNoSlash()
   {
      String urlbaseabsolute = urlbaseabsolute();
      return urlbaseabsolute.endsWith("/") ? urlbaseabsolute.substring(0, urlbaseabsolute.length() - 1) : urlbaseabsolute;
   }
   public static String googleAnalyticsTracker()
   {
      return config().getString("gatracker");
   }
}