package models;

import com.nesbot.commons.collections.ConcurrentOrderedMap;

import java.util.ArrayList;
import java.util.List;

public class Post
{
   public String title;
   public String slug;
   public long updated;

   private static ConcurrentOrderedMap<String, Post> posts = new ConcurrentOrderedMap<String, Post>();

   public Post(String title, String slug, long updated)
   {
      this.title = title;
      this.slug = slug;
      this.updated = updated;
   }

   public static Post findBySlug(String slug)
   {
      return posts.get(slug);
   }
   public static List<Post> findAll()
   {
      return posts.list();
   }
   public static List<Post> findMostRecent(int count)
   {
      if (count > count())
      {
         count = count();
      }
      List<Post> ret = new ArrayList<Post>();
      for (int i = 0 ; i < count ; i++)
      {
         ret.add(posts.get(i));
      }
      return ret;
   }
   public static int count()
   {
      return posts.size();
   }
   public static void clear()
   {
      posts.clear();
   }
   public Post save()
   {
      return posts.append(slug, this) ? this : null;
   }
   public Post previous()
   {
      int i = posts.indexOf(slug);
      if (i == 0)
      {
         return null;
      }
      return posts.get(i - 1);
   }
   public Post next()
   {
      int i = posts.indexOf(slug);

      if (i == (count() - 1))
      {
         return null;
      }
      return posts.get(i + 1);
   }
}