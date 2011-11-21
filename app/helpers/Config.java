package helpers;

import play.Configuration;
import play.api.Play;

public class Config
{
   private static Configuration _envConfig;

   public static void init(Configuration configuration)
   {
      _envConfig = Play.isDev(Play.current()) ? configuration.getSub("dev") : configuration.getSub("prod");
   }

   public static boolean isDev()
   {
      return Play.isDev(Play.current());
   }
   public static boolean isProd()
   {
      return Play.isProd(Play.current());
   }
   private static String getString(String key)
   {
      String envValue = _envConfig.getString(key);
      return (envValue != null) ? envValue : Configuration.root().getString(key);
   }

   public static String postsPath()
   {
      return getString("posts.path");
   }
   public static String urlbaseabsolute()
   {
      return getString("urlbaseabsolute");
   }
   public static String urlbaseabsoluteNoSlash()
   {
      String urlbaseabsolute = urlbaseabsolute();
      return urlbaseabsolute.endsWith("/") ? urlbaseabsolute.substring(0, urlbaseabsolute.length() - 1) : urlbaseabsolute;
   }
   public static String googleAnalyticsTracker()
   {
      return getString("gatracker");
   }
}